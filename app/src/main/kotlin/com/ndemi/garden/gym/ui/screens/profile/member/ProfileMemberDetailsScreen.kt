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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockActiveMemberEntity
import com.ndemi.garden.gym.ui.screens.profile.ProfileTopSection
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants
import com.ndemi.garden.gym.ui.utils.getBMIColor
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.StatsTextWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberImageWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberSessionWidget
import org.joda.time.DateTime

@Composable
fun ProfileMemberDetailsScreen(
    state: ProfileMemberScreenViewModel.UiState.Success,
    countdown: String = "",
    listeners: ProfileMemberScreenListeners = ProfileMemberScreenListeners(),
) {
    Column {
        ProfileTopSection(onLogoutTapped = listeners.onLogoutTapped)

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
                    StatsTextWidget(
                        "${state.workouts}",
                        stringResource(R.string.txt_workouts),
                    )
                    StatsTextWidget(
                        state.memberEntity.getDisplayWeight(),
                        stringResource(R.string.txt_weight),
                    )
                    StatsTextWidget(
                        state.memberEntity.getDisplayHeight(),
                        stringResource(R.string.txt_height),
                    )
                    StatsTextWidget(
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

            TrackedWeightComponent()
        }
    }
}

data class ProfileMemberScreenListeners(
    val onImageDeleted: () -> Unit = {},
    val onImageSelected: () -> Unit = {},
    val onSessionTapped: () -> Unit = {},
    val onEditDetailsTapped: () -> Unit = {},
    val onLogoutTapped: () -> Unit = {},
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
        )
    }
