package cv.domain.usecase

import cv.domain.repositories.AccessRepository

class AccessUseCase(
    private val accessRepository: AccessRepository,
) {
    suspend fun login(
        email: String,
        password: String,
    ) = accessRepository.login(email, password)

    suspend fun register(
        email: String,
        password: String,
    ) = accessRepository.register(email, password)

    suspend fun resetPasswordForEmail(email: String) = accessRepository.resetPasswordForEmail(email)

    fun logOut() = accessRepository.logOut()
}
