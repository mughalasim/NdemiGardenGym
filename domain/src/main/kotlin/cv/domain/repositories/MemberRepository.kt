package cv.domain.repositories

import cv.domain.DomainResult
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.MemberEntity

interface MemberRepository {
    suspend fun getMember(memberId: String): DomainResult<MemberEntity>

    suspend fun getAllMembers(isLive: Boolean): DomainResult<List<MemberEntity>>

    suspend fun updateMember(memberEntity: MemberEntity): DomainResult<Boolean>

    suspend fun getAttendances(isMembersAttendances: Boolean, year: Int, month: Int): DomainResult<List<AttendanceEntity>>
}
