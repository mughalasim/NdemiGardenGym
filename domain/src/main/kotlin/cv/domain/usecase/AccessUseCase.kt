package cv.domain.usecase

import cv.domain.repositories.AccessRepository
import kotlinx.coroutines.Job

class AccessUseCase(
    private val job: MutableList<Job>,
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
        job.forEach {
            it.cancel()
        }
        job.clear()
        accessRepository.logOut()
    }
}
