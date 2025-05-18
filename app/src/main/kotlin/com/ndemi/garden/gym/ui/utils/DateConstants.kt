package com.ndemi.garden.gym.ui.utils

import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.Locale

object DateConstants {
    private val appLocale =
        ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)
            ?: Locale(Locale.ENGLISH.language)

    val formatDayMonthYear: DateTimeFormatter =
        DateTimeFormat.forPattern("dd MMM yyyy").withLocale(appLocale)

    val formatMonthYear: DateTimeFormatter =
        DateTimeFormat.forPattern("MMM yyyy").withLocale(appLocale)

    val formatDateDay: DateTimeFormatter =
        DateTimeFormat.forPattern("d EEEE").withLocale(appLocale)

    val formatTime: DateTimeFormatter =
        DateTimeFormat.shortTime().withLocale(appLocale)

    val formatMonth: DateTimeFormatter =
        DateTimeFormat.forPattern("MMMM").withLocale(appLocale)

    const val DAYS_IN_MONTH = 30
    const val HOUR_IN_DAY = 12
    const val MINUTES_IN_HOUR = 60
    const val SECONDS_IN_HOUR = 3600
    const val SECONDS_IN_MINUTE = 60
}
