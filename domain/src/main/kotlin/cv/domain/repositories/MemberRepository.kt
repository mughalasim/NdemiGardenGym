package cv.domain.repositories

import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    suspend fun getMemberById(memberId: String): DomainResult<MemberEntity>

    suspend fun getAllMembers(isActiveNow: Boolean): Flow<DomainResult<List<MemberEntity>>>

    suspend fun getExpiredMembers(): Flow<DomainResult<List<MemberEntity>>>

    suspend fun updateMember(memberEntity: MemberEntity): DomainResult<Boolean>

    suspend fun deleteMember(memberEntity: MemberEntity): DomainResult<Unit>
}
