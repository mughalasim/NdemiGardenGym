package cv.domain.usecase

import cv.domain.dispatchers.ScopeProvider
import cv.domain.repositories.AuthRepository
import kotlinx.coroutines.withContext

class AuthUseCase(
    private val scope: ScopeProvider,
    private val authRepository: AuthRepository,
) {
    suspend fun getLoggedInUser() = withContext(scope.ioDispatcher()) { authRepository.getLoggedInUser() }

    suspend fun getAuthState() = withContext(scope.ioDispatcher()) { authRepository.getAuthState() }

    suspend fun getAppVersion() = withContext(scope.ioDispatcher()) { authRepository.getAppVersion() }

    fun observeUser() = authRepository.observeUser()
}
