package cv.data.repository

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import cv.domain.enums.AppLogType
import cv.domain.repositories.AnalyticsRepository
import cv.domain.repositories.AppLoggerRepository

class AnalyticsRepositoryImp(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val logger: AppLoggerRepository,
) : AnalyticsRepository {
    override fun logEvent(
        eventName: String,
        params: List<Pair<String, String>>,
    ) {
        val bundle = Bundle()
        for (param in params) {
            bundle.putString(param.first, param.second)
        }
        firebaseAnalytics.logEvent(eventName, bundle)
        logger.log("Event: $eventName Params: $bundle", AppLogType.ANALYTICS)
    }
}
