package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.compose.runtime.Immutable
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.UiError
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import com.ndemi.garden.gym.ui.utils.isValidApartmentNumber
import com.ndemi.garden.gym.ui.utils.isValidPhoneNumber
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.StorageUseCase
import cv.domain.usecase.UpdateType
import kotlinx.coroutines.launch

class MemberEditScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val memberUseCase: MemberUseCase,
    private val authUseCase: AuthUseCase,
    private val storageUseCase: StorageUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, Action>(UiState.Loading) {

    private var memberId: String = ""
    private lateinit var mutableMemberEntity: MemberEntity
    private val _memberEntity = MutableLiveData<MemberEntity>(null)
    val memberEntity: LiveData<MemberEntity> = _memberEntity

    fun setMemberId(memberId: String){
        this.memberId = memberId
    }

    fun setString(value: String, inPutType: InputType) {
        mutableMemberEntity = when (inPutType) {
            InputType.FIRST_NAME -> mutableMemberEntity.copy(firstName = value)
            InputType.LAST_NAME -> mutableMemberEntity.copy(lastName = value)
            InputType.APARTMENT_NUMBER -> mutableMemberEntity.copy(apartmentNumber = value)
            InputType.PHONE_NUMBER -> mutableMemberEntity.copy(phoneNumber = value)
            InputType.HAS_COACH -> mutableMemberEntity.copy(hasCoach = value.toBoolean())
            InputType.NONE -> mutableMemberEntity
        }
        validateInput()
    }

    private fun validateInput() {
        val firstName = mutableMemberEntity.firstName
        val lastName = mutableMemberEntity.lastName
        val apartmentNumber = mutableMemberEntity.apartmentNumber.orEmpty()
        val phoneNumber = mutableMemberEntity.phoneNumber

        if (firstName.isEmpty() || firstName.isDigitsOnly()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_FIRST_NAME),
                    InputType.FIRST_NAME
                )
            )

        } else if (lastName.isEmpty() || lastName.isDigitsOnly()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_LAST_NAME),
                    InputType.LAST_NAME
                )
            )

        } else if (phoneNumber.isNotEmpty() && !phoneNumber.isValidPhoneNumber()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_PHONE_NUMBER),
                    InputType.PHONE_NUMBER
                )
            )

        } else if (apartmentNumber.isNotEmpty() && !apartmentNumber.isValidApartmentNumber()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiError.INVALID_APARTMENT_NUMBER),
                    InputType.APARTMENT_NUMBER
                )
            )
        } else if (mutableMemberEntity.isNotEqualTo(_memberEntity.value)){
            sendAction(Action.SetReadyToUpdate)
        } else {
            sendAction(Action.Success)
        }
    }

    fun getMemberForId() {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.getMemberById(memberId).also { result ->
                when (result) {
                    is DomainResult.Error ->
                        sendAction(Action.ShowError(converter.getMessage(result.error)))

                    is DomainResult.Success -> {
                        sendAction(Action.Success)
                        mutableMemberEntity = result.data
                        _memberEntity.value = result.data
                    }
                }
            }
        }
    }

    fun deleteMemberImage() {
        _memberEntity.value?.let {
            sendAction(Action.SetLoading)
            viewModelScope.launch {
                memberUseCase
                    .updateMember(it.copy(profileImageUrl = ""), UpdateType.PHOTO_DELETE)
                    .also { getMemberForId() }
            }
        }
    }

    fun updateMemberImage(byteArray: ByteArray) {
        _memberEntity.value?.let {
            sendAction(Action.SetLoading)
            viewModelScope.launch {
                val success = storageUseCase.updateImageForMember(it, byteArray)
                if (success){
                    getMemberForId()
                } else {
                    sendAction(Action.Success)
                }
            }
        }
    }

    fun navigateBack() {
        navigationService.popBack()
    }

    fun deleteMember() {
        _memberEntity.value?.let {
            sendAction(Action.SetLoading)
            viewModelScope.launch {
                memberUseCase.deleteMember(it).also {
                    when(it){
                        is DomainResult.Error -> Action.ShowError(
                            converter.getMessage(it.error)
                        )
                        is DomainResult.Success -> navigationService.popBack()
                    }
                }
            }
        }
    }

    fun onUpdateTapped() {
        if (uiStateFlow.value == UiState.ReadyToUpdate){
            sendAction(Action.SetLoading)
            viewModelScope.launch {
                memberUseCase.updateMember(
                    memberEntity = mutableMemberEntity,
                    updateType = UpdateType.MEMBER
                ).also { getMemberForId() }
            }
        }
    }

    fun hasAdminRights() =  authUseCase.hasAdminRights()

    enum class InputType {
        NONE,
        FIRST_NAME,
        LAST_NAME,
        APARTMENT_NUMBER,
        PHONE_NUMBER,
        HAS_COACH,
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data object ReadyToUpdate : UiState

        data class Error(val message: String, val inputType: InputType) : UiState

        data object Success : UiState

    }

    sealed interface Action : BaseAction<UiState> {
        data object SetReadyToUpdate: Action {
            override fun reduce(state: UiState) = UiState.ReadyToUpdate
        }

        data object SetLoading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class ShowError(val message: String, val inputType: InputType = InputType.NONE) : Action {
            override fun reduce(state: UiState): UiState = UiState.Error(message, inputType)
        }

        data object Success : Action {
            override fun reduce(state: UiState): UiState = UiState.Success
        }
    }
}
