package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class MemberEditScreenViewModel(
    private val errorCodeConverter: ErrorCodeConverter,
    private val authUseCase: AuthUseCase,
    private val memberUseCase: MemberUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, Action>(UiState.Loading) {

    private lateinit var memberEntity: MemberEntity

    fun getMemberForId(memberId: String) {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.getMemberById(memberId).also { result ->
                when (result) {
                    is DomainResult.Error ->
                        sendAction(Action.ShowError(errorCodeConverter.getMessage(result.error)))

                    is DomainResult.Success -> {
                        memberEntity = result.data
                        sendAction(Action.Success(result.data))
                    }
                }
            }
        }
    }

    private fun updateMemberLiveStatus(){
        viewModelScope.launch {
            memberUseCase.updateMember(
                memberEntity
            )
        }
    }

    fun setAttendance(startDateTime: DateTime, endDateTime: DateTime) {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.addAttendance(startDateTime.toDate(), endDateTime.toDate())
                .also { result ->
                    when (result) {
                        is DomainResult.Error -> {
                            sendAction(
                                Action.Success(
                                    memberEntity,
                                    errorCodeConverter.getMessage(result.error)
                                )
                            )
                        }

                        is DomainResult.Success ->
                            sendAction(Action.Success(memberEntity))
                    }
                }
        }
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data class Error(val message: String) : UiState

        data class Success(val memberEntity: MemberEntity, val message: String) : UiState

    }

    sealed interface Action : BaseAction<UiState> {
        data object SetLoading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class ShowError(val message: String) : Action {
            override fun reduce(state: UiState): UiState = UiState.Error(message)
        }

        data class Success(val memberEntity: MemberEntity, val message: String = "") : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(memberEntity, message)
        }
    }
}