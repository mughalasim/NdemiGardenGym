package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
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
import cv.domain.entities.MemberEntity

@Composable
fun MemberCoachWidget(
    memberEntity: MemberEntity,
    onCoachSetUpdate: (Boolean) -> Unit = {},
) {
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
            text = stringResource(R.string.txt_update_training_coach_status)
        )
        TextRegular(
            modifier = Modifier.padding(top = padding_screen_small),
            text = stringResource(R.string.txt_select_training_coach)
        )

        ButtonWidget(
            title = if (memberEntity.hasCoach) {
                stringResource(R.string.txt_remove_training_coach)
            } else {
                stringResource(
                    R.string.txt_assign_training_coach
                )
            },
        ) {
            onCoachSetUpdate.invoke(!memberEntity.hasCoach)
        }
    }
}

@AppPreview
@Composable
fun MemberCoachWidgetPreview() {
    AppThemeComposable {
        Column {
            MemberCoachWidget(memberEntity = getMockRegisteredMemberEntity())
            MemberCoachWidget(memberEntity = getMockActiveMemberEntity())
            MemberCoachWidget(memberEntity = getMockExpiredMemberEntity())
        }
    }
}
