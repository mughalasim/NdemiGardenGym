package com.ndemi.garden.gym.ui.screens.members

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.members.MembersScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.members.MembersScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.enums.DomainErrorType
import cv.domain.enums.MemberUpdateType
import cv.domain.usecase.AttendanceUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.PermissionsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class MembersScreenViewModel(
    private val job: MutableList<Job>,
    private val screenType: MemberScreenType,
    private val converter: ErrorCodeConverter,
    private val memberUseCase: MemberUseCase,
    private val attendanceUseCase: AttendanceUseCase,
    private val permissionsUseCase: PermissionsUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, Action>(UiState.Loading) {
    private val membersUnfiltered = MutableStateFlow<MutableList<MemberEntity>>(mutableListOf())

    private val _members = mutableStateListOf<MemberEntity>()
    val members: List<MemberEntity> = _members

    private val _searchTerm = MutableStateFlow("")
    val searchTerm: StateFlow<String> = _searchTerm

    init {
        getMembers()
    }

    fun getMembers() {
        sendAction(Action.SetLoading)
        job +=
            viewModelScope.launch {
                val useCaseAction =
                    when (screenType) {
                        MemberScreenType.ALL_MEMBERS -> memberUseCase.getAllMembers()
                        MemberScreenType.EXPIRED_MEMBERS -> memberUseCase.getExpiredMembers()
                        MemberScreenType.LIVE_MEMBERS -> memberUseCase.getLiveMembers()
                        MemberScreenType.NON_MEMBERS -> memberUseCase.getNonMembers()
                    }
                useCaseAction.collect { result ->
                    when (result) {
                        is DomainResult.Error -> {
                            sendAction(Action.ShowDomainError(result.error, converter))
                        }

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

    fun getPermissions() = permissionsUseCase.getPermissions()

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
            memberUseCase
                .updateMember(
                    memberEntity.copy(activeNowDateMillis = if (memberEntity.isActiveNow()) null else DateTime.now().millis),
                    MemberUpdateType.ACTIVE_SESSION,
                )
        }
    }

    fun onSearchTextChanged(searchTerm: String) {
        _searchTerm.value = searchTerm
        filterResults()
    }

    private fun filterResults() {
        _members.clear()
        if (_searchTerm.value.isNotEmpty()) {
            _members.addAll(
                membersUnfiltered.value.filter {
                    it.getFullName().lowercase().contains(_searchTerm.value.lowercase())
                },
            )
        } else {
            _members.addAll(membersUnfiltered.value)
        }
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data class Error(
            val message: String,
        ) : UiState

        data object Success : UiState
    }

    sealed interface Action : BaseAction<UiState> {
        data object SetLoading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class ShowDomainError(
            val domainErrorType: DomainErrorType,
            val errorCodeConverter: ErrorCodeConverter,
        ) : Action {
            override fun reduce(state: UiState): UiState = UiState.Error(errorCodeConverter.getMessage(domainErrorType))
        }

        data object Success : Action {
            override fun reduce(state: UiState): UiState = UiState.Success
        }
    }
}
