package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.Variables.EVENT_PHOTO
import cv.domain.Variables.PARAM_PHOTO_UPLOAD
import cv.domain.dispatchers.ScopeProvider
import cv.domain.entities.MemberEntity
import cv.domain.enums.DomainErrorType
import cv.domain.repositories.AnalyticsRepository
import cv.domain.repositories.MemberRepository
import cv.domain.repositories.StorageRepository
import kotlinx.coroutines.withContext

class StorageUseCase(
    private val scope: ScopeProvider,
    private val storageRepository: StorageRepository,
    private val memberRepository: MemberRepository,
    private val analyticsRepository: AnalyticsRepository,
) {
    suspend fun updateImageForMember(
        memberEntity: MemberEntity,
        byteArray: ByteArray,
    ): DomainResult<Unit> =
        withContext(scope.ioDispatcher()) {
            analyticsRepository.logEvent(EVENT_PHOTO, PARAM_PHOTO_UPLOAD, memberEntity.id)
            return@withContext when (val responseUploadFile = storageRepository.updateImageForMember(memberEntity.id, byteArray)) {
                is DomainResult.Error -> {
                    DomainResult.Error(DomainErrorType.UPLOAD_FAILURE)
                }

                is DomainResult.Success -> {
                    memberRepository.updateMember(memberEntity.copy(profileImageUrl = responseUploadFile.data))
                }
            }
        }
}
