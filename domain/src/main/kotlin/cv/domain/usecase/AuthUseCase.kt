package cv.domain.usecase

import cv.domain.entities.MemberEntity
import cv.domain.repositories.AuthRepository

class AuthUseCase(
    private val authRepository: AuthRepository,
) {
    fun isAuthenticated() =
        authRepository.isAuthenticated()

    fun isAdmin() =
        authRepository.isAdmin()

    fun logOut() = authRepository.logOut()

    fun getMember(callback: (String) -> Unit) =
        authRepository.getMember(callback)

    fun register(memberEntity: MemberEntity, password: String, callback: (String) -> Unit) =
        authRepository.register(memberEntity, password, callback)

    fun login(email: String, password: String, callback: (String) -> Unit) =
        authRepository.login(email, password, callback)
}
