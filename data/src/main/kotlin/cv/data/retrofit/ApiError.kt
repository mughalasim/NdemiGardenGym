package cv.data.retrofit

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestoreException
import cv.domain.DomainError

enum class ApiError {
    UNKNOWN,
    SERVER,
    NETWORK,
    UNAUTHORISED,
}

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
