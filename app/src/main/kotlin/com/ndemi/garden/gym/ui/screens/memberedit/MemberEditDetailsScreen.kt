package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.enums.MemberEditScreenInputType
import com.ndemi.garden.gym.ui.mock.getMockRegisteredMemberEntity
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.image_size_large
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants.formatDayMonthYear
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberImageWidget
import cv.domain.entities.MemberEntity
import cv.domain.entities.PermissionsEntity
import cv.domain.entities.getAdminPermissions
import org.joda.time.DateTime

@Composable
fun MemberEditDetailsScreen(
    uiState: UiState,
    permissionState: PermissionsEntity = PermissionsEntity(),
    memberEntity: MemberEntity,
    toolbarTitle: String = "",
    listeners: MemberEditScreenListeners = MemberEditScreenListeners(),
) {
    var errorFirstName = ""
    var errorLastName = ""
    var errorApartmentNumber = ""
    var errorPhoneNumber = ""
    var errorHeight = ""

    if (uiState is UiState.Error) {
        when (uiState.inputType) {
            MemberEditScreenInputType.FIRST_NAME -> errorFirstName = uiState.message
            MemberEditScreenInputType.LAST_NAME -> errorLastName = uiState.message
            MemberEditScreenInputType.APARTMENT_NUMBER -> errorApartmentNumber = uiState.message
            MemberEditScreenInputType.PHONE_NUMBER -> errorPhoneNumber = uiState.message
            MemberEditScreenInputType.HEIGHT -> errorHeight = uiState.message
            else -> Unit
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ToolBarWidget(
            title = toolbarTitle,
            canNavigateBack = true,
            secondaryIcon = if (permissionState.canDeleteMember) Icons.Default.DeleteForever else null,
            onSecondaryIconPressed = listeners.onDeleteMemberTapped,
            onBackPressed = listeners.onBackTapped,
        )
        Column(
            modifier = Modifier
                .padding(horizontal = padding_screen)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MemberImageWidget(
                modifier = Modifier.padding(top = padding_screen),
                imageUrl = memberEntity.profileImageUrl,
                canEditImage = permissionState.canEditMember,
                overrideImageSize = image_size_large,
                onImageSelect = listeners.onImageSelect,
                onImageDelete = listeners.onImageDelete,
            )

            Column(
                modifier = Modifier
                    .padding(top = padding_screen)
                    .toAppCardStyle(),
            ) {
                EditTextWidget(
                    hint = stringResource(R.string.txt_email),
                    textInput = memberEntity.email,
                    isEnabled = false,
                )

                EditTextWidget(
                    modifier = Modifier.padding(top = padding_screen),
                    hint = stringResource(R.string.txt_registration_date),
                    textInput = DateTime(memberEntity.registrationDateMillis).toString(formatDayMonthYear),
                    isEnabled = false,
                )

                EditTextWidget(
                    modifier = Modifier.padding(top = padding_screen),
                    hint = stringResource(R.string.txt_first_name),
                    textInput = memberEntity.firstName,
                    errorText = errorFirstName,
                    isEnabled = permissionState.canEditMember,
                ) {
                    listeners.onSetString.invoke(it, MemberEditScreenInputType.FIRST_NAME)
                }

                EditTextWidget(
                    modifier = Modifier.padding(top = padding_screen),
                    hint = stringResource(R.string.txt_last_name),
                    textInput = memberEntity.lastName,
                    errorText = errorLastName,
                    isEnabled = permissionState.canEditMember,
                ) {
                    listeners.onSetString.invoke(it, MemberEditScreenInputType.LAST_NAME)
                }

                EditTextWidget(
                    modifier = Modifier.padding(top = padding_screen),
                    hint = stringResource(R.string.txt_apartment_number),
                    textInput = memberEntity.apartmentNumber.orEmpty(),
                    errorText = errorApartmentNumber,
                    isEnabled = permissionState.canEditMember,
                ) {
                    listeners.onSetString.invoke(it, MemberEditScreenInputType.APARTMENT_NUMBER)
                }

                EditTextWidget(
                    modifier = Modifier.padding(top = padding_screen),
                    hint = stringResource(R.string.txt_phone_number),
                    textInput = memberEntity.phoneNumber,
                    errorText = errorPhoneNumber,
                    keyboardType = KeyboardType.Phone,
                    isEnabled = permissionState.canEditMember,
                ) {
                    listeners.onSetString.invoke(it, MemberEditScreenInputType.PHONE_NUMBER)
                }

                EditTextWidget(
                    modifier = Modifier.padding(top = padding_screen),
                    hint = stringResource(R.string.txt_height),
                    textInput = memberEntity.height,
                    errorText = errorHeight,
                    keyboardType = KeyboardType.Number,
                    isEnabled = permissionState.canEditMember,
                ) {
                    listeners.onSetString.invoke(it, MemberEditScreenInputType.HEIGHT)
                }

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = padding_screen),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    TextWidget(text = stringResource(id = R.string.txt_training_coach_assigned))
                    Switch(
                        checked = memberEntity.hasCoach,
                        enabled = permissionState.canAssignCoach,
                        onCheckedChange = {
                            listeners.onSetString.invoke(it.toString(), MemberEditScreenInputType.HAS_COACH)
                        },
                    )
                }
            }

            if (permissionState.canEditMember) {
                ButtonWidget(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = padding_screen),
                    title = stringResource(R.string.txt_update),
                    isEnabled = uiState is UiState.ReadyToUpdate,
                    isLoading = uiState is UiState.Loading,
                    hideKeyboardOnClick = true,
                    onButtonClicked = listeners.onUpdateTapped
                )
            }
        }
    }
}

@AppPreview
@Composable
private fun MemberEditDetailsScreenPreview() {
    AppThemeComposable {
        MemberEditDetailsScreen(
            uiState = UiState.ReadyToUpdate,
            memberEntity = getMockRegisteredMemberEntity(),
            permissionState = getAdminPermissions(),
            toolbarTitle = "Edit your details"
        )
    }
}
