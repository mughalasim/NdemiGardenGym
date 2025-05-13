package cv.data

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestoreException
import cv.domain.DomainResult
import cv.domain.enums.AppLogType
import cv.domain.enums.DomainErrorType
import cv.domain.repositories.AppLoggerRepository

internal fun <T> handleError(
    throwable: Throwable,
    logger: AppLoggerRepository,
): DomainResult<T> {
    logger.log("Exception: $throwable", AppLogType.ERROR)
    return if (throwable is Exception) {
        DomainResult.Error(throwable.toDomainError())
    } else {
        DomainResult.Error(DomainErrorType.UNKNOWN)
    }
}

@Suppress("detekt.CyclomaticComplexMethod")
fun Exception.toDomainError(): DomainErrorType {
    return when (this) {
        is FirebaseAuthWeakPasswordException -> DomainErrorType.INVALID_PASSWORD_LENGTH
        is FirebaseAuthInvalidCredentialsException -> DomainErrorType.INVALID_LOGIN_CREDENTIALS
        is FirebaseAuthEmailException -> DomainErrorType.INVALID_LOGIN_CREDENTIALS
        is FirebaseAuthInvalidUserException -> DomainErrorType.USER_DISABLED
        is FirebaseAuthUserCollisionException -> DomainErrorType.EMAIL_ALREADY_EXISTS
        is FirebaseAuthException -> DomainErrorType.UNKNOWN
        is FirebaseFirestoreException -> {
            return when (this.code) {
                FirebaseFirestoreException.Code.UNKNOWN -> DomainErrorType.NETWORK
                FirebaseFirestoreException.Code.INVALID_ARGUMENT -> DomainErrorType.INVALID_ARGUMENT
                FirebaseFirestoreException.Code.NOT_FOUND -> DomainErrorType.NO_DATA
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> DomainErrorType.UNAUTHORISED
                FirebaseFirestoreException.Code.UNAUTHENTICATED -> DomainErrorType.UNAUTHORISED
                else -> DomainErrorType.SERVER
            }
        }
        is FirebaseNetworkException -> DomainErrorType.NETWORK
        else -> DomainErrorType.UNKNOWN
    }
}
