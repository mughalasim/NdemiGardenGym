package com.ndemi.garden.gym.ui.screens.profile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.enums.SnackbarType
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.profile.ProfileScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.profile.ProfileScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.enums.MemberUpdateType
import cv.domain.usecase.AccessUseCase
import cv.domain.usecase.AttendanceUseCase
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.PermissionsUseCase
import cv.domain.usecase.StorageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class ProfileScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val authUseCase: AuthUseCase,
    private val accessUseCase: AccessUseCase,
    private val memberUseCase: MemberUseCase,
    private val attendanceUseCase: AttendanceUseCase,
    private val storageUseCase: StorageUseCase,
    private val permissionsUseCase: PermissionsUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, Action>(UiState.Loading) {

    private val memberEntity = MutableStateFlow(MemberEntity())

    private val _sessionStartTime = MutableStateFlow<DateTime?>(null)
    val sessionStartTime: StateFlow<DateTime?> = _sessionStartTime

    init {
        sendAction(Action.Loading)
        viewModelScope.launch {
            authUseCase.observeUser().collect { result ->
                memberEntity.value = result
                _sessionStartTime.value =
                    if (result.activeNowDateMillis != null) DateTime(result.activeNowDateMillis) else null
                sendAction(Action.Success(result))
            }
        }
    }

    fun setStartedSession() {
        viewModelScope.launch {
            memberUseCase.updateMember(
                memberEntity.value.copy(activeNowDateMillis = DateTime.now().millis),
                MemberUpdateType.ACTIVE_SESSION,
            )
        }
    }

    private fun clearStartedSession() {
        viewModelScope.launch {
            memberUseCase.updateMember(
                memberEntity.value.copy(activeNowDateMillis = null),
                MemberUpdateType.ACTIVE_SESSION,
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

    fun onLogOutTapped() {
        accessUseCase.logOut()
    }

    fun deleteMemberImage() {
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
            val snackbarState =
                when (val result = storageUseCase.updateImageForMember(memberEntity.value, byteArray)) {
                    is DomainResult.Error -> SnackbarState.Visible(SnackbarType.ERROR,
                        converter.getMessage(result.error))
                    else -> SnackbarState.Visible(SnackbarType.SUCCESS, "Successfully updated")
                }
            showSnackbar(snackbarState)
        }
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data class Success(val memberEntity: MemberEntity) : UiState
    }

    sealed interface Action : BaseAction<UiState> {
        data object Loading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class Success(val memberEntity: MemberEntity) : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(memberEntity)
        }
    }
}
