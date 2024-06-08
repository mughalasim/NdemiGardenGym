package cv.domain.repositories

import cv.domain.DomainResult

interface AuthRepository {
    fun isAuthenticated(): Boolean

    fun isAdmin(): Boolean

    fun logOut()

    fun register(email: String, password: String, callback: (DomainResult<String>) -> Unit)

    fun login(email: String, password: String, callback: (DomainResult<Unit>) -> Unit)

    fun resetPasswordForEmail(email: String, callback: (DomainResult<Unit>) -> Unit)
}
