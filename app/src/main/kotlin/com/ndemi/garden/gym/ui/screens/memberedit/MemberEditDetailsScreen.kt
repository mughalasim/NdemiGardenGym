package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.mock.getMockRegisteredMemberEntity
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.MemberCoachWidget
import com.ndemi.garden.gym.ui.widgets.MemberDueDateWidget
import com.ndemi.garden.gym.ui.widgets.MemberInfoWidget
import com.ndemi.garden.gym.ui.widgets.MemberSessionWidget
import cv.domain.entities.MemberEntity
import org.joda.time.DateTime

@Composable
fun MemberEditDetailsScreen(
    memberEntity: MemberEntity,
    onCoachSetUpdate: (Boolean) -> Unit = {},
    onMembershipDueDateUpdate: (DateTime) -> Unit = { },
    onViewAttendance: (memberEntity: MemberEntity) -> Unit = {},
    sessionMessage: String = "",
    sessionStartTime: DateTime? = null,
    onSessionStarted: () -> Unit = {},
    onSessionCompleted: (DateTime, DateTime) -> Unit = { _, _ -> },
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MemberInfoWidget(memberEntity)

        MemberCoachWidget(memberEntity = memberEntity, onCoachSetUpdate = onCoachSetUpdate)

        MemberSessionWidget(
            sessionMessage, sessionStartTime, onSessionStarted, onSessionCompleted
        )

        MemberDueDateWidget(memberEntity, onMembershipDueDateUpdate)

        ButtonWidget(title = "View Attendance") {
            onViewAttendance.invoke(memberEntity)
        }

    }
}

@AppPreview
@Composable
fun MemberEditDetailsScreenPreview() {
    AppThemeComposable {
        MemberEditDetailsScreen(
            memberEntity = getMockRegisteredMemberEntity(),
        )
    }
}
