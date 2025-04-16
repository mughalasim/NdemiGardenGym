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
import cv.domain.usecase.AttendanceUseCase
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.UpdateType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class MembersScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val memberUseCase: MemberUseCase,
    private val attendanceUseCase: AttendanceUseCase,
    private val authUseCase: AuthUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, Action>(UiState.Loading) {
    private val membersUnfiltered = MutableStateFlow<MutableList<MemberEntity>>(mutableListOf())
    private var screenType: MemberScreenType = MemberScreenType.ALL_MEMBERS

    private val _members = MutableStateFlow<List<MemberEntity>>(listOf())
    val members: StateFlow<List<MemberEntity>> = _members

    private val _searchTerm = MutableStateFlow("")
    val searchTerm: StateFlow<String> = _searchTerm

    fun getMembers(memberScreenType: MemberScreenType) {
        screenType = memberScreenType
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            val useCaseAction =
                when (memberScreenType) {
                    MemberScreenType.ALL_MEMBERS -> memberUseCase.getAllMembers()
                    MemberScreenType.EXPIRED_MEMBERS -> memberUseCase.getExpiredMembers()
                    MemberScreenType.LIVE_MEMBERS -> memberUseCase.getLiveMembers()
                }
            useCaseAction.also { result ->
                when (result) {
                    is DomainResult.Error ->
                        sendAction(Action.ShowDomainError(result.error, converter))
                    is DomainResult.Success -> {
                        membersUnfiltered.value.clear()
                        membersUnfiltered.value.addAll(result.data)
                        filterResults()
                        sendAction(Action.Success)
                    }
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
        if (memberEntity.isActiveNow()) {
            viewModelScope.launch {
                attendanceUseCase.addAttendanceForMember(
                    memberEntity.id,
                    DateTime(memberEntity.activeNowDateMillis).toDate(),
                    DateTime.now().toDate(),
                )
            }
        }

        // Update the member model
        viewModelScope.launch {
            memberUseCase.updateMember(
                memberEntity.copy(activeNowDateMillis = if (memberEntity.isActiveNow()) null else DateTime.now().millis),
                UpdateType.ACTIVE_SESSION,
            ).also {
                getMembers(screenType)
            }
        }
    }

    fun onSearchTextChanged(searchTerm: String) {
        _searchTerm.value = searchTerm
        filterResults()
    }

    private fun filterResults() {
        if (_searchTerm.value.isNotEmpty()) {
            _members.value =
                membersUnfiltered.value.filter {
                    it.getFullName().lowercase().contains(_searchTerm.value.lowercase())
                }
        } else {
            _members.value = membersUnfiltered.value
        }
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data class Error(val message: String) : UiState

        data object Success : UiState
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

        data object Success : Action {
            override fun reduce(state: UiState): UiState = UiState.Success
        }
    }
}
