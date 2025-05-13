package com.ndemi.garden.gym.ui.screens.attendance

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.screens.attendance.AttendanceScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.attendance.AttendanceScreenViewModel.UiState
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.AttendanceMonthEntity
import cv.domain.enums.DomainErrorType
import cv.domain.usecase.AttendanceUseCase
import cv.domain.usecase.PermissionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class AttendanceScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val attendanceUseCase: AttendanceUseCase,
    private val permissionsUseCase: PermissionsUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, Action>(UiState.Loading) {
    private var memberId: String = ""

    private val _selectedDate: MutableStateFlow<DateTime> = MutableStateFlow(DateTime.now())
    val selectedDate: StateFlow<DateTime> = _selectedDate

    fun setMemberId(memberId: String) {
        this.memberId = memberId
    }

    fun getAttendances() {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            attendanceUseCase.getMemberAttendancesForId(
                memberId = memberId,
                year = selectedDate.value.year,
            ).collect { result ->
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

    fun increaseYear() {
        _selectedDate.value = _selectedDate.value.plusYears(1)
        getAttendances()
    }

    fun decreaseYear() {
        _selectedDate.value = _selectedDate.value.minusYears(1)
        getAttendances()
    }

    fun deleteAttendance(attendanceEntity: AttendanceEntity) {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            attendanceUseCase.deleteAttendance(attendanceEntity).also { result ->
                when (result) {
                    is DomainResult.Error ->
                        sendAction(
                            Action.ShowDomainError(
                                result.error,
                                converter,
                            ),
                        )

                    is DomainResult.Success -> getAttendances()
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

        data class Error(val message: String) : UiState

        data class Success(val attendancesMonthly: List<AttendanceMonthEntity>) : UiState
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

        data class Success(val attendancesMonthly: List<AttendanceMonthEntity>) : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(attendancesMonthly)
        }
    }
}
