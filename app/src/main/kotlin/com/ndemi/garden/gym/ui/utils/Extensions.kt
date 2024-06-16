package com.ndemi.garden.gym.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.Route
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
import java.util.Date

fun String.isValidApartmentNumber(): Boolean =
    this.matches(Regex("^[A-Da-d](?:[1-9][0-4][0-4][0-4]?|1404)\$"))

fun String.toRoute(): Route {
    return when {
        this.contains(Route.ResetPasswordScreen.javaClass.simpleName) -> Route.ResetPasswordScreen
        this.contains(Route.RegisterScreen.javaClass.simpleName)-> Route.RegisterScreen
        this.contains(Route.RegisterNewScreen.javaClass.simpleName) -> Route.RegisterNewScreen
        this.contains(Route.ProfileScreen.javaClass.simpleName) -> Route.ProfileScreen
        this.contains(Route.AttendanceScreen.javaClass.simpleName) -> Route.AttendanceScreen
        this.contains(Route.LiveAttendanceScreen.javaClass.simpleName) -> Route.LiveAttendanceScreen
        this.contains(Route.MembersScreen.javaClass.simpleName) -> Route.MembersScreen
        this.contains("MembersAttendancesScreen") -> Route.MembersAttendancesScreen("", "")
        this.contains("MemberEditScreen") -> Route.MemberEditScreen("")
        else  -> Route.LoginScreen
    }
}

@Composable
fun Date?.toMembershipStatusString(): String {
    return this?.let {
        DateTime(it).toString(formatDayMonthYear)
    }?: run{ "Expired" }
}

@Composable
fun DateTime.toHoursMinutesSecondsDuration(startDate: DateTime): String {
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
    val nowString = context.getString(R.string.txt_now)
    val notActiveString = context.getString(R.string.txt_not_active)

    return (if (hours > 0) hoursString else "") +
            (if (hours > 0 && minutes > 0) " " else "") +
            (if (minutes > 0) minutesString else "") +
            (if (seconds > 0 && minutes < 1) nowString else notActiveString)

}

@Composable
fun DateTime.toHoursMinutesDuration(startDate: DateTime): String {
    val context = LocalContext.current.resources

    val hours = Hours.hoursBetween(
        startDate.toInstant(),
        this.toInstant()
    ).hours
    val minutes = Minutes.minutesBetween(
        startDate.toInstant(),
        this.toInstant()
    ).minutes % MINUTES_IN_HOUR

    val hoursString = String.format(context.getQuantityString(R.plurals.plural_hours, hours), hours)
    val minutesString = String.format(context.getQuantityString(R.plurals.plural_minutes, minutes), minutes)
    val nowString = context.getString(R.string.txt_now)

    return (if (hours > 0) hoursString else "") +
            (if (hours > 0 && minutes > 0) " " else "") +
            (if (minutes > 0) minutesString else "") +
            (if (hours < 1 && minutes < 1) nowString else "")

}

@Composable
fun DateTime.toDaysDuration(): String {
    val context = LocalContext.current.resources

    val days = Days.daysBetween(
        DateTime.now().toInstant(),
        this.toInstant()
    ).days

    val daysString = String.format(context.getQuantityString(R.plurals.plural_days, days), days)

    return (if (days > 0) daysString else "") +
            (if (days == 1) context.getString(R.string.txt_today) else "") +
            (if (days == 0) context.getString(R.string.txt_tomorrow) else "")

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

    const val MINUTES_IN_HOUR = 60
    const val SECONDS_IN_HOUR = 3600
}
