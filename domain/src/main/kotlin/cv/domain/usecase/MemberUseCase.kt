package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.Variables.EVENT_ACTIVE
import cv.domain.Variables.EVENT_CREATE_MEMBER
import cv.domain.Variables.EVENT_MEMBERSHIP
import cv.domain.Variables.EVENT_MEMBER_DELETE
import cv.domain.Variables.EVENT_MEMBER_UPDATE
import cv.domain.Variables.EVENT_PHOTO
import cv.domain.Variables.EVENT_REGISTRATION
import cv.domain.Variables.PARAM_ACTIVE_SESSION_FALSE
import cv.domain.Variables.PARAM_ACTIVE_SESSION_TRUE
import cv.domain.Variables.PARAM_MEMBERSHIP_ADDED
import cv.domain.Variables.PARAM_MEMBER_DELETE
import cv.domain.Variables.PARAM_MEMBER_UPDATE
import cv.domain.Variables.PARAM_PHOTO_DELETE
import cv.domain.Variables.PARAM_REGISTRATION_ADMIN
import cv.domain.Variables.PARAM_REGISTRATION_SELF
import cv.domain.entities.MemberEntity
import cv.domain.enums.DomainErrorType
import cv.domain.enums.MemberFetchType
import cv.domain.enums.MemberUpdateType
import cv.domain.repositories.AnalyticsRepository
import cv.domain.repositories.MemberRepository
import kotlinx.coroutines.flow.firstOrNull

class MemberUseCase(
    private val memberRepository: MemberRepository,
    private val analyticsRepository: AnalyticsRepository,
) {
    suspend fun getMemberById(memberId: String) = memberRepository.getMemberById(memberId = memberId)

    fun getAllMembers() = memberRepository.getMembers(fetchType = MemberFetchType.MEMBERS)

    fun getLiveMembers() = memberRepository.getMembers(fetchType = MemberFetchType.ACTIVE)

    fun getExpiredMembers() = memberRepository.getMembers(fetchType = MemberFetchType.EXPIRED_REGISTRATIONS)

    suspend fun updateMember(
        memberEntity: MemberEntity,
        memberUpdateType: MemberUpdateType,
    ): DomainResult<Unit> {
        when (memberUpdateType) {
            MemberUpdateType.CREATE -> {
                when (val result = memberRepository.getMembers(fetchType = MemberFetchType.ALL).firstOrNull()) {
                    is DomainResult.Success -> {
                        if (result.data.find { it.email == memberEntity.email } != null) {
                            return DomainResult.Error(DomainErrorType.EMAIL_ALREADY_EXISTS)
                        }
                    }
                    else -> return DomainResult.Error(DomainErrorType.SERVER)
                }
                analyticsRepository.logEvent(
                    eventName = EVENT_CREATE_MEMBER,
                    params =
                        listOf(
                            Pair(PARAM_REGISTRATION_ADMIN, memberEntity.id),
                        ),
                )
            }
            MemberUpdateType.REGISTRATION -> {
                analyticsRepository.logEvent(
                    eventName = EVENT_REGISTRATION,
                    params =
                        listOf(
                            Pair(PARAM_REGISTRATION_SELF, memberEntity.id),
                        ),
                )
            }
            MemberUpdateType.MEMBERSHIP -> {
                analyticsRepository.logEvent(
                    eventName = EVENT_MEMBERSHIP,
                    params =
                        listOf(
                            Pair(PARAM_MEMBERSHIP_ADDED, memberEntity.id),
                        ),
                )
            }

            MemberUpdateType.PHOTO_DELETE -> {
                analyticsRepository.logEvent(
                    eventName = EVENT_PHOTO,
                    params =
                        listOf(
                            Pair(PARAM_PHOTO_DELETE, memberEntity.id),
                        ),
                )
            }
            MemberUpdateType.ACTIVE_SESSION -> {
                val paramName =
                    if (memberEntity.isActiveNow()) {
                        PARAM_ACTIVE_SESSION_TRUE
                    } else {
                        PARAM_ACTIVE_SESSION_FALSE
                    }
                analyticsRepository.logEvent(
                    eventName = EVENT_ACTIVE,
                    params = listOf(Pair(paramName, memberEntity.id)),
                )
            }
            MemberUpdateType.DETAILS -> {
                analyticsRepository.logEvent(
                    eventName = EVENT_MEMBER_UPDATE,
                    params = listOf(Pair(PARAM_MEMBER_UPDATE, memberEntity.id)),
                )
            }
        }
        return memberRepository.updateMember(memberEntity)
    }

    suspend fun deleteMember(memberEntity: MemberEntity): DomainResult<Unit> {
        analyticsRepository.logEvent(
            eventName = EVENT_MEMBER_DELETE,
            params =
                listOf(
                    Pair(
                        PARAM_MEMBER_DELETE,
                        memberEntity.id +
                            " - " + memberEntity.getFullName(),
                    ),
                ),
        )
        return memberRepository.deleteMember(memberEntity)
    }
}
