package cv.data.repository

import cv.data.retrofit.toDomainError
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.repositories.AppLogLevel
import cv.domain.repositories.AppLoggerRepository

fun <T> handleError(
    throwable: Throwable,
    logger: AppLoggerRepository,
): DomainResult<T> {
    logger.log("Exception: $throwable", AppLogLevel.ERROR)
    return if (throwable is Exception) {
        DomainResult.Error(throwable.toDomainError())
    } else {
        DomainResult.Error(DomainError.UNKNOWN)
    }
}
