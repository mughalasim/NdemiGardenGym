package com.ndemi.garden.gym.ui.screens.live

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.live.LiveAttendanceScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.live.LiveAttendanceScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.usecase.MemberUseCase
import kotlinx.coroutines.launch

class LiveAttendanceScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val memberUseCase: MemberUseCase,
) : BaseViewModel<UiState, Action>(UiState.Loading) {
    // TODO - make flow
    fun getLiveMembers() {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.getLiveMembers().collect { result ->
                when (result) {
                    is DomainResult.Error ->
                        sendAction(Action.ShowDomainError(result.error, converter))

                    is DomainResult.Success -> {
                        sendAction(Action.Success(result.data))
                    }
                }
            }
        }
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data class Error(val message: String) : UiState

        data class Success(val members: List<MemberEntity>) : UiState
    }

    sealed interface Action : BaseAction<UiState> {
        data object SetLoading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class ShowDomainError(
            val domainError: DomainError,
            val errorCodeConverter: ErrorCodeConverter,
        ) : Action {
            override fun reduce(state: UiState): UiState = UiState.Error(errorCodeConverter.getMessage(domainError))
        }

        data class Success(val members: List<MemberEntity>) : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(members)
        }
    }
}
