package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.compose.runtime.Immutable
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.enums.MemberEditScreenInputType
import com.ndemi.garden.gym.ui.enums.SnackbarType
import com.ndemi.garden.gym.ui.enums.UiErrorType
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import com.ndemi.garden.gym.ui.utils.isValidApartmentNumber
import com.ndemi.garden.gym.ui.utils.isValidHeight
import com.ndemi.garden.gym.ui.utils.isValidPhoneNumber
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.enums.MemberUpdateType
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.PermissionsUseCase
import cv.domain.usecase.StorageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MemberEditScreenViewModel(
    private val converter: ErrorCodeConverter,
    private val memberUseCase: MemberUseCase,
    private val permissionsUseCase: PermissionsUseCase,
    private val storageUseCase: StorageUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<UiState, Action>(UiState.Loading) {
    private val initialMemberEntity = MutableStateFlow(MemberEntity())
    private val _memberEntity = MutableStateFlow(initialMemberEntity.value)

    val memberEntity: StateFlow<MemberEntity> = _memberEntity

    fun getMemberForId(
        memberId: String,
        showMessage: Boolean = false,
    ) {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.getMemberById(memberId).also { result ->
                when (result) {
                    is DomainResult.Error -> {
                        showSnackbar(SnackbarType.ERROR, converter.getMessage(result.error))
                    }

                    is DomainResult.Success -> {
                        initialMemberEntity.value = result.data
                        _memberEntity.value = result.data
                        sendAction(Action.SetWaiting)
                        if (showMessage) {
                            showSnackbar(SnackbarType.SUCCESS, "Update successful")
                        }
                    }
                }
            }
        }
    }

    fun setString(
        value: String,
        inPutType: MemberEditScreenInputType,
    ) {
        _memberEntity.value =
            when (inPutType) {
                MemberEditScreenInputType.FIRST_NAME -> _memberEntity.value.copy(firstName = value)
                MemberEditScreenInputType.LAST_NAME -> _memberEntity.value.copy(lastName = value)
                MemberEditScreenInputType.APARTMENT_NUMBER -> _memberEntity.value.copy(apartmentNumber = value)
                MemberEditScreenInputType.PHONE_NUMBER -> _memberEntity.value.copy(phoneNumber = value)
                MemberEditScreenInputType.HAS_COACH -> _memberEntity.value.copy(hasCoach = value.toBoolean())
                MemberEditScreenInputType.HEIGHT -> _memberEntity.value.copy(height = value)
                MemberEditScreenInputType.NONE -> _memberEntity.value
            }
        validateInput()
    }

    private fun validateInput() {
        val firstName = _memberEntity.value.firstName
        val lastName = _memberEntity.value.lastName
        val apartmentNumber = _memberEntity.value.apartmentNumber.orEmpty()
        val phoneNumber = _memberEntity.value.phoneNumber
        val height = _memberEntity.value.height

        if (firstName.isEmpty() || firstName.isDigitsOnly()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiErrorType.INVALID_FIRST_NAME),
                    MemberEditScreenInputType.FIRST_NAME,
                ),
            )
        } else if (lastName.isEmpty() || lastName.isDigitsOnly()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiErrorType.INVALID_LAST_NAME),
                    MemberEditScreenInputType.LAST_NAME,
                ),
            )
        } else if (phoneNumber.isNotEmpty() && !phoneNumber.isValidPhoneNumber()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiErrorType.INVALID_PHONE_NUMBER),
                    MemberEditScreenInputType.PHONE_NUMBER,
                ),
            )
        } else if (!height.isValidHeight()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiErrorType.INVALID_HEIGHT),
                    MemberEditScreenInputType.HEIGHT,
                ),
            )
        } else if (apartmentNumber.isNotEmpty() && !apartmentNumber.isValidApartmentNumber()) {
            sendAction(
                Action.ShowError(
                    converter.getMessage(UiErrorType.INVALID_APARTMENT_NUMBER),
                    MemberEditScreenInputType.APARTMENT_NUMBER,
                ),
            )
        } else if (initialMemberEntity.value.isNotEqualTo(_memberEntity.value)) {
            sendAction(Action.SetReadyToUpdate)
        } else {
            sendAction(Action.SetWaiting)
        }
    }

    fun deleteMemberImage() {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase
                .updateMember(_memberEntity.value.copy(profileImageUrl = ""), MemberUpdateType.PHOTO_DELETE)
                .also { getMemberForId(initialMemberEntity.value.id, showMessage = true) }
        }
    }

    fun updateMemberImage(byteArray: ByteArray) {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            val success = storageUseCase.updateImageForMember(_memberEntity.value, byteArray)
            if (success is DomainResult.Success) {
                getMemberForId(initialMemberEntity.value.id, showMessage = true)
            } else {
                showSnackbar(SnackbarType.ERROR, converter.getMessage((success as DomainResult.Error).error))
            }
        }
    }

    fun navigateBack() {
        navigationService.popBack()
    }

    fun deleteMember() {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.deleteMember(_memberEntity.value).also {
                when (it) {
                    is DomainResult.Error ->
                        showSnackbar(SnackbarType.ERROR, converter.getMessage(it.error))

                    is DomainResult.Success -> {
                        showSnackbar(SnackbarType.SUCCESS, "Successfully deleted member")
                        navigateBack()
                    }
                }
            }
        }
    }

    fun onUpdateTapped() {
        if (uiStateFlow.value == UiState.ReadyToUpdate) {
            sendAction(Action.SetLoading)
            viewModelScope.launch {
                memberUseCase.updateMember(
                    memberEntity = _memberEntity.value,
                    memberUpdateType = MemberUpdateType.DETAILS,
                ).also { getMemberForId(initialMemberEntity.value.id, showMessage = true) }
            }
        }
    }

    fun getPermissions() = permissionsUseCase.getPermissions(_memberEntity.value.id)

    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data object ReadyToUpdate : UiState

        data object Waiting : UiState

        data class Error(val message: String, val inputType: MemberEditScreenInputType) : UiState
    }

    sealed interface Action : BaseAction<UiState> {
        data object SetReadyToUpdate : Action {
            override fun reduce(state: UiState) = UiState.ReadyToUpdate
        }

        data object SetLoading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data object SetWaiting : Action {
            override fun reduce(state: UiState): UiState = UiState.Waiting
        }

        data class ShowError(val message: String, val inputType: MemberEditScreenInputType = MemberEditScreenInputType.NONE) : Action {
            override fun reduce(state: UiState): UiState = UiState.Error(message, inputType)
        }
    }
}
