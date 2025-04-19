package com.ndemi.garden.gym.ui.screens.profile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.profile.ProfileScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.profile.ProfileScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.usecase.AttendanceUseCase
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.StorageUseCase
import cv.domain.usecase.UpdateType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class ProfileScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val authUseCase: AuthUseCase,
    private val memberUseCase: MemberUseCase,
    private val attendanceUseCase: AttendanceUseCase,
    private val storageUseCase: StorageUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, Action>(UiState.Loading) {
    private val _sessionStartTime = MutableStateFlow<DateTime?>(null)
    val sessionStartTime: StateFlow<DateTime?> = _sessionStartTime

    private val memberEntity = MutableStateFlow(MemberEntity())

    fun observeMember() {
        sendAction(Action.Loading)
        viewModelScope.launch {
            authUseCase.observeUser().collect { memberEntity ->
                this@ProfileScreenViewModel.memberEntity.value = memberEntity
                val sessionTime = this@ProfileScreenViewModel.memberEntity.value.activeNowDateMillis
                _sessionStartTime.value = if (sessionTime != null) DateTime(sessionTime) else null
                sendAction(Action.Success(memberEntity))
            }
        }
    }

    fun setStartedSession() {
        viewModelScope.launch {
            memberUseCase.updateMember(
                memberEntity.value.copy(activeNowDateMillis = DateTime.now().millis),
                UpdateType.ACTIVE_SESSION,
            )
        }
    }

    private fun clearStartedSession() {
        viewModelScope.launch {
            memberUseCase.updateMember(
                memberEntity.value.copy(activeNowDateMillis = null),
                UpdateType.ACTIVE_SESSION,
            )
        }
    }

    fun setAttendance(
        startDateTime: DateTime,
        endDateTime: DateTime,
    ) {
        sendAction(Action.Loading)
        viewModelScope.launch {
            attendanceUseCase.addAttendance(startDateTime.toDate(), endDateTime.toDate())
                .also { clearStartedSession() }
        }
    }

    fun isAdmin() = authUseCase.isNotMember()

    fun onLogOutTapped() {
        authUseCase.logOut()
        navigationService.open(Route.LoginScreen, true)
    }

    fun deleteMemberImage() {
        sendAction(Action.Loading)
        viewModelScope.launch {
            memberUseCase
                .updateMember(
                    memberEntity.value.copy(profileImageUrl = ""),
                    UpdateType.PHOTO_DELETE,
                )
        }
    }

    fun updateMemberImage(byteArray: ByteArray) {
        sendAction(Action.Loading)
        viewModelScope.launch {
            val message =
                when (val result = storageUseCase.updateImageForMember(memberEntity.value, byteArray)) {
                    is DomainResult.Error -> converter.getMessage(result.error)
                    else -> ""
                }
            sendAction(Action.Success(memberEntity = memberEntity.value, errorMessage = message))
        }
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data class Success(val memberEntity: MemberEntity, val errorMessage: String) : UiState
    }

    sealed interface Action : BaseAction<UiState> {
        data object Loading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class Success(val memberEntity: MemberEntity, val errorMessage: String = "") : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(memberEntity, errorMessage)
        }
    }
}
