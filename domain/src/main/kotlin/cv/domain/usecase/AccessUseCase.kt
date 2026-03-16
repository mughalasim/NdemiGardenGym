package cv.domain.usecase

import cv.domain.repositories.AccessRepository
import cv.domain.repositories.JobRepository

class AccessUseCase(
    private val jobRepository: JobRepository,
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

    suspend fun verifyEmail() = accessRepository.verifyEmail()

    fun logOut() {
        jobRepository.cancelAll()
        accessRepository.logOut()
    }
}
