package cv.domain.repositories

import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.entities.MemberType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    fun isAuthenticated(): Boolean

    fun getMemberType(): MemberType

    fun logOut()

    fun register(
        email: String,
        password: String,
        callback: (DomainResult<String>) -> Unit,
    )

    fun login(
        email: String,
        password: String,
        callback: (DomainResult<Unit>) -> Unit,
    )

    fun resetPasswordForEmail(
        email: String,
        callback: (DomainResult<Unit>) -> Unit,
    )

    suspend fun getLoggedInUser(): Flow<DomainResult<MemberEntity>>

    suspend fun getAuthState(): Flow<DomainResult<Unit>>

    suspend fun getAppVersion(): Flow<DomainResult<String>>

    fun observeUser(): StateFlow<MemberEntity>
}
