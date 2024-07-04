package com.ndemi.garden.gym.ui.screens.memberedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.mock.getMockRegisteredMemberEntity
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.MemberCoachWidget
import com.ndemi.garden.gym.ui.widgets.MemberInfoWidget
import com.ndemi.garden.gym.ui.widgets.MemberSessionWidget
import cv.domain.entities.MemberEntity
import org.joda.time.DateTime

@Composable
fun MemberEditDetailsScreen(
    memberEntity: MemberEntity,
    onCoachSetUpdate: (Boolean) -> Unit = {},
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

        MemberSessionWidget(
            sessionMessage, sessionStartTime, onSessionStarted, onSessionCompleted
        )

        MemberCoachWidget(memberEntity = memberEntity, onCoachSetUpdate = onCoachSetUpdate)

    }
}

@AppPreview
@Composable
fun MemberEditDetailsScreenPreview() {
    AppThemeComposable {
        Column (modifier = Modifier.verticalScroll(rememberScrollState())){
            MemberEditDetailsScreen(
                memberEntity = getMockRegisteredMemberEntity(),
            )
        }
    }
}
