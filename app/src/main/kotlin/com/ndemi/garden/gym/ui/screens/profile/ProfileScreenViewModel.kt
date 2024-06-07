package com.ndemi.garden.gym.ui.screens.profile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.UiError
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.profile.ProfileScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import kotlinx.coroutines.launch

class ProfileScreenViewModel (
    private val errorCodeConverter: ErrorCodeConverter,
    private val authUseCase: AuthUseCase,
    private val memberUseCase: MemberUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, ProfileScreenViewModel.Action>(UiState.Loading) {

    fun getMember() {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.getMember().also { result ->
                when(result){
                    is DomainResult.Error ->
                        sendAction(Action.ShowDomainError(result.error, errorCodeConverter))
                    is DomainResult.Success ->
                        sendAction(Action.Success(result.data))
                }
            }
        }
    }

    fun onLogOutTapped() {
        authUseCase.logOut()
        navigationService.open(Route.LoginScreen, true)
    }


    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data class Error(val message: String) : UiState

        data class Success(val memberEntity: MemberEntity, ) : UiState

    }

    sealed interface Action : BaseAction<UiState> {
        data object SetLoading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class ShowUiError(
            val uiError: UiError,
            val errorCodeConverter: ErrorCodeConverter,
        ) : Action {
            override fun reduce(state: UiState): UiState =
                UiState.Error(errorCodeConverter.getMessage(uiError))
        }

        data class ShowDomainError(
            val domainError: DomainError,
            val errorCodeConverter: ErrorCodeConverter,
        ) : Action {
            override fun reduce(state: UiState): UiState =
                UiState.Error(errorCodeConverter.getMessage(domainError))
        }

        data class Success(val memberEntity: MemberEntity) : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(memberEntity)
        }
    }
}