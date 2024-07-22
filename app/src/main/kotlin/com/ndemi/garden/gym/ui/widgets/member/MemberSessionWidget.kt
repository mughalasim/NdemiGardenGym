package com.ndemi.garden.gym.ui.widgets.member

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.TextRegularBold
import org.joda.time.DateTime

@Composable
fun MemberSessionWidget(
    message: String = "",
    sessionStartTime: DateTime?,
    onSessionStarted: () -> Unit = {},
    onSessionCompleted: (DateTime, DateTime) -> Unit = { _, _ -> },
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = padding_screen)
            .background(
                color = AppTheme.colors.backgroundCard,
                shape = RoundedCornerShape(border_radius)
            )
            .border(
                width = line_thickness,
                color = AppTheme.colors.backgroundCardBorder,
                shape = RoundedCornerShape(border_radius),
            )
            .padding(padding_screen),
    ) {
        TextRegularBold(
            color = AppTheme.colors.highLight,
            text = stringResource(R.string.txt_workout_session)
        )

        if (sessionStartTime != null) {
            TextRegular(
                modifier = Modifier.padding(top = padding_screen_small),
                text = stringResource(R.string.txt_your_workout_session_is_in_progress)
            )
            TextRegular(
                modifier = Modifier.padding(top = padding_screen_small),
                text = stringResource(
                    R.string.txt_started_at,
                    sessionStartTime.toString(DateConstants.formatTime)
                )
            )
        } else {
            TextRegular(
                modifier = Modifier.padding(top = padding_screen_small),
                text = stringResource(R.string.txt_workout_session_desc)
            )
        }

        TextRegularBold(
            modifier = Modifier
                .padding(top = padding_screen)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = if (sessionStartTime != null) {
                        AppTheme.colors.backgroundError
                    } else {
                        AppTheme.colors.highLight
                    },
                    shape = RoundedCornerShape(border_radius)
                )
                .padding(padding_screen)
                .clickable {
                    if (sessionStartTime != null) {
                        onSessionCompleted.invoke(sessionStartTime, DateTime.now())
                    } else {
                        onSessionStarted.invoke()
                    }
                },
            text = if (sessionStartTime != null) {
                stringResource(R.string.txt_end_session)
            } else {
                stringResource(R.string.txt_start_session)
            },
            textAlign = TextAlign.Center,
            color = Color.Black
        )

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
        Column {
            MemberSessionWidget(sessionStartTime = DateTime.now().minusHours(2))
            MemberSessionWidget(sessionStartTime = null)
        }
    }
}
