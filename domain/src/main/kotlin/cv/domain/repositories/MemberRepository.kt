package cv.domain.repositories

import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    suspend fun getMemberById(memberId: String): DomainResult<MemberEntity>

    fun getMembers(fetchType: MemberFetchType): Flow<DomainResult<List<MemberEntity>>>

    suspend fun updateMember(memberEntity: MemberEntity): DomainResult<Unit>

    suspend fun deleteMember(memberEntity: MemberEntity): DomainResult<Unit>
}
