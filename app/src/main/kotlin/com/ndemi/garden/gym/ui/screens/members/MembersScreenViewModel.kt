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
import com.ndemi.garden.gym.ui.utils.OBSERVE_MEMBER_SCREEN
import cv.domain.DomainResult
import cv.domain.enums.DomainErrorType
import cv.domain.enums.MemberUpdateType
import cv.domain.mappers.MemberPresentationMapper
import cv.domain.presentationModels.MemberPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.JobRepository
import cv.domain.usecase.AttendanceUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.PermissionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MembersScreenViewModel(
    private val jobRepository: JobRepository,
    private val screenType: MemberScreenType,
    private val converter: ErrorCodeConverter,
    private val memberUseCase: MemberUseCase,
    private val attendanceUseCase: AttendanceUseCase,
    private val permissionsUseCase: PermissionsUseCase,
    private val navigationService: NavigationService,
    private val dateProviderRepository: DateProviderRepository,
    private val memberPresentationMapper: MemberPresentationMapper,
) : BaseViewModel<UiState, Action>(UiState.Loading) {
    private val membersUnfiltered = MutableStateFlow<MutableList<MemberPresentationModel>>(mutableListOf())

    private val _members = mutableStateListOf<MemberPresentationModel>()
    val members: List<MemberPresentationModel> = _members

    private val _searchTerm = MutableStateFlow("")
    val searchTerm: StateFlow<String> = _searchTerm

    init {
        getMembers()
    }

    fun getMembers() {
        sendAction(Action.SetLoading)
        jobRepository.add(
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
                            membersUnfiltered.value.addAll(result.data.map { memberPresentationMapper.getModel(it) })
                            filterResults()
                            sendAction(Action.Success)
                        }
                    }
                }
            },
            "$OBSERVE_MEMBER_SCREEN-$screenType",
        )
    }

    fun getPermissions() = permissionsUseCase.getPermissions()

    fun onMemberTapped(model: MemberPresentationModel) {
        navigationService.open(Route.MemberEditScreen(model.id))
    }

    fun onRegisterMember() {
        navigationService.open(Route.RegisterNewScreen)
    }

    fun onPaymentsTapped(model: MemberPresentationModel) {
        navigationService.open(Route.PaymentsScreen(model.id, model.fullName))
    }

    fun onAttendanceTapped(model: MemberPresentationModel) {
        navigationService.open(Route.MembersAttendancesScreen(model.id, model.fullName))
    }

    fun onSessionTapped(model: MemberPresentationModel) {
        sendAction(Action.SetLoading)
        // Attempt to register an attendance
        if (model.activeNowDateMillis != null) {
            viewModelScope.launch {
                attendanceUseCase.addAttendanceForMember(
                    model.id,
                    dateProviderRepository.getDate(model.activeNowDateMillis!!),
                    dateProviderRepository.getDate(),
                )
            }
        }

        // Update the member model
        viewModelScope.launch {
            val result = memberUseCase.getMemberById(model.id)
            if (result is DomainResult.Success) {
                val memberEntity = result.data
                memberUseCase
                    .updateMember(
                        memberEntity.copy(
                            activeNowDateMillis =
                                if (memberEntity.activeNowDateMillis != null) null else dateProviderRepository.getDate().time,
                        ),
                        MemberUpdateType.ACTIVE_SESSION,
                    )
            }
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
                    it.fullName.lowercase().contains(_searchTerm.value.lowercase())
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
