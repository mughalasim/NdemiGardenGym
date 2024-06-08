package cv.domain.repositories

import cv.domain.DomainResult
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.MemberEntity
import java.util.Date

interface MemberRepository {
    suspend fun getMember(): DomainResult<MemberEntity>

    suspend fun getMember(memberId: String): DomainResult<MemberEntity>

    suspend fun getAllMembers(isLive: Boolean): DomainResult<List<MemberEntity>>

    suspend fun updateMember(memberEntity: MemberEntity): DomainResult<Boolean>

    suspend fun getAttendances(isMembersAttendances: Boolean, year: Int, month: Int):
            DomainResult<List<AttendanceEntity>>

    suspend fun addAttendance(startDate: Date, endDate: Date): DomainResult<Unit>

    suspend fun deleteAttendance (attendanceEntity: AttendanceEntity): DomainResult<Unit>
}
