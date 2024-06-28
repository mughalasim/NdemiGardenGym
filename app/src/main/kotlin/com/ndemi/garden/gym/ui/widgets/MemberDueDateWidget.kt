package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.mock.getMockActiveMemberEntity
import com.ndemi.garden.gym.ui.mock.getMockExpiredMemberEntity
import com.ndemi.garden.gym.ui.mock.getMockRegisteredMemberEntity
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toMembershipStatusString
import cv.domain.entities.MemberEntity
import org.joda.time.DateTime


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MemberDueDateWidget(
    memberEntity: MemberEntity,
    onMembershipDueDateUpdate: (DateTime) -> Unit = {},
) {
    val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    var datePickerVisibility by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(top = padding_screen)
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = line_thickness,
                color = AppTheme.colors.backgroundCardBorder,
                shape = RoundedCornerShape(border_radius),
            )
            .padding(padding_screen),
        horizontalAlignment = Alignment.Start
    ) {
        TextSmall(
            color = AppTheme.colors.highLight,
            text = "Update Membership Due date"
        )
        TextRegular(
            modifier = Modifier.padding(top = padding_screen_small),
            text = "Please select the date the members subscription will end"
        )

        ButtonWidget(
            title = memberEntity.renewalFutureDateMillis.toMembershipStatusString(),
        ) {
            datePickerVisibility = !datePickerVisibility
            errorMessage = ""
        }

        TextRegular(
            modifier = Modifier.padding(top = padding_screen),
            color = AppTheme.colors.backgroundError,
            text = errorMessage
        )

        if (datePickerVisibility) {
            DatePickerDialog(
                onDismissRequest = {
                    datePickerVisibility = !datePickerVisibility
                },
                confirmButton = {
                    Text(
                        text = "Update",
                        style = AppTheme.textStyles.regularBold,
                        modifier = Modifier
                            .padding(padding_screen)
                            .clickable {
                                datePickerVisibility = !datePickerVisibility
                                errorMessage = ""
                                state.selectedDateMillis?.let {
                                    val selectedTime = DateTime(it)
                                    if (selectedTime.isBeforeNow) {
                                        errorMessage = "You cannot set a date in the past"
                                    } else {
                                        onMembershipDueDateUpdate.invoke(selectedTime)
                                    }
                                }
                            }
                    )
                },
                dismissButton = {
                    Text(
                        text = "Cancel",
                        style = AppTheme.textStyles.regular,
                        modifier = Modifier
                            .padding(padding_screen)
                            .clickable {
                                datePickerVisibility = !datePickerVisibility
                            }
                    )
                }
            ) {
                DatePicker(state = state, showModeToggle = false, headline = null, title = null)
            }
        }
    }
}

@AppPreview
@Composable
fun MemberDueDateWidgetPreview(){
    AppThemeComposable {
        Column {
            MemberDueDateWidget(memberEntity = getMockRegisteredMemberEntity())
            MemberDueDateWidget(memberEntity = getMockExpiredMemberEntity())
            MemberDueDateWidget(memberEntity = getMockActiveMemberEntity())
        }
    }
}
