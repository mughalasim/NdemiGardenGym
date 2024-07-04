package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.StorageUseCase
import cv.domain.usecase.UpdateType
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class MemberEditScreenViewModel(
    private val errorCodeConverter: ErrorCodeConverter,
    private val memberUseCase: MemberUseCase,
    private val storageUseCase: StorageUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, Action>(UiState.Loading) {

    private lateinit var memberEntity: MemberEntity
    private val _sessionStartTime = MutableLiveData<DateTime?>()
    val sessionStartTime: LiveData<DateTime?> = _sessionStartTime

    fun getMemberForId(memberId: String) {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.getMemberById(memberId).also { result ->
                when (result) {
                    is DomainResult.Error ->
                        sendAction(Action.ShowError(errorCodeConverter.getMessage(result.error)))

                    is DomainResult.Success -> {
                        if(result.data.activeNowDateMillis != null){
                            _sessionStartTime.value = DateTime(result.data.activeNowDateMillis)
                        }
                        memberEntity = result.data
                        sendAction(Action.Success(result.data))
                    }
                }
            }
        }
    }

    fun setStartedSession(clear: Boolean = false) {
        _sessionStartTime.value = if (clear) null else DateTime.now()
        updateMemberLiveStatus()
    }

    private fun updateMemberLiveStatus() {
        viewModelScope.launch {
            memberUseCase.updateMember(
                memberEntity.copy(activeNowDateMillis =  _sessionStartTime.value?.millis),
                UpdateType.ACTIVE_SESSION
            )
        }
    }

    fun onCoachSetUpdate(hasCoach: Boolean) {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.updateMember(memberEntity.copy(hasCoach = hasCoach), UpdateType.HAS_COACH)
                .also { getMemberForId(memberEntity.id) }
        }
    }

    fun setAttendance(startDateTime: DateTime, endDateTime: DateTime) {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.addAttendanceForMember(memberEntity.id, startDateTime.toDate(), endDateTime.toDate())
                .also { result ->
                    setStartedSession(true)
                    when (result) {
                        is DomainResult.Error -> {
                            sendAction(
                                Action.Success(
                                    memberEntity,
                                    errorCodeConverter.getMessage(result.error)
                                )
                            )
                        }

                        is DomainResult.Success ->
                            sendAction(Action.Success(memberEntity))
                    }
                }
        }
    }

    fun deleteMemberImage() {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase
                .updateMember(memberEntity.copy(profileImageUrl = ""), UpdateType.PHOTO_DELETE)
                .also { getMemberForId(memberEntity.id) }
        }
    }

    fun updateMemberImage(byteArray: ByteArray) {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            val success = storageUseCase.updateImageForMember(memberEntity, byteArray)
            if (success){
                getMemberForId(memberEntity.id)
            } else {
                sendAction(Action.Success(memberEntity))
            }
        }
    }

    fun navigateBack() {
        navigationService.popBack()
    }

    fun deleteMember() {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.deleteMember(memberEntity).also {
                when(it){
                    is DomainResult.Error -> Action.Success(
                        memberEntity,
                        errorCodeConverter.getMessage(it.error)
                    )
                    is DomainResult.Success -> navigationService.popBack()
                }
            }
        }
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data class Error(val message: String) : UiState

        data class Success(val memberEntity: MemberEntity, val message: String) : UiState

    }

    sealed interface Action : BaseAction<UiState> {
        data object SetLoading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class ShowError(val message: String) : Action {
            override fun reduce(state: UiState): UiState = UiState.Error(message)
        }

        data class Success(val memberEntity: MemberEntity, val message: String = "") : Action {
            override fun reduce(state: UiState): UiState = UiState.Success(memberEntity, message)
        }
    }
}
