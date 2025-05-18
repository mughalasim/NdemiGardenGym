package com.ndemi.garden.gym.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.DateConstants.DAYS_IN_MONTH
import com.ndemi.garden.gym.ui.utils.DateConstants.HOUR_IN_DAY
import com.ndemi.garden.gym.ui.utils.DateConstants.MINUTES_IN_HOUR
import com.ndemi.garden.gym.ui.utils.DateConstants.SECONDS_IN_HOUR
import com.ndemi.garden.gym.ui.utils.DateConstants.formatDayMonthYear
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import kotlinx.coroutines.flow.StateFlow
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.Minutes
import org.joda.time.Months
import org.joda.time.Seconds

@Composable
fun Long?.toMembershipStatusString(): String {
    return this?.let {
        val date = DateTime(it)
        date.toString(formatDayMonthYear) + " (${date.toDaysDuration()})"
    } ?: run { stringResource(R.string.txt_expired) }
}

@Composable
fun DateTime.toActiveStatusDuration(startDate: DateTime): String {
    val hours =
        Hours.hoursBetween(
            startDate.toInstant(),
            this.toInstant(),
        ).hours
    val minutes =
        Minutes.minutesBetween(
            startDate.toInstant(),
            this.toInstant(),
        ).minutes % MINUTES_IN_HOUR
    val seconds =
        Seconds.secondsBetween(
            startDate.toInstant(),
            this.toInstant(),
        ).seconds % SECONDS_IN_HOUR

    val hoursString = String.format(pluralStringResource(R.plurals.plural_hours, hours), hours)
    val minutesString =
        String.format(pluralStringResource(R.plurals.plural_minutes, minutes), minutes)

    return if (seconds <= 0) {
        stringResource(R.string.txt_now)
    } else if (hours <= 0 && minutes < 1) {
        stringResource(R.string.txt_now)
    } else {
        (if (hours > 0) hoursString else "") + (if (minutes > 0) " $minutesString" else "")
    }
}

@Composable
fun DateTime.toDaysDuration(): String {
    val days =
        Days.daysBetween(
            DateTime.now().toInstant(),
            this.toInstant(),
        ).days

    val hours =
        Hours.hoursBetween(
            DateTime.now().toInstant(),
            this.toInstant(),
        ).hours

    val daysString = String.format(pluralStringResource(R.plurals.plural_days, days), days)

    return if (hours <= HOUR_IN_DAY) {
        stringResource(R.string.txt_today)
    } else if (hours <= HOUR_IN_DAY * 2) {
        stringResource(R.string.txt_tomorrow)
    } else {
        daysString
    }
}

@Composable
fun DateTime.toPaymentPlanDuration(): String {
    if (this.isBeforeNow) return ""

    val months =
        Months.monthsBetween(
            DateTime.now().toInstant(),
            this.toInstant(),
        ).months

    val totalDays =
        Days.daysBetween(
            DateTime.now().toInstant(),
            this.toInstant(),
        ).days

    val daysLeft =
        Days.daysBetween(
            DateTime.now().toInstant(),
            this.toInstant(),
        ).days % DAYS_IN_MONTH

    val hours =
        Hours.hoursBetween(
            DateTime.now().toInstant(),
            this.toInstant(),
        ).hours

    val monthsString =
        String.format(pluralStringResource(R.plurals.plural_months, months), months)
    val daysLeftString =
        String.format(pluralStringResource(R.plurals.plural_days, daysLeft), daysLeft)
    val daysTotalString =
        String.format(pluralStringResource(R.plurals.plural_days, totalDays), totalDays)

    return if (daysLeft == 0 && totalDays > 0) {
        daysTotalString
    } else if (months > 0 || hours <= HOUR_IN_DAY * 2 || daysLeft > 0) {
        (if (months > 0) monthsString else "") + (if (daysLeft > 0) " $daysLeftString" else "")
    } else {
        stringResource(R.string.txt_tomorrow)
    }
}

@Composable
fun Modifier.toAppCardStyle(overridePadding: Dp = padding_screen_small) =
    this.fillMaxWidth()
        .wrapContentHeight()
        .background(
            color = AppTheme.colors.backgroundCard,
            shape = RoundedCornerShape(border_radius),
        )
        .padding(overridePadding)

@Composable
@Suppress("Detekt:MagicNumber")
fun getBMIColor(bmi: Double) =
    when {
        bmi > 40 -> AppTheme.colors.error
        bmi > 35 -> Color.Red
        bmi < 18.5 || bmi > 25.0 -> Color.Magenta
        else -> AppTheme.colors.success
    }

@Composable
fun StateFlow<BaseViewModel.SnackbarState>.ObserveAppSnackbar(snackbarHostState: AppSnackbarHostState) {
    val snackbarState by this.collectAsStateWithLifecycle()
    when (snackbarState) {
        is BaseViewModel.SnackbarState.Visible ->
            snackbarHostState.Show(
                type = (snackbarState as BaseViewModel.SnackbarState.Visible).snackbarType,
                message = (snackbarState as BaseViewModel.SnackbarState.Visible).message,
            )
        else -> Unit
    }
}
