package cv.domain.repositories

import cv.domain.enums.AppLogType

interface AppLoggerRepository {
    fun log(
        message: String,
        appLogType: AppLogType = AppLogType.DEBUG,
    )
}
