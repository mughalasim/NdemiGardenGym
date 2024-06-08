package com.ndemi.garden.gym.ui.screens.profile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.profile.ProfileScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.SharedPrefsUseCase
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class ProfileScreenViewModel(
    private val errorCodeConverter: ErrorCodeConverter,
    private val authUseCase: AuthUseCase,
    private val memberUseCase: MemberUseCase,
    private val sharedPrefsUseCase: SharedPrefsUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, ProfileScreenViewModel.Action>(UiState.Loading) {

    private lateinit var memberEntity: MemberEntity

    private val _sessionStartTime = MutableLiveData<DateTime?>()
    val sessionStartTime: LiveData<DateTime?> = _sessionStartTime


    init {
        val time = sharedPrefsUseCase.getStartedSession()
        _sessionStartTime.value = if (time.isEmpty()) null else DateTime.parse(time)
    }

    fun getMember() {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.getMember().also { result ->
                when (result) {
                    is DomainResult.Error ->
                        sendAction(Action.ShowError(errorCodeConverter.getMessage(result.error)))

                    is DomainResult.Success -> {
                        if(result.data.activeNowDate != null){
                            _sessionStartTime.value = DateTime(result.data.activeNowDate)
                            sharedPrefsUseCase.setStartedSession(_sessionStartTime.value.toString())
                        }
                        memberEntity = result.data
                        sendAction(Action.Success(result.data))
                    }
                }
            }
        }
    }

    fun setStartedSession() {
        _sessionStartTime.value = DateTime.now()
        sharedPrefsUseCase.setStartedSession(_sessionStartTime.value.toString())
        updateMemberLiveStatus()
    }

    private fun clearStartedSession() {
        _sessionStartTime.value = null
        sharedPrefsUseCase.setStartedSession("")
        updateMemberLiveStatus()
    }

    private fun updateMemberLiveStatus(){
        viewModelScope.launch {
            memberUseCase.updateMember(
                memberEntity.copy(activeNowDate =  _sessionStartTime.value?.toDate())
            )
        }
    }

    fun setAttendance(startDateTime: DateTime, endDateTime: DateTime) {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.addAttendance(startDateTime.toDate(), endDateTime.toDate())
                .also { result ->
                    clearStartedSession()
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

    fun onLogOutTapped() {
        authUseCase.logOut()
        navigationService.open(Route.LoginScreen, true)
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
