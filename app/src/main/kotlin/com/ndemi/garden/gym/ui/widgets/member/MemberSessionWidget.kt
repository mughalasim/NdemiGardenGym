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
import com.ndemi.garden.gym.ui.widgets.TextWidget
import org.joda.time.DateTime

@Composable
fun MemberSessionWidget(
    message: String = "",
    sessionStartTime: DateTime?,
    onSessionStarted: () -> Unit = {},
    onSessionCompleted: (DateTime, DateTime) -> Unit = { _, _ -> },
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = padding_screen)
                .background(
                    color = AppTheme.colors.backgroundCard,
                    shape = RoundedCornerShape(border_radius),
                )
                .border(
                    width = line_thickness,
                    color = AppTheme.colors.border,
                    shape = RoundedCornerShape(border_radius),
                )
                .padding(padding_screen),
    ) {
        TextWidget(
            color = AppTheme.colors.primary,
            text = stringResource(R.string.txt_workout_session),
            style = AppTheme.textStyles.regularBold,
        )

        if (sessionStartTime != null) {
            TextWidget(
                modifier = Modifier.padding(top = padding_screen_small),
                text = stringResource(R.string.txt_your_workout_session_is_in_progress),
            )
            TextWidget(
                modifier = Modifier.padding(top = padding_screen_small),
                text =
                    stringResource(
                        R.string.txt_started_at,
                        sessionStartTime.toString(DateConstants.formatTime),
                    ),
            )
        } else {
            TextWidget(
                modifier = Modifier.padding(top = padding_screen_small),
                text = stringResource(R.string.txt_workout_session_desc),
            )
        }

        TextWidget(
            modifier =
                Modifier
                    .padding(top = padding_screen)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color =
                            if (sessionStartTime != null) {
                                AppTheme.colors.error
                            } else {
                                AppTheme.colors.primary
                            },
                        shape = RoundedCornerShape(border_radius),
                    )
                    .padding(padding_screen)
                    .clickable {
                        if (sessionStartTime != null) {
                            onSessionCompleted.invoke(sessionStartTime, DateTime.now())
                        } else {
                            onSessionStarted.invoke()
                        }
                    },
            text =
                if (sessionStartTime != null) {
                    stringResource(R.string.txt_end_session)
                } else {
                    stringResource(R.string.txt_start_session)
                },
            textAlign = TextAlign.Center,
            color = Color.Black,
            style = AppTheme.textStyles.regularBold,
        )

        if (sessionStartTime == null) {
            TextWidget(
                modifier = Modifier.padding(top = padding_screen),
                text = message,
                color = AppTheme.colors.error,
            )
        }
    }
}

@AppPreview
@Composable
private fun MemberSessionWidgetPreview() {
    AppThemeComposable {
        Column {
            MemberSessionWidget(sessionStartTime = DateTime.now().minusHours(2))
            MemberSessionWidget(sessionStartTime = null)
        }
    }
}
