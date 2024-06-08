package cv.domain.usecase

import cv.domain.entities.AttendanceEntity
import cv.domain.entities.MemberEntity
import cv.domain.repositories.MemberRepository
import java.util.Date

class MemberUseCase(
    private val memberRepository: MemberRepository,
) {
    suspend fun getMember() = memberRepository.getMember()

    suspend fun getMember(memberId: String) = memberRepository.getMember(memberId)

    suspend fun getAllMembers() = memberRepository.getAllMembers(false)

    suspend fun getLiveMembers() = memberRepository.getAllMembers(true)

    suspend fun getMemberAttendances(year: Int, month: Int) = memberRepository.getAttendances(true, year, month)

    suspend fun getAllAttendances(year: Int, month: Int) = memberRepository.getAttendances(false, year, month)

    suspend fun addAttendance(startDate: Date, endDate: Date) = memberRepository.addAttendance(startDate, endDate)

    suspend fun updateMember(memberEntity: MemberEntity) = memberRepository.updateMember(memberEntity)

    suspend fun deleteAttendance(attendanceEntity: AttendanceEntity) = memberRepository.deleteAttendance(attendanceEntity)
}
