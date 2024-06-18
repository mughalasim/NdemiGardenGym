package com.ndemi.garden.gym.ui.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.MemberInfoWidget
import com.ndemi.garden.gym.ui.widgets.SessionWidget
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
            SessionWidget(
                message = message,
                sessionStartTime = sessionStartTime,
                onSessionStarted = onSessionStarted,
                onSessionCompleted = onSessionCompleted,
            )
        }

        if (isAdmin){
            ButtonWidget(
                title = "Register a new Member",
                isEnabled = true
            ) {
                onRegisterMember.invoke()
            }
        }

        TextSmall(
            color = AppTheme.colors.highLight,
            modifier = Modifier
                .padding(top = padding_screen)
                .padding(top = padding_screen)
                .clickable { onLogOut.invoke() },
            text = "Logout")
    }
}

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