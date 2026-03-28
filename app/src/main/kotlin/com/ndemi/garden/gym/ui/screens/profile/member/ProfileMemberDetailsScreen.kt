package com.ndemi.garden.gym.ui.screens.profile.member

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockMemberDashboardPresentationModel
import com.ndemi.garden.gym.ui.screens.weight.graph.WeightComponent
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.getBMIColor
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.StatsTextWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberImageWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberSessionWidget

@Composable
fun ProfileMemberDetailsScreen(
    state: ProfileMemberScreenViewModel.UiState.Success,
    countdown: String = "",
    sessionStartTime: String = "",
    listeners: ProfileMemberScreenListeners = ProfileMemberScreenListeners(),
) {
    Column {
        ToolBarWidget(
            title = stringResource(R.string.txt_profile),
            secondaryIcon = Icons.Default.Settings,
            onSecondaryIconPressed = listeners.onSettingsTapped,
        )

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
                        imageUrl = state.model.profileImageUrl,
                        onImageDelete = listeners.onImageDeleted,
                        onImageSelect = listeners.onImageSelected,
                    )
                    Column(
                        modifier = Modifier.padding(horizontal = padding_screen),
                    ) {
                        TextWidget(
                            text = state.model.fullName,
                            style = AppTheme.textStyles.large,
                        )
                        TextWidget(
                            modifier = Modifier.padding(top = padding_screen_small),
                            text =
                                stringResource(
                                    R.string.txt_member_since,
                                    state.model.registrationDate,
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
                    StatsTextWidget(
                        state.model.workouts,
                        stringResource(R.string.txt_workouts),
                    )
                    StatsTextWidget(
                        state.model.weight,
                        stringResource(R.string.txt_weight, state.model.weightUnit),
                    )
                    StatsTextWidget(
                        state.model.height,
                        stringResource(R.string.txt_height, state.model.heightUnit),
                    )
                    StatsTextWidget(
                        state.model.bmiValue.toString(),
                        stringResource(R.string.txt_bmi),
                        overrideColor = getBMIColor(state.model.bmiValue),
                    )
                }
            }

            MemberSessionWidget(
                sessionStartTime = sessionStartTime,
                countdown = countdown,
                onSessionTapped = listeners.onSessionTapped,
            )

            WeightComponent()
        }
    }
}

data class ProfileMemberScreenListeners(
    val onImageDeleted: () -> Unit = {},
    val onImageSelected: () -> Unit = {},
    val onSessionTapped: () -> Unit = {},
    val onEditDetailsTapped: () -> Unit = {},
    val onSettingsTapped: () -> Unit = {},
)

@AppPreview
@Composable
private fun ProfileMemberDetailsScreenPreview() =
    AppThemeComposable {
        ProfileMemberDetailsScreen(
            state =
                ProfileMemberScreenViewModel.UiState.Success(
                    model = getMockMemberDashboardPresentationModel(),
                ),
        )
    }
