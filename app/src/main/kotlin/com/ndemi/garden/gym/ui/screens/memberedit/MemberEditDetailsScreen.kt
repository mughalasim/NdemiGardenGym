package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.enums.MemberEditScreenInputType
import com.ndemi.garden.gym.ui.enums.SnackbarType
import com.ndemi.garden.gym.ui.mock.getMockRegisteredMemberEntity
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_large
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants.formatDayMonthYear
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import cv.domain.entities.MemberEntity
import org.joda.time.DateTime

@Composable
fun MemberEditDetailsScreen(
    canUpdateMemberDetails: Boolean,
    canAssignCoach: Boolean,
    uiState: UiState,
    memberEntity: MemberEntity,
    onSetString: (String, MemberEditScreenInputType) -> Unit = { _, _ -> },
    snackbarHostState: AppSnackbarHostState = AppSnackbarHostState(),
    onUpdateTapped: () -> Unit = {},
) {
    var errorFirstName = ""
    var errorLastName = ""
    var errorApartmentNumber = ""
    var errorPhoneNumber = ""

    if (uiState is UiState.Error) {
        when (uiState.inputType) {
            MemberEditScreenInputType.FIRST_NAME -> errorFirstName = uiState.message
            MemberEditScreenInputType.LAST_NAME -> errorLastName = uiState.message
            MemberEditScreenInputType.APARTMENT_NUMBER -> errorApartmentNumber = uiState.message
            MemberEditScreenInputType.PHONE_NUMBER -> errorPhoneNumber = uiState.message
            MemberEditScreenInputType.NONE ->
                snackbarHostState.Show(
                    type = SnackbarType.ERROR,
                    message = uiState.message,
                )

            else -> Unit
        }
    }

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
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
            isEnabled = canUpdateMemberDetails,
        ) {
            onSetString.invoke(it, MemberEditScreenInputType.FIRST_NAME)
        }

        EditTextWidget(
            modifier = Modifier.padding(top = padding_screen),
            hint = stringResource(R.string.txt_last_name),
            textInput = memberEntity.lastName,
            errorText = errorLastName,
            isEnabled = canUpdateMemberDetails,
        ) {
            onSetString.invoke(it, MemberEditScreenInputType.LAST_NAME)
        }

        EditTextWidget(
            modifier = Modifier.padding(top = padding_screen),
            hint = stringResource(R.string.txt_apartment_number),
            textInput = memberEntity.apartmentNumber.orEmpty(),
            errorText = errorApartmentNumber,
            isEnabled = canUpdateMemberDetails,
        ) {
            onSetString.invoke(it, MemberEditScreenInputType.APARTMENT_NUMBER)
        }

        EditTextWidget(
            modifier = Modifier.padding(top = padding_screen),
            hint = stringResource(R.string.txt_phone_number),
            textInput = memberEntity.phoneNumber,
            errorText = errorPhoneNumber,
            keyboardType = KeyboardType.Phone,
            isEnabled = canUpdateMemberDetails,
        ) {
            onSetString.invoke(it, MemberEditScreenInputType.PHONE_NUMBER)
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
                enabled = canAssignCoach,
                onCheckedChange = {
                    onSetString.invoke(it.toString(), MemberEditScreenInputType.HAS_COACH)
                },
            )
        }
        if (canUpdateMemberDetails) {
            ButtonWidget(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = padding_screen_large),
                title = stringResource(R.string.txt_update),
                isEnabled = uiState is UiState.ReadyToUpdate,
                isLoading = uiState is UiState.Loading,
                hideKeyboardOnClick = true,
            ) {
                onUpdateTapped.invoke()
            }
        }
    }
}

@AppPreview
@Composable
private fun MemberEditDetailsScreenPreview() {
    AppThemeComposable {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            MemberEditDetailsScreen(
                canUpdateMemberDetails = true,
                canAssignCoach = true,
                memberEntity = getMockRegisteredMemberEntity(),
                uiState = UiState.ReadyToUpdate,
            )
        }
    }
}
