package cv.domain.usecase

import cv.domain.entities.MemberEntity
import cv.domain.repositories.MemberRepository

class MemberUseCase(
    private val memberRepository: MemberRepository,
) {
    suspend fun getMember(memberId: String = "") = memberRepository.getMember(memberId)

    suspend fun getAllMembers() = memberRepository.getAllMembers(false)

    suspend fun getAllLiveMembers() = memberRepository.getAllMembers(true)

    suspend fun getMemberAttendances(year: Int, month: Int) = memberRepository.getAttendances(true, year, month)

    suspend fun getAllAttendances(year: Int, month: Int) = memberRepository.getAttendances(false, year, month)

    suspend fun updateMember(memberEntity: MemberEntity) = memberRepository.updateMember(memberEntity)
}
