package cv.domain.repositories

import cv.domain.DomainResult

interface AccessRepository {
    suspend fun login(
        email: String,
        password: String,
    ): DomainResult<Unit>

    suspend fun register(
        email: String,
        password: String,
    ): DomainResult<String>

    suspend fun resetPasswordForEmail(email: String): DomainResult<Unit>

    fun logOut()
}
