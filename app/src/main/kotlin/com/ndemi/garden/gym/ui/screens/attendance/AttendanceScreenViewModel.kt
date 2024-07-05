package com.ndemi.garden.gym.ui.screens.attendance

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.ui.screens.attendance.AttendanceScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.attendance.AttendanceScreenViewModel.UiState
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.entities.AttendanceEntity
import cv.domain.usecase.MemberUseCase
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class AttendanceScreenViewModel(
    private val errorCodeConverter: ErrorCodeConverter,
    private val membersUseCase: MemberUseCase,
) : BaseViewModel<UiState, Action>(UiState.Loading) {

    private lateinit var selectedDate: DateTime

    fun getAttendances(selectedDate: DateTime) {
        this.selectedDate = selectedDate
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            membersUseCase.getMemberAttendances(
                year = selectedDate.year,
                month = selectedDate.monthOfYear
            ).also { result ->
                when (result) {
                    is DomainResult.Error ->
                        sendAction(Action.ShowDomainError(result.error, errorCodeConverter))

                    is DomainResult.Success ->
                        sendAction(Action.Success(result.data.first, result.data.second))
                }
            }
        }
    }

    fun deleteAttendance(attendanceEntity: AttendanceEntity) {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            membersUseCase.deleteAttendance(attendanceEntity).also { result ->
                when (result) {
                    is DomainResult.Error -> sendAction(
                        Action.ShowDomainError(
                            result.error,
                            errorCodeConverter
                        )
                    )

                    is DomainResult.Success -> getAttendances(selectedDate)
                }
            }
        }
    }


    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data class Error(val message: String) : UiState

        data class Success(val attendances: List<AttendanceEntity>, val totalMinutes: Int) : UiState

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

        data class Success(val attendances: List<AttendanceEntity>, val totalMinutes: Int) : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(attendances, totalMinutes)
        }
    }
}
