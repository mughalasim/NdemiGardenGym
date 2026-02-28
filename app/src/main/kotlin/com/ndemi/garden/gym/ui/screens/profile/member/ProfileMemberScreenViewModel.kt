package com.ndemi.garden.gym.ui.screens.profile.member

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.enums.SnackbarType
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.profile.member.ProfileMemberScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.profile.member.ProfileMemberScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.enums.MemberUpdateType
import cv.domain.repositories.DateProviderRepository
import cv.domain.usecase.AccessUseCase
import cv.domain.usecase.AttendanceUseCase
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.StorageUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProfileMemberScreenViewModel(
    private val job: MutableList<Job>,
    private val converter: ErrorCodeConverter,
    private val authUseCase: AuthUseCase,
    private val accessUseCase: AccessUseCase,
    private val memberUseCase: MemberUseCase,
    private val attendanceUseCase: AttendanceUseCase,
    private val storageUseCase: StorageUseCase,
    private val navigationService: NavigationService,
    private val dateProviderRepository: DateProviderRepository,
) : BaseViewModel<UiState, Action>(UiState.Loading) {
    private var sessionCountdownJob: Job? = null
    private val memberEntity = MutableStateFlow(MemberEntity())

    private val _countdown = MutableStateFlow("")
    val countdown: StateFlow<String> = _countdown

    private val _registrationDate = MutableStateFlow("")
    val registrationDate: StateFlow<String> = _registrationDate

    private val _sessionStartTime = MutableStateFlow("")
    val sessionStartTime: StateFlow<String> = _sessionStartTime

    init {
        sendAction(Action.Loading)
        job +=
            viewModelScope.launch {
                authUseCase.observeUser().collect { result ->
                    memberEntity.value = result
                    _registrationDate.value = dateProviderRepository.formatMonthYear(result.registrationDateMillis)
                    _sessionStartTime.value = ""
                    if (result.activeNowDateMillis != null) {
                        startCounter()
                    } else {
                        sessionCountdownJob?.cancel()
                    }
                    updateWorkoutInformation()
                }
            }
    }

    private fun startCounter() {
        sessionCountdownJob?.cancel()
        sessionCountdownJob =
            viewModelScope.launch {
                _countdown
                    .onStart {
                        val startTime = memberEntity.value.activeNowDateMillis ?: 0
                        while (startTime > 0) {
                            _sessionStartTime.value = dateProviderRepository.formatTime(startTime)
                            emit(dateProviderRepository.toCountdownTimer(startTime))
                            delay(COUNTDOWN_SECONDS)
                        }
                    }.onCompletion {
                        _sessionStartTime.value = ""
                        _countdown.emit("")
                    }.collect { _countdown.emit(it) }
            }
    }

    private fun updateWorkoutInformation() {
        job +=
            viewModelScope.launch {
                attendanceUseCase.getMemberAttendancesForId(memberEntity.value.id, dateProviderRepository.getYear()).collect { result ->
                    when (result) {
                        is DomainResult.Error -> {
                            sendAction(Action.Success(memberEntity.value))
                        }

                        is DomainResult.Success -> {
                            sendAction(Action.Success(memberEntity.value, result.data.sumOf { it.attendances.size }))
                        }
                    }
                }
            }
    }

    private fun updateMemberSession(now: Long?) {
        viewModelScope.launch {
            memberUseCase.updateMember(
                memberEntity.value.copy(activeNowDateMillis = now),
                MemberUpdateType.ACTIVE_SESSION,
            )
        }
    }

    private fun setAttendance() {
        viewModelScope.launch {
            attendanceUseCase
                .addAttendance(dateProviderRepository.getDate(memberEntity.value.activeNowDateMillis!!), dateProviderRepository.getDate())
                .also { result ->
                    val snackbarState: Pair<SnackbarType, String> =
                        when (result) {
                            is DomainResult.Error -> Pair(SnackbarType.ERROR, converter.getMessage(result.error))
                            else -> Pair(SnackbarType.SUCCESS, "Successfully added attendance")
                        }
                    showSnackbar(snackbarState.first, snackbarState.second)
                    updateMemberSession(now = null)
                }
        }
    }

    fun onEditDetailsTapped() {
        navigationService.open(Route.MemberEditScreen(memberId = memberEntity.value.id))
    }

    fun onSessionTapped() {
        val startedTime = memberEntity.value.activeNowDateMillis
        if (startedTime == null) {
            updateMemberSession(now = dateProviderRepository.getDate().time)
        } else {
            setAttendance()
        }
    }

    fun onLogOutTapped() {
        accessUseCase.logOut()
    }

    fun onImageDeleted() {
        viewModelScope.launch {
            memberUseCase
                .updateMember(
                    memberEntity.value.copy(profileImageUrl = ""),
                    MemberUpdateType.PHOTO_DELETE,
                )
        }
    }

    fun updateMemberImage(byteArray: ByteArray) {
        viewModelScope.launch {
            val snackbarState: Pair<SnackbarType, String> =
                when (val result = storageUseCase.updateImageForMember(memberEntity.value, byteArray)) {
                    is DomainResult.Error -> Pair(SnackbarType.ERROR, converter.getMessage(result.error))
                    else -> Pair(SnackbarType.SUCCESS, "Successfully updated")
                }
            showSnackbar(snackbarState.first, snackbarState.second)
        }
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data class Success(
            val memberEntity: MemberEntity,
            val workouts: Int = 0,
        ) : UiState
    }

    sealed interface Action : BaseAction<UiState> {
        data object Loading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class Success(
            val memberEntity: MemberEntity,
            val workouts: Int = 0,
        ) : Action {
            override fun reduce(state: UiState): UiState =
                UiState.Success(
                    memberEntity = memberEntity,
                    workouts = workouts,
                )
        }
    }
}

private const val COUNTDOWN_SECONDS = 1000L
