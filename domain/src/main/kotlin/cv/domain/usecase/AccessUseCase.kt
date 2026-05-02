package cv.domain.usecase

import cv.domain.dispatchers.ScopeProvider
import cv.domain.repositories.AccessRepository
import cv.domain.repositories.JobRepository
import kotlinx.coroutines.withContext

class AccessUseCase(
    private val scope: ScopeProvider,
    private val jobRepository: JobRepository,
    private val accessRepository: AccessRepository,
) {
    suspend fun login(
        email: String,
        password: String,
    ) = withContext(scope.ioDispatcher()) {
        accessRepository.login(email, password)
    }

    suspend fun register(
        email: String,
        password: String,
    ) = withContext(scope.ioDispatcher()) {
        accessRepository.register(email, password)
    }

    suspend fun resetPasswordForEmail(email: String) =
        withContext(scope.ioDispatcher()) {
            accessRepository.resetPasswordForEmail(email)
        }

    suspend fun verifyEmail() =
        withContext(scope.ioDispatcher()) {
            accessRepository.verifyEmail()
        }

    fun logOut() {
        jobRepository.cancelAll()
        accessRepository.logOut()
    }
}
