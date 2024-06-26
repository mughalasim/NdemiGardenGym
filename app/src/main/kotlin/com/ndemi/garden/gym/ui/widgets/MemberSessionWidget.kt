package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants
import org.joda.time.DateTime

@Composable
fun MemberSessionWidget(
    message: String = "",
    sessionStartTime: DateTime? = null,
    onSessionStarted: () -> Unit = {},
    onSessionCompleted: (DateTime, DateTime) -> Unit = { _, _ -> },
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = padding_screen)
            .border(
                width = line_thickness,
                color = AppTheme.colors.backgroundChip,
                shape = RoundedCornerShape(border_radius),
            )
            .padding(padding_screen),
    ) {
        TextSmall(
            color = AppTheme.colors.highLight,
            text = "Workout session")

        if (sessionStartTime != null) {
            TextRegular(
                modifier = Modifier.padding(top = padding_screen_small),
                text = "Your work out session is in progress...")
            TextRegular(
                modifier = Modifier.padding(top = padding_screen_small),
                text = "Started at ${sessionStartTime.toString(DateConstants.formatTime)}"
            )
        } else {
            TextRegular(
                modifier = Modifier.padding(top = padding_screen_small),
                text = "Set session start and end time by tapping the button below")
        }

        ButtonWidget(
            title = if (sessionStartTime != null) "End session" else "Start session"
        ) {
            if (sessionStartTime != null){
                onSessionCompleted.invoke(sessionStartTime, DateTime.now())
            } else {
                onSessionStarted.invoke()
            }
        }

        if (sessionStartTime == null) {
            TextRegular(
                modifier = Modifier.padding(top = padding_screen),
                text = message,
                color = AppTheme.colors.backgroundError
            )
        }
    }
}

@AppPreview
@Composable
fun MemberSessionWidgetPreview(){
    AppThemeComposable {
        MemberSessionWidget()
    }
}
