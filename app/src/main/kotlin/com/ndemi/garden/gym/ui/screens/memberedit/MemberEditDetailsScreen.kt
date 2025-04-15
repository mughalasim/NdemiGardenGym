package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockRegisteredMemberEntity
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.InputType
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel.UiState
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants.formatDayMonthYear
import com.ndemi.garden.gym.ui.utils.toPhoneNumberString
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import cv.domain.entities.MemberEntity
import org.joda.time.DateTime

@Composable
fun MemberEditDetailsScreen(
    hasAdminRights: Boolean,
    uiState: UiState,
    memberEntity: MemberEntity,
    onSetString: (String, InputType) -> Unit = { _, _ -> },
    onUpdateTapped: () -> Unit = {},
) {
    var initialHasCoach by remember { mutableStateOf(memberEntity.hasCoach) }
    var errorFirstName = ""
    var errorLastName = ""
    var errorApartmentNumber = ""
    var errorPhoneNumber = ""

    if (uiState is UiState.Error) {
        when (uiState.inputType) {
            InputType.FIRST_NAME -> errorFirstName = uiState.message
            InputType.LAST_NAME -> errorLastName = uiState.message
            InputType.APARTMENT_NUMBER -> errorApartmentNumber = uiState.message
            InputType.PHONE_NUMBER -> errorPhoneNumber = uiState.message
            else -> Unit
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EditTextWidget(
            hint = stringResource(R.string.txt_email),
            textInput = memberEntity.email,
            isEnabled = false,
        ) {}

        EditTextWidget(
            hint = stringResource(R.string.txt_registration_date),
            textInput = DateTime(memberEntity.registrationDateMillis).toString(formatDayMonthYear),
            isEnabled = false,
        ) {}

        EditTextWidget(
            hint = stringResource(R.string.txt_first_name),
            textInput = memberEntity.firstName,
            errorText = errorFirstName,
            isEnabled = hasAdminRights,
        ) {
            onSetString.invoke(it, InputType.FIRST_NAME)
        }

        EditTextWidget(
            hint = stringResource(R.string.txt_last_name),
            textInput = memberEntity.lastName,
            errorText = errorLastName,
            isEnabled = hasAdminRights,
        ) {
            onSetString.invoke(it, InputType.LAST_NAME)
        }

        EditTextWidget(
            hint = stringResource(R.string.txt_apartment_number),
            textInput = memberEntity.apartmentNumber.orEmpty(),
            errorText = errorApartmentNumber,
            isEnabled = hasAdminRights,
        ) {
            onSetString.invoke(it, InputType.APARTMENT_NUMBER)
        }

        EditTextWidget(
            hint = stringResource(R.string.txt_phone_number),
            textInput = memberEntity.phoneNumber.toPhoneNumberString(),
            errorText = errorPhoneNumber,
            keyboardType = KeyboardType.Phone,
            isEnabled = hasAdminRights,
        ) {
            onSetString.invoke(it, InputType.PHONE_NUMBER)
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = padding_screen_small)
                    .padding(top = padding_screen_small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TextWidget(text = stringResource(id = R.string.txt_training_coach_assigned))
            Switch(
                checked = initialHasCoach,
                enabled = hasAdminRights,
                onCheckedChange = {
                    initialHasCoach = it
                    onSetString.invoke(initialHasCoach.toString(), InputType.HAS_COACH)
                },
            )
        }

        ButtonWidget(
            title = stringResource(R.string.txt_update),
            isEnabled = uiState is UiState.ReadyToUpdate && hasAdminRights,
            isLoading = uiState is UiState.Loading,
        ) {
            onUpdateTapped.invoke()
        }
    }
}

@AppPreview
@Composable
private fun MemberEditDetailsScreenPreview() {
    AppThemeComposable {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            MemberEditDetailsScreen(
                hasAdminRights = false,
                memberEntity = getMockRegisteredMemberEntity(),
                uiState = UiState.Loading,
            )
        }
    }
}
