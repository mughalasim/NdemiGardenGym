package com.ndemi.garden.gym.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.mock.getMockRegisteredMemberEntity
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.member.MemberInfoWidget
import com.ndemi.garden.gym.ui.widgets.member.MemberSessionWidget
import cv.domain.entities.MemberEntity
import org.joda.time.DateTime

@Composable
fun ProfileDetailsScreen(
    memberEntity: MemberEntity,
    isAdmin: Boolean,
    message: String = "",
    sessionStartTime: DateTime? = null,
    onSessionStarted: () -> Unit = {},
    onSessionCompleted: (DateTime, DateTime) -> Unit = { _, _ -> },
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MemberInfoWidget(memberEntity = memberEntity, showExtraInfo = !isAdmin)

        if (memberEntity.hasPaidMembership() && !isAdmin){
            MemberSessionWidget(
                message = message,
                sessionStartTime = sessionStartTime,
                onSessionStarted = onSessionStarted,
                onSessionCompleted = onSessionCompleted,
            )
        }
    }
}

@AppPreview
@Composable
fun ProfileDetailsScreenPreview() {
    AppThemeComposable {
        ProfileDetailsScreen(
            memberEntity = getMockRegisteredMemberEntity(),
            isAdmin = false
        )
    }
}
