package cv.data.repository

import cv.domain.repositories.DateProviderRepository
import org.joda.time.DateTime
import java.util.Date

class DateProviderRepositoryImp : DateProviderRepository {
    private val dateTime = DateTime.now()

    override fun getDate(): Date = dateTime.toDate()

    override fun getYear() = dateTime.year

    override fun getMonth() = dateTime.monthOfYear

    override fun isWithinCurrentMonth(
        startTime: Long,
        endTime: Long,
    ): Boolean {
        val startDateMonth = DateTime(startTime).monthOfYear
        val endDateMonth = DateTime(endTime).monthOfYear

        return when {
            startDateMonth > endDateMonth ||
                endDateMonth - startDateMonth > 1 ||
                startDateMonth != dateTime.monthOfYear -> false

            else -> true
        }
    }
}
