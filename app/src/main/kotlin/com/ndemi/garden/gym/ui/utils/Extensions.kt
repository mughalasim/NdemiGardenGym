package com.ndemi.garden.gym.ui.utils

import android.content.res.Resources
import android.telephony.PhoneNumberUtils
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.core.os.ConfigurationCompat
import androidx.core.text.isDigitsOnly
import com.ndemi.garden.gym.BuildConfig
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.utils.DateConstants.DAYS_IN_MONTH
import com.ndemi.garden.gym.ui.utils.DateConstants.HOUR_IN_DAY
import com.ndemi.garden.gym.ui.utils.DateConstants.MINUTES_IN_HOUR
import com.ndemi.garden.gym.ui.utils.DateConstants.PHONE_NUMBER_DIGITS
import com.ndemi.garden.gym.ui.utils.DateConstants.SECONDS_IN_HOUR
import com.ndemi.garden.gym.ui.utils.DateConstants.appLocale
import com.ndemi.garden.gym.ui.utils.DateConstants.formatDayMonthYear
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.Minutes
import org.joda.time.Months
import org.joda.time.Seconds
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.text.DecimalFormat
import java.util.Locale

@Composable
fun Long?.toMembershipStatusString(): String {
    return this?.let {
        val date = DateTime(it)
        date.toString(formatDayMonthYear) + " (${date.toDaysDuration()})"
    } ?: run { stringResource(R.string.txt_expired) }
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
    val minutesString =
        String.format(pluralStringResource(R.plurals.plural_minutes, minutes), minutes)

    return if (seconds <= 0) {
        stringResource(R.string.txt_not_active)
    } else if (hours <= 0 && minutes < 1) {
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
    if (this.isBeforeNow) return stringResource(id = R.string.txt_expired)

    val months = Months.monthsBetween(
        DateTime.now().toInstant(),
        this.toInstant()
    ).months

    val totalDays = Days.daysBetween(
        DateTime.now().toInstant(),
        this.toInstant()
    ).days

    val daysLeft = Days.daysBetween(
        DateTime.now().toInstant(),
        this.toInstant()
    ).days % DAYS_IN_MONTH

    val hours = Hours.hoursBetween(
        DateTime.now().toInstant(),
        this.toInstant()
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

fun Double.toAmountString(): String =
    DecimalFormat("${BuildConfig.CURRENCY_CODE} #,###").format(this)

fun String.toPhoneNumberString(): String =
    if (this.isEmpty()) {
        ""
    } else {
        PhoneNumberUtils.formatNumber(this, appLocale.country)
    }

fun String.isValidApartmentNumber(): Boolean =
    this.matches(Regex("^[A-Da-d](?:[1-9][0-4][0-4][0-4]?|1404)\$"))

fun String.isValidPhoneNumber(): Boolean =
    this.length == PHONE_NUMBER_DIGITS && this.isDigitsOnly() && this.first() == '0'

object DateConstants {
    val appLocale =
        ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)
            ?: Locale(Locale.ENGLISH.language)

    val formatDayMonthYear: DateTimeFormatter =
        DateTimeFormat.forPattern("dd MMMM yyyy").withLocale(appLocale)


    val formatDateDay: DateTimeFormatter =
        DateTimeFormat.forPattern("d EEEE").withLocale(appLocale)

    val formatTime: DateTimeFormatter =
        DateTimeFormat.shortTime().withLocale(appLocale)

    val formatMonthYear: DateTimeFormatter =
        DateTimeFormat.forPattern("MMMM yyyy").withLocale(appLocale)

    val formatYear: DateTimeFormatter =
        DateTimeFormat.forPattern("yyyy").withLocale(appLocale)

    const val DAYS_IN_MONTH = 30
    const val HOUR_IN_DAY = 12
    const val MINUTES_IN_HOUR = 60
    const val SECONDS_IN_HOUR = 3600
    const val PHONE_NUMBER_DIGITS = 10
}
