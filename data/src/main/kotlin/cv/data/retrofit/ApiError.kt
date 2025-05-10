package cv.data.retrofit

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestoreException
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.repositories.AppLogLevel
import cv.domain.repositories.AppLoggerRepository

enum class ApiError {
    UNKNOWN,
    SERVER,
    NETWORK,
    UNAUTHORISED,
}

fun ApiError.toDomainError(): DomainError =
    when (this) {
        ApiError.UNKNOWN -> DomainError.UNKNOWN
        ApiError.NETWORK -> DomainError.NETWORK
        ApiError.UNAUTHORISED -> DomainError.UNAUTHORISED
        else -> DomainError.SERVER
    }

// TODO - Remove complettableDefferred from all repositories
fun <T, D>handleError(task: Task<T>, logger: AppLoggerRepository): DomainResult<D> =
    task.exception?.let {
        logger.log("Exception: $it", AppLogLevel.ERROR)
        DomainResult.Error(it.toDomainError())
    }?: DomainResult.Error(DomainError.UNKNOWN)

@Suppress("detekt.CyclomaticComplexMethod")
fun Exception.toDomainError(): DomainError {
    return when (this) {
        is FirebaseAuthWeakPasswordException -> DomainError.INVALID_PASSWORD_LENGTH
        is FirebaseAuthInvalidCredentialsException -> DomainError.INVALID_LOGIN_CREDENTIALS
        is FirebaseAuthEmailException -> DomainError.INVALID_LOGIN_CREDENTIALS
        is FirebaseAuthInvalidUserException -> DomainError.USER_DISABLED
        is FirebaseAuthException -> DomainError.UNKNOWN
        is FirebaseFirestoreException -> {
            return when (this.code) {
                FirebaseFirestoreException.Code.UNKNOWN -> DomainError.NETWORK
                FirebaseFirestoreException.Code.INVALID_ARGUMENT -> DomainError.INVALID_ARGUMENT
                FirebaseFirestoreException.Code.NOT_FOUND -> DomainError.NO_DATA
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> DomainError.UNAUTHORISED
                FirebaseFirestoreException.Code.UNAUTHENTICATED -> DomainError.UNAUTHORISED
                else -> DomainError.SERVER
            }
        }
        is FirebaseNetworkException -> DomainError.NETWORK
        else -> DomainError.UNKNOWN
    }
}
