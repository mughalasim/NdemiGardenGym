package cv.domain.usecase

import cv.domain.repositories.AuthRepository

class AuthUseCase(
    private val authRepository: AuthRepository,
) {
    fun isAuthenticated() =
        authRepository.isAuthenticated()

    fun isAdmin() =
        authRepository.isAdmin()

    fun logOut() = authRepository.logOut()

    fun register(email: String, password: String, callback: (String) -> Unit) =
        authRepository.register(email, password, callback)

    fun login(email: String, password: String, callback: (String) -> Unit) =
        authRepository.login(email, password, callback)
}
