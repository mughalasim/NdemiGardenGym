package cv.domain.repositories

import java.util.Date

interface DateProviderRepository {
    fun getDate(): Date

    fun getYear(): Int

    fun getMonth(): Int

    fun isWithinCurrentMonth(
        startTime: Long,
        endTime: Long,
    ): Boolean
}
