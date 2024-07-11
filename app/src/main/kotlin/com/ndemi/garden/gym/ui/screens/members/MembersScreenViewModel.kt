package com.ndemi.garden.gym.ui.screens.members

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.members.MembersScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.members.MembersScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.UpdateType
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class MembersScreenViewModel (
    private val errorCodeConverter: ErrorCodeConverter,
    private val memberUseCase: MemberUseCase,
    private val authUseCase: AuthUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, Action>(UiState.Loading) {

    fun getMembers() {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.getAllMembers().also { result ->
                when(result){
                    is DomainResult.Error ->
                        sendAction(Action.ShowDomainError(result.error, errorCodeConverter))
                    is DomainResult.Success ->
                        sendAction(Action.Success(result.data))
                }
            }
        }
    }

    fun hasAdminRights() = authUseCase.hasAdminRights()

    fun onMemberTapped(memberEntity: MemberEntity) {
       navigationService.open(Route.MemberEditScreen(memberEntity.id))
    }

    fun onRegisterMember() {
        navigationService.open(Route.RegisterNewScreen)
    }

    fun onPaymentsTapped(memberEntity: MemberEntity) {
        navigationService.open(Route.PaymentsScreen(memberEntity.id, memberEntity.getFullName()))
    }

    fun onAttendanceTapped(memberEntity: MemberEntity) {
        navigationService.open(Route.MembersAttendancesScreen(memberEntity.id, memberEntity.getFullName()))
    }

    fun onSessionTapped(memberEntity: MemberEntity) {
        sendAction(Action.SetLoading)
        // Attempt to register an attendance
        if (memberEntity.isActiveNow()){
            viewModelScope.launch {
                memberUseCase.addAttendanceForMember(
                    memberEntity.id,
                    DateTime(memberEntity.activeNowDateMillis).toDate(),
                    DateTime.now().toDate()
                )
            }
        }

        // Update the member model
        viewModelScope.launch {
            memberUseCase.updateMember(
                memberEntity.copy(activeNowDateMillis =  if (memberEntity.isActiveNow()) null else DateTime.now().millis),
                UpdateType.ACTIVE_SESSION
            ).also {
                getMembers()
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
            override fun reduce(state: UiState): UiState =
                UiState.Error(errorCodeConverter.getMessage(domainError))
        }

        data class Success(val members: List<MemberEntity>) : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(members)
        }
    }
}
