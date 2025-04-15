package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.Variables.EVENT_ACTIVE
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
import cv.domain.repositories.AnalyticsRepository
import cv.domain.repositories.MemberRepository

class MemberUseCase(
    private val memberRepository: MemberRepository,
    private val analyticsRepository: AnalyticsRepository,
) {
    suspend fun getMember() = memberRepository.getMember()

    suspend fun getMemberById(memberId: String) = memberRepository.getMemberById(memberId = memberId)

    suspend fun getAllMembers() = memberRepository.getAllMembers(isActiveNow = false)

    suspend fun getLiveMembers() = memberRepository.getAllMembers(isActiveNow = true)

    suspend fun getExpiredMembers() = memberRepository.getExpiredMembers()

    suspend fun updateMember(
        memberEntity: MemberEntity,
        updateType: UpdateType,
    ): DomainResult<Boolean> {
        when (updateType) {
            UpdateType.ADMIN_REGISTRATION -> {
                analyticsRepository.logEvent(
                    eventName = EVENT_REGISTRATION,
                    params =
                        listOf(
                            Pair(PARAM_REGISTRATION_ADMIN, memberEntity.id),
                        ),
                )
            }
            UpdateType.SELF_REGISTRATION -> {
                analyticsRepository.logEvent(
                    eventName = EVENT_REGISTRATION,
                    params =
                        listOf(
                            Pair(PARAM_REGISTRATION_SELF, memberEntity.id),
                        ),
                )
            }
            UpdateType.MEMBERSHIP -> {
                analyticsRepository.logEvent(
                    eventName = EVENT_MEMBERSHIP,
                    params =
                        listOf(
                            Pair(PARAM_MEMBERSHIP_ADDED, memberEntity.id),
                        ),
                )
            }

            UpdateType.PHOTO_DELETE -> {
                analyticsRepository.logEvent(
                    eventName = EVENT_PHOTO,
                    params =
                        listOf(
                            Pair(PARAM_PHOTO_DELETE, memberEntity.id),
                        ),
                )
            }
            UpdateType.ACTIVE_SESSION -> {
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
            UpdateType.MEMBER -> {
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

enum class UpdateType {
    ADMIN_REGISTRATION,
    SELF_REGISTRATION,
    MEMBERSHIP,
    PHOTO_DELETE,
    ACTIVE_SESSION,
    MEMBER,
}
