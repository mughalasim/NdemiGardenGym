package com.ndemi.garden.gym.ui.screens.membersattendances

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.membersattendances.MembersAttendancesScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.membersattendances.MembersAttendancesScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.AttendanceMonthEntity
import cv.domain.usecase.AttendanceUseCase
import cv.domain.usecase.AuthUseCase
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class MembersAttendancesScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val attendanceUseCase: AttendanceUseCase,
    private val authUseCase: AuthUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, Action>(UiState.Loading) {
    private lateinit var selectedDate: DateTime
    private lateinit var memberId: String

    fun getAttendances(
        memberId: String,
        selectedDate: DateTime,
    ) {
        this.selectedDate = selectedDate
        this.memberId = memberId
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            attendanceUseCase.getMemberAttendancesForId(
                memberId = memberId,
                year = selectedDate.year,
                month = selectedDate.monthOfYear,
            ).also { result ->
                when (result) {
                    is DomainResult.Error ->
                        sendAction(Action.ShowDomainError(result.error, converter))

                    is DomainResult.Success ->
                        sendAction(Action.Success(result.data))
                }
            }
        }
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

                    is DomainResult.Success -> getAttendances(memberId, selectedDate)
                }
            }
        }
    }

    fun navigateBack() {
        navigationService.popBack()
    }

    fun hasAdminRights() = authUseCase.hasAdminRights()

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
            val domainError: DomainError,
            val errorCodeConverter: ErrorCodeConverter,
        ) : Action {
            override fun reduce(state: UiState): UiState = UiState.Error(errorCodeConverter.getMessage(domainError))
        }

        data class Success(val attendancesMonthly: List<AttendanceMonthEntity>) : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(attendancesMonthly)
        }
    }
}
