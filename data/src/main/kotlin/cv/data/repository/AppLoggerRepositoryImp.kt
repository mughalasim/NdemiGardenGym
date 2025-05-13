package cv.data.repository

import android.util.Log
import cv.domain.enums.AppLogType
import cv.domain.repositories.AppLoggerRepository

class AppLoggerRepositoryImp(val isEnabled: Boolean) : AppLoggerRepository {
    override fun log(
        message: String,
        appLogType: AppLogType,
    ) {
        if (isEnabled) {
            when (appLogType) {
                AppLogType.ANALYTICS -> {
                    Log.i(AppLogType.ANALYTICS.name, message)
                }
                AppLogType.DEBUG -> {
                    Log.d(javaClass.simpleName, message)
                }
                AppLogType.ERROR -> {
                    Log.e(javaClass.simpleName, message)
                }
            }
        }
    }
}
