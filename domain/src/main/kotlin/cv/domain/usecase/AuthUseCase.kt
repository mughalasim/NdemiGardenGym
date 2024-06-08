package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.repositories.AuthRepository

class AuthUseCase(
    private val authRepository: AuthRepository,
) {
    fun register(email: String, password: String, callback: (DomainResult<String>) -> Unit) =
        authRepository.register(email, password, callback)

    fun login(email: String, password: String, callback: (DomainResult<Unit>) -> Unit) =
        authRepository.login(email, password, callback)

    fun resetPasswordForEmail(email: String, callback: (DomainResult<Unit>) -> Unit) =
        authRepository.resetPasswordForEmail(email, callback)

    fun isAuthenticated() = authRepository.isAuthenticated()

    fun isAdmin() = authRepository.isAdmin()

    fun logOut() = authRepository.logOut()
}
