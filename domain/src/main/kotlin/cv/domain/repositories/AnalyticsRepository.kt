package cv.domain.repositories

interface AnalyticsRepository {
    fun logEvent(
        eventName: String,
        paramName: String,
        value: String,
    )
}
