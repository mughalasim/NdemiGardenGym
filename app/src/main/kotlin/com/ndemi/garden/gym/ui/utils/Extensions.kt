package com.ndemi.garden.gym.ui.utils

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.core.os.ConfigurationCompat
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.utils.DateConstants.HOUR_IN_DAY
import com.ndemi.garden.gym.ui.utils.DateConstants.MINUTES_IN_HOUR
import com.ndemi.garden.gym.ui.utils.DateConstants.SECONDS_IN_HOUR
import com.ndemi.garden.gym.ui.utils.DateConstants.formatDayMonthYear
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.Minutes
import org.joda.time.Seconds
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

@Composable
fun Long?.toMembershipStatusString(): String {
    return this?.let {
        DateTime(it).toString(formatDayMonthYear)
    }?: run{ stringResource(R.string.txt_expired) }
}

@Composable
fun DateTime.toActiveStatusDuration(startDate: DateTime): String {
    val hours = Hours.hoursBetween(
        startDate.toInstant(),
        this.toInstant()
    ).hours
    val minutes = Minutes.minutesBetween(
        startDate.toInstant(),
        this.toInstant()
    ).minutes % MINUTES_IN_HOUR
    val seconds = Seconds.secondsBetween(
        startDate.toInstant(),
        this.toInstant()
    ).seconds % SECONDS_IN_HOUR

    val hoursString = String.format(pluralStringResource(R.plurals.plural_hours, hours), hours)
    val minutesString = String.format(pluralStringResource(R.plurals.plural_minutes, minutes), minutes)

    return if (seconds <= 0){
        stringResource(R.string.txt_not_active)
    } else if (hours <= 0 && minutes < 1){
        stringResource(R.string.txt_now)
    } else {
        (if (hours > 0) hoursString else "") + (if (minutes > 0) " $minutesString" else "")
    }
}

@Composable
fun DateTime.toDaysDuration(): String {
    val days = Days.daysBetween(
        DateTime.now().toInstant(),
        this.toInstant()
    ).days

    val hours = Hours.hoursBetween(
        DateTime.now().toInstant(),
        this.toInstant()
    ).hours

    val daysString = String.format(pluralStringResource(R.plurals.plural_days, days), days)

    return if (hours <= HOUR_IN_DAY){
        stringResource(R.string.txt_today)
    } else if (hours <= HOUR_IN_DAY*2){
        stringResource(R.string.txt_tomorrow)
    } else {
        daysString
    }
}

object DateConstants {
    private val appLocale =
        ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)

    val formatDayMonthYear: DateTimeFormatter =
        DateTimeFormat.forPattern("dd MMMM yyyy").withLocale(appLocale)


    val formatDateDay: DateTimeFormatter =
        DateTimeFormat.forPattern("d EEEE").withLocale(appLocale)

    val formatTime: DateTimeFormatter =
        DateTimeFormat.shortTime().withLocale(appLocale)

    val formatMonthYear: DateTimeFormatter =
        DateTimeFormat.forPattern("MMMM yyyy").withLocale(appLocale)

    const val HOUR_IN_DAY = 12
    const val MINUTES_IN_HOUR = 60
    const val SECONDS_IN_HOUR = 3600
}
