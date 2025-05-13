package cv.data.repository

import com.google.firebase.auth.FirebaseAuth
import cv.data.handleError
import cv.domain.DomainResult
import cv.domain.enums.DomainErrorType
import cv.domain.repositories.AccessRepository
import cv.domain.repositories.AppLoggerRepository
import kotlinx.coroutines.tasks.await

class AccessRepositoryImp(
    private val firebaseAuth: FirebaseAuth,
    private val logger: AppLoggerRepository,
) : AccessRepository {
    override suspend fun login(
        email: String,
        password: String,
    ): DomainResult<Unit> =
        runCatching {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        }.fold(
            onSuccess = {
                return it.user?.let {
                    DomainResult.Success(Unit)
                } ?: run {
                    DomainResult.Error(DomainErrorType.NO_DATA)
                }
            },
            onFailure = { handleError(it, logger) },
        )

    override suspend fun register(
        email: String,
        password: String,
    ): DomainResult<String> =
        runCatching {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        }.fold(
            onSuccess = { result ->
                return result.user?.let {
                    DomainResult.Success(it.uid)
                } ?: run {
                    DomainResult.Error(DomainErrorType.NO_DATA)
                }
            },
            onFailure = { handleError(it, logger) },
        )

    override suspend fun resetPasswordForEmail(email: String): DomainResult<Unit> =
        runCatching {
            firebaseAuth.sendPasswordResetEmail(email).await()
        }.fold(
            onSuccess = { DomainResult.Success(Unit) },
            onFailure = { handleError(it, logger) },
        )

    override fun logOut() = firebaseAuth.signOut()
}
