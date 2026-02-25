package cv.domain.repositories

import java.util.Date

interface DateProviderRepository {
    fun getDate(): Date

    fun getYear(): Int

    fun getYear(date: Date): Int

    fun getMonth(): Int

    fun getMonth(date: Date): Int

    fun isWithinCurrentMonth(
        startTime: Long,
        endTime: Long,
        currentDate: Date,
    ): Boolean
}
