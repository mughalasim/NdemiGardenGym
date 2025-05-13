package cv.domain.repositories

import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.enums.MemberType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    fun isAuthenticated(): Boolean

    fun getMemberType(): MemberType

    fun getMemberId(): String

    suspend fun getLoggedInUser(): Flow<DomainResult<MemberEntity>>

    suspend fun getAuthState(): Flow<DomainResult<Unit>>

    suspend fun getAppVersion(): Flow<DomainResult<String>>

    fun observeUser(): StateFlow<MemberEntity>
}
