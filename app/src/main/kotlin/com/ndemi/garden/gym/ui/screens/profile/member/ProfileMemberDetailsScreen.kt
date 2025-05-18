package com.ndemi.garden.gym.ui.screens.profile.member

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockActiveMemberEntity
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.theme.padding_screen_tiny
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants
import com.ndemi.garden.gym.ui.utils.getBMIColor
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.WeightWidget
import com.ndemi.garden.gym.ui.widgets.dialog.InputDialogWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberImageWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberSessionWidget
import cv.domain.entities.WeightEntity
import org.joda.time.DateTime

@Composable
fun ProfileMemberDetailsScreen(
    state: ProfileMemberScreenViewModel.UiState.Success,
    weightState: ProfileMemberScreenViewModel.WeightState,
    countdown: String = "",
    listeners: ProfileMemberScreenListeners = ProfileMemberScreenListeners(),
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(horizontal = padding_screen)
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(top = padding_screen)
                    .clickable { listeners.onEditDetailsTapped.invoke() }
                    .toAppCardStyle(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MemberImageWidget(
                    imageUrl = state.memberEntity.profileImageUrl,
                    onImageDelete = listeners.onImageDeleted,
                    onImageSelect = listeners.onImageSelected,
                )
                Column(
                    modifier = Modifier.padding(horizontal = padding_screen),
                ) {
                    TextWidget(
                        text = state.memberEntity.getFullName(),
                        style = AppTheme.textStyles.large,
                    )
                    TextWidget(
                        modifier = Modifier.padding(top = padding_screen_small),
                        text =
                            stringResource(
                                R.string.txt_member_since,
                                DateTime(state.memberEntity.registrationDateMillis).toString(DateConstants.formatMonthYear),
                            ),
                    )
                }
            }
            Row(
                modifier =
                    Modifier
                        .padding(vertical = padding_screen_small)
                        .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                StatsText(
                    "${state.workouts}",
                    stringResource(R.string.txt_workouts),
                )
                StatsText(
                    state.memberEntity.getDisplayWeight(),
                    stringResource(R.string.txt_weight),
                )
                StatsText(
                    state.memberEntity.getDisplayHeight(),
                    stringResource(R.string.txt_height),
                )
                StatsText(
                    state.memberEntity.getDisplayBMI(),
                    stringResource(R.string.txt_bmi),
                    overrideColor = getBMIColor(state.memberEntity.bmi),
                )
            }
        }

        MemberSessionWidget(
            sessionStartTime = state.memberEntity.activeNowDateMillis,
            countdown = countdown,
            onSessionTapped = listeners.onSessionTapped,
        )

        Column(
            modifier =
                Modifier
                    .padding(top = padding_screen)
                    .toAppCardStyle(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextWidget(
                    text = stringResource(R.string.txt_tracked_weight),
                    style = AppTheme.textStyles.large,
                )
                ButtonWidget(
                    title = stringResource(R.string.txt_add),
                    overridePadding = padding_screen_small,
                    isOutlined = true,
                ) {
                    showDialog = !showDialog
                }
            }
            if (state.memberEntity.trackedWeights.isEmpty()) {
                TextWidget(
                    modifier = Modifier.padding(top = padding_screen),
                    text = stringResource(R.string.txt_no_weight_recorded),
                )
            } else {
                for (weight in state.memberEntity.trackedWeights) {
                    WeightWidget(weightEntity = weight, onDeleteWeight = listeners.onDeleteWeightTapped)
                }
            }
        }
    }

    if (showDialog) {
        InputDialogWidget(
            title = stringResource(R.string.txt_capture_weight),
            message = stringResource(R.string.txt_capture_weight_desc),
            onDismissed = { showDialog = !showDialog },
            positiveButton = stringResource(R.string.txt_update),
            positiveOnClick = {
                showDialog = !showDialog
                listeners.onAddWeightTapped()
            },
            negativeButton = stringResource(R.string.txt_cancel),
            negativeOnClick = { showDialog = !showDialog },
            editTextInput = weightState.inputText,
            editTextError = weightState.errorText,
            editTextKeyboardType = KeyboardType.Number,
            editTextValueChanged = listeners.onWeightValueChanged,
        )
    }
}

@Composable
private fun StatsText(
    value: String,
    description: String,
    overrideColor: Color = AppTheme.colors.primary,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextWidget(
            color = overrideColor,
            text = value,
            style = AppTheme.textStyles.large,
        )
        TextWidget(
            modifier = Modifier.padding(top = padding_screen_tiny),
            text = description,
            style = AppTheme.textStyles.small,
        )
    }
}

data class ProfileMemberScreenListeners(
    val onImageDeleted: () -> Unit = {},
    val onImageSelected: () -> Unit = {},
    val onSessionTapped: () -> Unit = {},
    val onEditDetailsTapped: () -> Unit = {},
    val onWeightValueChanged: (String) -> Unit = {},
    val onDeleteWeightTapped: (WeightEntity) -> Unit = {},
    val onAddWeightTapped: () -> Unit = {},
)

@AppPreview
@Composable
fun ProfileMemberDetailsScreenPreview() =
    AppThemeComposable {
        ProfileMemberDetailsScreen(
            state =
                ProfileMemberScreenViewModel.UiState.Success(
                    memberEntity = getMockActiveMemberEntity(),
                    workouts = 240,
                ),
            weightState = ProfileMemberScreenViewModel.WeightState(),
        )
    }
