package cv.domain.usecase

import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.Variables.EVENT_PHOTO
import cv.domain.Variables.PARAM_PHOTO_UPLOAD
import cv.domain.entities.MemberEntity
import cv.domain.repositories.AnalyticsRepository
import cv.domain.repositories.MemberRepository
import cv.domain.repositories.StorageRepository

class StorageUseCase(
    private val storageRepository: StorageRepository,
    private val memberRepository: MemberRepository,
    private val analyticsRepository: AnalyticsRepository,
) {
    suspend fun updateImageForMember(
        memberEntity: MemberEntity,
        byteArray: ByteArray,
    ): DomainResult<Unit> {
        analyticsRepository.logEvent(
            eventName = EVENT_PHOTO,
            params =
                listOf(
                    Pair(PARAM_PHOTO_UPLOAD, memberEntity.id),
                ),
        )
        return when (val responseUploadFile = storageRepository.updateImageForMember(memberEntity.id, byteArray)) {
            is DomainResult.Error -> DomainResult.Error(DomainError.UPLOAD_FAILURE)
            is DomainResult.Success -> {
                memberRepository.updateMember(memberEntity.copy(profileImageUrl = responseUploadFile.data))
            }
        }
    }
}
