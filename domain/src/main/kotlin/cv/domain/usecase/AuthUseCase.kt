package cv.domain.usecase

import cv.domain.repositories.AuthRepository

class AuthUseCase(
    private val authRepository: AuthRepository,
) {
    suspend fun getLoggedInUser() = authRepository.getLoggedInUser()

    suspend fun getAuthState() = authRepository.getAuthState()

    suspend fun getAppVersion() = authRepository.getAppVersion()

    fun observeUser() = authRepository.observeUser()
}
