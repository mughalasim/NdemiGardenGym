package cv.data.repository

import android.app.Application
import androidx.core.os.ConfigurationCompat
import cv.data.R
import cv.domain.enums.DateFormatType
import cv.domain.repositories.DateProviderRepository
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.Minutes
import org.joda.time.Months
import org.joda.time.Seconds
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Suppress("detekt.TooManyFunctions")
class DateProviderRepositoryImp(
    private val context: Application,
) : DateProviderRepository {
    private fun dateTime() = DateTime.now()

    private val appLocale =
        ConfigurationCompat.getLocales(context.resources.configuration).get(0)
            ?: Locale.ENGLISH

    private val formatDayMonthYear: DateTimeFormatter =
        DateTimeFormat.forPattern("dd MMM yyyy").withLocale(appLocale)

    private val formatMonthYear: DateTimeFormatter =
        DateTimeFormat.forPattern("MMM yyyy").withLocale(appLocale)

    private val formatDateDay: DateTimeFormatter =
        DateTimeFormat.forPattern("d EEEE").withLocale(appLocale)

    private val formatTime: DateTimeFormatter =
        DateTimeFormat.shortTime().withLocale(appLocale)

    private val formatMonth: DateTimeFormatter =
        DateTimeFormat.forPattern("MMMM").withLocale(appLocale)

    override fun getDate(): Date = dateTime().toDate()

    override fun getDate(dateMillis: Long): Date = DateTime(dateMillis).toDate()

    override fun getYear() = dateTime().year

    override fun getYear(date: Date) = DateTime(date).year

    override fun getYear(dateMillis: Long) = DateTime(dateMillis).year

    override fun getMonth(date: Date) = DateTime(date).monthOfYear

    override fun getMonth(dateMillis: Long) = DateTime(dateMillis).monthOfYear

    override fun getMonthName(month: Int): String = dateTime().withMonthOfYear(month).toString(formatMonth)

    override fun isAfterNow() = dateTime().isAfterNow

    override fun isAfterNow(dateMillis: Long) = DateTime(dateMillis).isAfterNow

    override fun isWithinCurrentMonth(
        startTime: Long,
        currentMonth: Int,
    ) = DateTime(startTime).monthOfYear == currentMonth

    override fun minutesBetween(
        startDateMillis: Long,
        endDateMillis: Long,
    ): Int =
        Minutes
            .minutesBetween(
                DateTime(startDateMillis),
                DateTime(endDateMillis),
            ).minutes

    override fun isWithinCurrentDay(
        startDate: Date,
        endDate: Date,
    ): Boolean {
        val startDateTime = DateTime(startDate)
        val endDateTime = DateTime(endDate)

        return endDateTime.isBefore(startDateTime) ||
            Days.daysBetween(startDateTime, endDateTime).days > 1 ||
            Minutes.minutesBetween(startDateTime, endDateTime).minutes < 1
    }

    override fun toCountdownTimer(startDateMillis: Long): String {
        val startInstant = DateTime(startDateMillis).toInstant()
        val currentInstant = dateTime().toInstant()
        val hours =
            Hours
                .hoursBetween(
                    startInstant,
                    currentInstant,
                ).hours
        val minutes =
            Minutes
                .minutesBetween(
                    startInstant,
                    currentInstant,
                ).minutes % MINUTES_IN_HOUR
        val seconds =
            Seconds
                .secondsBetween(
                    startInstant,
                    currentInstant,
                ).seconds % SECONDS_IN_MINUTE

        return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    }

    override fun getEndDate(
        startDate: Long,
        monthDuration: Int,
    ): Long {
        val startDate = DateTime(startDate).withTime(0, 0, 0, 0)
        return startDate.plusMonths(monthDuration).millis
    }

    override fun activeStatusDuration(
        startDateMillis: Long,
        endDateMillis: Long,
    ): String {
        val startDate = DateTime(startDateMillis)
        val endDate = DateTime(endDateMillis)

        val hours =
            Hours
                .hoursBetween(
                    startDate.toInstant(),
                    endDate.toInstant(),
                ).hours
        val minutes =
            Minutes
                .minutesBetween(
                    startDate.toInstant(),
                    endDate.toInstant(),
                ).minutes % MINUTES_IN_HOUR
        val seconds =
            Seconds
                .secondsBetween(
                    startDate.toInstant(),
                    endDate.toInstant(),
                ).seconds % SECONDS_IN_HOUR

        val hoursString =
            String.format(context.resources.getQuantityString(R.plurals.plural_hours, hours), hours)
        val minutesString =
            String.format(context.resources.getQuantityString(R.plurals.plural_minutes, minutes), minutes)

        return if (seconds <= 0) {
            context.getString(R.string.txt_now)
        } else if (hours <= 0 && minutes < 1) {
            context.getString(R.string.txt_now)
        } else {
            (if (hours > 0) hoursString else "") + (if (minutes > 0) " $minutesString" else "")
        }
    }

    override fun activeStatusDuration(totalMinutes: Int): String =
        activeStatusDuration(dateTime().millis, dateTime().plusMinutes(totalMinutes).millis)

    override fun toPaymentPlanDuration(endDateMillis: Long): String {
        val endDate = DateTime(endDateMillis).toInstant()
        val currentDate = dateTime().toInstant()

        if (endDate.isBeforeNow) return ""

        val months =
            Months
                .monthsBetween(
                    currentDate,
                    endDate,
                ).months

        val totalDays =
            Days
                .daysBetween(
                    currentDate,
                    endDate,
                ).days

        val daysLeft =
            Days
                .daysBetween(
                    currentDate,
                    endDate,
                ).days % DAYS_IN_MONTH

        val hours =
            Hours
                .hoursBetween(
                    currentDate,
                    endDate,
                ).hours

        val monthsString =
            String.format(context.resources.getQuantityString(R.plurals.plural_months, months), months)
        val daysLeftString =
            String.format(context.resources.getQuantityString(R.plurals.plural_days, daysLeft), daysLeft)
        val daysTotalString =
            String.format(context.resources.getQuantityString(R.plurals.plural_days, totalDays), totalDays)

        return if (daysLeft == 0 && totalDays > 0) {
            daysTotalString
        } else if (months > 0 || hours <= HOUR_IN_DAY * 2 || daysLeft > 0) {
            (if (months > 0) monthsString else "") + (if (daysLeft > 0) " $daysLeftString" else "")
        } else {
            context.getString(R.string.txt_tomorrow)
        }
    }

    override fun format(
        dateMillis: Long,
        format: DateFormatType,
    ): String =
        when (format) {
            DateFormatType.DAY_MONTH_YEAR -> {
                DateTime(dateMillis).toString(formatDayMonthYear)
            }

            DateFormatType.MONTH_YEAR -> {
                DateTime(dateMillis).toString(formatMonthYear)
            }

            DateFormatType.DATE_DAY -> {
                DateTime(dateMillis).toString(formatDateDay)
            }

            DateFormatType.TIME -> {
                DateTime(dateMillis).toString(formatTime)
            }
        }
}

private const val DAYS_IN_MONTH = 30
private const val HOUR_IN_DAY = 12
private const val MINUTES_IN_HOUR = 60
private const val SECONDS_IN_HOUR = 3600
private const val SECONDS_IN_MINUTE = 60
