package com.ndemi.garden.gym.ui.screens.attendance

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.appSnackbar.buildErrorSnackbar
import com.ndemi.garden.gym.ui.appSnackbar.buildSuccessSnackbar
import com.ndemi.garden.gym.ui.screens.attendance.AttendanceScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.attendance.AttendanceScreenViewModel.UiState
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import com.ndemi.garden.gym.ui.utils.OBSERVE_MEMBER_ATTENDANCE
import cv.domain.DomainResult
import cv.domain.presentationModels.AttendanceMonthPresentationModel
import cv.domain.presentationModels.AttendancePresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.JobRepository
import cv.domain.usecase.AttendanceUseCase
import cv.domain.usecase.PermissionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AttendanceScreenViewModel(
    private val memberId: String,
    private val showSnackbar: (AppSnackbarData) -> Unit,
    private val jobRepository: JobRepository,
    private val converter: ErrorCodeConverter,
    private val attendanceUseCase: AttendanceUseCase,
    private val permissionsUseCase: PermissionsUseCase,
    private val navigationService: NavigationService,
    dateProviderRepository: DateProviderRepository,
) : BaseViewModel<UiState, Action>(UiState.Loading) {
    private val _selectedYear: MutableStateFlow<Int> = MutableStateFlow(dateProviderRepository.getYear())
    val selectedYear: StateFlow<Int> = _selectedYear

    init {
        getAttendances()
    }

    fun getAttendances() {
        sendAction(Action.SetLoading)
        jobRepository.add(
            viewModelScope.launch {
                attendanceUseCase
                    .getMemberAttendancesForId(
                        memberId = memberId,
                        year = selectedYear.value,
                    ).collect { result ->
                        when (result) {
                            is DomainResult.Error -> {
                                showSnackbar(buildErrorSnackbar(converter.getMessage(result.error)))
                                sendAction(Action.ShowInputError)
                            }

                            is DomainResult.Success -> {
                                sendAction(Action.Success(result.data))
                            }
                        }
                    }
            },
            OBSERVE_MEMBER_ATTENDANCE,
        )
    }

    fun increaseYear() {
        _selectedYear.value += 1
        getAttendances()
    }

    fun decreaseYear() {
        _selectedYear.value -= 1
        getAttendances()
    }

    fun deleteAttendance(model: AttendancePresentationModel) {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            attendanceUseCase
                .deleteAttendance(
                    attendanceId = model.attendanceId,
                    startYear = model.startYear,
                    startMonth = model.startMonth,
                ).also { result ->
                    when (result) {
                        is DomainResult.Error -> {
                            showSnackbar(buildErrorSnackbar(converter.getMessage(result.error)))
                            sendAction(Action.ShowInputError)
                        }

                        is DomainResult.Success -> {
                            showSnackbar(buildSuccessSnackbar("Successfully deleted"))
                            getAttendances()
                        }
                    }
                }
        }
    }

    fun navigateBack() {
        navigationService.popBack()
    }

    fun getPermissions() = permissionsUseCase.getPermissions(memberId)

    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data object Error : UiState

        data class Success(
            val attendancesMonthly: List<AttendanceMonthPresentationModel>,
        ) : UiState
    }

    sealed interface Action : BaseAction<UiState> {
        data object SetLoading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data object ShowInputError : Action {
            override fun reduce(state: UiState): UiState = UiState.Error
        }

        data class Success(
            val attendancesMonthly: List<AttendanceMonthPresentationModel>,
        ) : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(attendancesMonthly)
        }
    }
}
