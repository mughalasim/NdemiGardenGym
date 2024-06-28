package com.ndemi.garden.gym.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.utils.DateConstants.HOUR_IN_DAY
import com.ndemi.garden.gym.ui.utils.DateConstants.MINUTES_IN_HOUR
import com.ndemi.garden.gym.ui.utils.DateConstants.SECONDS_IN_HOUR
import com.ndemi.garden.gym.ui.utils.DateConstants.formatDayMonthYear
import dev.b3nedikt.restring.Restring
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
    }?: run{ "Expired" }
}

@Composable
fun DateTime.toActiveStatusDuration(startDate: DateTime): String {
    val context = LocalContext.current.resources

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

    val hoursString = String.format(context.getQuantityString(R.plurals.plural_hours, hours), hours)
    val minutesString = String.format(context.getQuantityString(R.plurals.plural_minutes, minutes), minutes)

    return if (seconds <= 0){
        context.getString(R.string.txt_not_active)
    } else if (hours <= 0 && minutes < 1){
        context.getString(R.string.txt_now)
    } else {
        (if (hours > 0) hoursString else "") + (if (minutes > 0) " $minutesString" else "")
    }
}

@Composable
fun DateTime.toDaysDuration(): String {
    val context = LocalContext.current.resources

    val days = Days.daysBetween(
        DateTime.now().toInstant(),
        this.toInstant()
    ).days

    val hours = Hours.hoursBetween(
        DateTime.now().toInstant(),
        this.toInstant()
    ).hours

    val daysString = String.format(context.getQuantityString(R.plurals.plural_days, days), days)

    return if (hours <= HOUR_IN_DAY){
        context.getString(R.string.txt_today)
    } else if (hours <= HOUR_IN_DAY*2){
        context.getString(R.string.txt_tomorrow)
    } else {
        daysString
    }
}

object DateConstants {
    val formatDayMonthYear: DateTimeFormatter =
        DateTimeFormat.forPattern("dd MMMM yyyy").withLocale(Restring.locale)


    val formatDateDay: DateTimeFormatter =
        DateTimeFormat.forPattern("d EEEE").withLocale(Restring.locale)

    val formatTime: DateTimeFormatter =
        DateTimeFormat.shortTime().withLocale(Restring.locale)

    val formatMonthYear: DateTimeFormatter =
        DateTimeFormat.forPattern("MMMM yyyy").withLocale(Restring.locale)

    const val HOUR_IN_DAY = 12
    const val MINUTES_IN_HOUR = 60
    const val SECONDS_IN_HOUR = 3600
}
