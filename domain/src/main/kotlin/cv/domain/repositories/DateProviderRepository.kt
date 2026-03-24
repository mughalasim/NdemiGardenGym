package cv.domain.repositories

import cv.domain.enums.DateFormatType
import java.util.Date

@Suppress("detekt.TooManyFunctions")
interface DateProviderRepository {
    fun getDate(): Date

    fun getDate(dateMillis: Long): Date

    fun getYear(): Int

    fun getYear(date: Date): Int

    fun getYear(dateMillis: Long): Int

    fun getMonth(date: Date): Int

    fun getMonth(dateMillis: Long): Int

    fun getMonthName(month: Int): String

    fun isAfterNow(): Boolean

    fun isAfterNow(dateMillis: Long): Boolean

    fun isWithinCurrentMonth(
        startTime: Long,
        currentMonth: Int,
    ): Boolean

    fun minutesBetween(
        startDateMillis: Long,
        endDateMillis: Long,
    ): Int

    fun isWithinCurrentDay(
        startDate: Date,
        endDate: Date,
    ): Boolean

    fun toCountdownTimer(startDateMillis: Long): String

    fun getEndDate(
        startDate: Long,
        monthDuration: Int,
    ): Long

    fun activeStatusDuration(
        startDateMillis: Long,
        endDateMillis: Long,
    ): String

    fun activeStatusDuration(totalMinutes: Int): String

    fun toPaymentPlanDuration(endDateMillis: Long): String

    fun format(
        dateMillis: Long,
        format: DateFormatType,
    ): String

    fun getDayOfYear(dateMillis: Long): Int
}
