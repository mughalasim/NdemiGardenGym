package com.ndemi.garden.gym.ui.screens.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.line_thickness_small
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.MemberInfoWidget
import com.ndemi.garden.gym.ui.widgets.TextLarge
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.TextSmall
import cv.domain.entities.MemberEntity
import cv.domain.entities.getMockMemberEntity
import org.joda.time.DateTime

@Composable
fun ProfileDetailsScreen(
    memberEntity: MemberEntity,
    isAdmin: Boolean,
    message: String = "",
    sessionStartTime: DateTime? = null,
    onSessionStarted: () -> Unit = {},
    onSessionCompleted: (DateTime, DateTime) -> Unit = { _, _ -> },
    onRegisterMember: () -> Unit = {},
    onLogOut: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding_screen),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MemberInfoWidget(memberEntity = memberEntity, showExtraInfo = !isAdmin)

        if (memberEntity.hasPaidMembership() && !isAdmin){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = padding_screen)
                    .border(
                        width = line_thickness_small,
                        color = AppTheme.colors.backgroundChip,
                        shape = RoundedCornerShape(border_radius),
                    )
                    .padding(padding_screen_small),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextLarge(text = "Workout session")
                Spacer(modifier = Modifier.padding(padding_screen_small))
                ButtonWidget(
                    title = if (sessionStartTime != null) "End session" else "Start session",
                    isEnabled = true
                ) {
                    if (sessionStartTime != null){
                        onSessionCompleted.invoke(sessionStartTime, DateTime.now())
                    } else {
                        onSessionStarted.invoke()
                    }
                }

                if (sessionStartTime == null && message.isNotEmpty()) {
                    Spacer(modifier = Modifier.padding(padding_screen_small))
                    TextRegular(
                        text = message,
                        color = AppTheme.colors.backgroundError
                    )
                }

                if (sessionStartTime != null) {
                    Spacer(modifier = Modifier.padding(padding_screen_small))
                    TextSmall(text = "Your work out session is in progress...")
                    TextRegular(
                        text =
                        "Started at ${sessionStartTime.toString(DateConstants.formatTime)}"
                    )
                }
            }
        }

        if (isAdmin){
            ButtonWidget(
                modifier = Modifier.padding(top = padding_screen),
                title = "Register a new Member",
                isEnabled = true
            ) {
                onRegisterMember.invoke()
            }
        }

        ButtonWidget(
            modifier = Modifier.padding(top = padding_screen),
            title = "LogOut", isEnabled = sessionStartTime == null) {
            onLogOut.invoke()
        }
    }
}

data class ProfileDetailsScreenListeners(
    val memberEntity: MemberEntity,
    val isAdmin: Boolean,
    val message: String = "",
    val sessionStartTime: DateTime? = null,
    val onSessionStarted: () -> Unit = {},
    val onSessionCompleted: (DateTime, DateTime) -> Unit = { _, _ -> },
    val onRegisterMember: () -> Unit = {},
    val onLogOut: () -> Unit = {},
)

@AppPreview
@Composable
fun ProfileDetailsScreenPreview() {
    AppThemeComposable {
        ProfileDetailsScreen(
            memberEntity = getMockMemberEntity(),
            isAdmin = true
        )
    }
}