package com.ndemi.garden.gym.ui.utils

import com.ndemi.garden.gym.BuildConfig
import com.ndemi.garden.gym.ui.utils.DateConstants.MINUTES_IN_HOUR
import com.ndemi.garden.gym.ui.utils.DateConstants.SECONDS_IN_MINUTE
import com.ndemi.garden.gym.ui.utils.DateConstants.formatMonth
import org.joda.time.DateTime
import org.joda.time.Hours
import org.joda.time.Minutes
import org.joda.time.Seconds
import java.text.DecimalFormat

fun Int.toMonthName(): String = DateTime().withMonthOfYear(this).toString(formatMonth)

fun Double.toAmountString(): String = DecimalFormat("${BuildConfig.CURRENCY_CODE} #,###").format(this)

fun DateTime.toCountdownTimer(startDate: DateTime): String {
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
        ).seconds % SECONDS_IN_MINUTE

    return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}
