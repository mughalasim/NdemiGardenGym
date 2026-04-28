package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.appSnackbar.buildErrorSnackbar
import com.ndemi.garden.gym.ui.appSnackbar.buildSuccessSnackbar
import com.ndemi.garden.gym.ui.enums.MemberEditScreenInputType
import com.ndemi.garden.gym.ui.enums.UiErrorType
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.Action
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.enums.MemberType
import cv.domain.enums.MemberUpdateType
import cv.domain.mappers.MemberPresentationMapper
import cv.domain.presentationModels.MemberEditPresentationModel
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.NumberFormatUseCase
import cv.domain.usecase.PermissionsUseCase
import cv.domain.usecase.StorageUseCase
import cv.domain.validator.RegisterScreenValidators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Suppress("detekt.LongParameterList")
class MemberEditScreenViewModel(
    private val showSnackbar: (AppSnackbarData) -> Unit,
    private val memberId: String,
    private val memberUseCase: MemberUseCase,
    private val validators: RegisterScreenValidators,
    private val converter: ErrorCodeConverter,
    private val storageUseCase: StorageUseCase,
    private val navigationService: NavigationService,
    private val permissionsUseCase: PermissionsUseCase,
    private val numberFormatUseCase: NumberFormatUseCase,
    private val memberPresentationMapper: MemberPresentationMapper,
) : BaseViewModel<UiState, Action>(UiState.Loading) {
    private val initialMemberModel = MutableStateFlow(MemberEditPresentationModel())
    private val _memberModel = MutableStateFlow(initialMemberModel.value)
    val memberModel: StateFlow<MemberEditPresentationModel> = _memberModel

    init {
        getMemberForId()
    }
    // TODO - extract all the string from here

    fun getMemberForId() {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.getMemberById(memberId).also { result ->
                sendAction(Action.SetWaiting)
                when (result) {
                    is DomainResult.Error -> {
                        showSnackbar(buildErrorSnackbar(converter.getMessage(result.error)))
                    }

                    is DomainResult.Success -> {
                        initialMemberModel.value = memberPresentationMapper.getEditModel(result.data)
                        _memberModel.value = initialMemberModel.value
                    }
                }
            }
        }
    }

    fun setString(
        value: String,
        inPutType: MemberEditScreenInputType,
    ) {
        _memberModel.value =
            when (inPutType) {
                MemberEditScreenInputType.FIRST_NAME -> {
                    _memberModel.value.copy(firstName = value)
                }

                MemberEditScreenInputType.LAST_NAME -> {
                    _memberModel.value.copy(lastName = value)
                }

                MemberEditScreenInputType.APARTMENT_NUMBER -> {
                    _memberModel.value.copy(apartmentNumber = value)
                }

                MemberEditScreenInputType.PHONE_NUMBER -> {
                    _memberModel.value.copy(phoneNumber = value)
                }

                MemberEditScreenInputType.HAS_COACH -> {
                    _memberModel.value.copy(hasCoach = value.toBoolean())
                }

                MemberEditScreenInputType.HEIGHT -> {
                    _memberModel.value.copy(height = value)
                }

                MemberEditScreenInputType.NONE -> {
                    _memberModel.value
                }
            }
        validateInput()
    }

    private fun validateInput() {
        when {
            validators.name.isNotValid(_memberModel.value.firstName) -> {
                sendAction(
                    Action.ShowInputError(
                        converter.getMessage(UiErrorType.INVALID_FIRST_NAME),
                        MemberEditScreenInputType.FIRST_NAME,
                    ),
                )
            }

            validators.name.isNotValid(_memberModel.value.lastName) -> {
                sendAction(
                    Action.ShowInputError(
                        converter.getMessage(UiErrorType.INVALID_LAST_NAME),
                        MemberEditScreenInputType.LAST_NAME,
                    ),
                )
            }

            validators.phoneNumber.isNotValid(_memberModel.value.phoneNumber) -> {
                sendAction(
                    Action.ShowInputError(
                        converter.getMessage(UiErrorType.INVALID_PHONE_NUMBER),
                        MemberEditScreenInputType.PHONE_NUMBER,
                    ),
                )
            }

            validators.height.isNotValid(_memberModel.value.height) -> {
                sendAction(
                    Action.ShowInputError(
                        converter.getMessage(UiErrorType.INVALID_HEIGHT),
                        MemberEditScreenInputType.HEIGHT,
                    ),
                )
            }

            validators.apartmentNumber.isNotValid(_memberModel.value.apartmentNumber) -> {
                sendAction(
                    Action.ShowInputError(
                        converter.getMessage(UiErrorType.INVALID_APARTMENT_NUMBER),
                        MemberEditScreenInputType.APARTMENT_NUMBER,
                    ),
                )
            }

            initialMemberModel.value != _memberModel.value -> {
                sendAction(Action.SetReadyToUpdate)
            }

            else -> {
                sendAction(Action.SetWaiting)
            }
        }
    }

    fun deleteMemberImage() {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            val result = memberUseCase.getMemberById(memberModel.value.id)
            if (result is DomainResult.Success) {
                memberUseCase
                    .updateMember(result.data.copy(profileImageUrl = ""), MemberUpdateType.PHOTO_DELETE)
                    .also {
                        getMemberForId()
                        showSnackbar(buildSuccessSnackbar("Delete image successful"))
                    }
            }
        }
    }

    fun updateMemberImage(byteArray: ByteArray) {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            when (val result = memberUseCase.getMemberById(memberModel.value.id)) {
                is DomainResult.Error -> {
                    showSnackbar(buildErrorSnackbar(converter.getMessage(result.error)))
                    sendAction(Action.SetWaiting)
                }

                is DomainResult.Success -> {
                    val success = storageUseCase.updateImageForMember(result.data, byteArray)
                    sendAction(Action.SetWaiting)
                    if (success is DomainResult.Success) {
                        getMemberForId()
                        showSnackbar(buildSuccessSnackbar("Update Image successful"))
                    } else {
                        showSnackbar(buildErrorSnackbar(converter.getMessage((success as DomainResult.Error).error)))
                    }
                }
            }
        }
    }

    fun navigateBack() {
        navigationService.popBack()
    }

    fun deleteMember() {
        sendAction(Action.SetLoading)
        viewModelScope.launch {
            memberUseCase.deleteMember(_memberModel.value.id).also {
                when (it) {
                    is DomainResult.Error -> {
                        showSnackbar(buildErrorSnackbar(converter.getMessage(it.error)))
                    }

                    is DomainResult.Success -> {
                        showSnackbar(buildSuccessSnackbar(converter.getString(R.string.txt_successfully_deleted)))
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
                val result = memberUseCase.getMemberById(memberModel.value.id)
                if (result is DomainResult.Success) {
                    memberUseCase
                        .updateMember(
                            memberEntity =
                                result.data.copy(
                                    firstName = _memberModel.value.firstName,
                                    lastName = _memberModel.value.lastName,
                                    phoneNumber = _memberModel.value.phoneNumber,
                                    apartmentNumber = _memberModel.value.apartmentNumber,
                                    height =
                                        if (_memberModel.value.height.isEmpty()) {
                                            0.0
                                        } else {
                                            numberFormatUseCase.setHeight(_memberModel.value.height.toDouble())
                                        },
                                    hasCoach = _memberModel.value.hasCoach,
                                    memberType = MemberType.valueOf(_memberModel.value.memberType),
                                ),
                            memberUpdateType = MemberUpdateType.DETAILS,
                        ).also {
                            showSnackbar(buildSuccessSnackbar(converter.getString(R.string.txt_successfully_updated)))
                            navigateBack()
                        }
                }
            }
        }
    }

    fun getPermissions() = permissionsUseCase.getPermissions(_memberModel.value.id)

    fun setNewMemberType(memberType: String) {
        _memberModel.value = _memberModel.value.copy(memberType = memberType)
        if (initialMemberModel.value.memberType != _memberModel.value.memberType) {
            sendAction(Action.SetReadyToUpdate)
        }
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data object ReadyToUpdate : UiState

        data object Waiting : UiState

        data class Error(
            val message: String,
            val inputType: MemberEditScreenInputType,
        ) : UiState
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

        data class ShowInputError(
            val message: String,
            val inputType: MemberEditScreenInputType = MemberEditScreenInputType.NONE,
        ) : Action {
            override fun reduce(state: UiState): UiState = UiState.Error(message, inputType)
        }
    }
}
