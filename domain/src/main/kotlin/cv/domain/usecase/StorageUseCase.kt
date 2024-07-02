package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.Variables.EVENT_PHOTO
import cv.domain.Variables.PARAM_PHOTO_UPLOAD
import cv.domain.entities.MemberEntity
import cv.domain.repositories.AnalyticsRepository
import cv.domain.repositories.MemberRepository
import cv.domain.repositories.StorageRepository

class StorageUseCase (
    private val storageRepository: StorageRepository,
    private val memberRepository: MemberRepository,
    private val analyticsRepository: AnalyticsRepository,
){
    suspend fun updateImageForMember(memberEntity: MemberEntity, byteArray: ByteArray): Boolean {
        analyticsRepository.logEvent(
            eventName = EVENT_PHOTO,
            params = listOf(
                Pair(PARAM_PHOTO_UPLOAD, memberEntity.id)
            )
        )
        when(val result = storageRepository.updateImageForMember(memberEntity.id, byteArray)){
            is DomainResult.Error -> return false
            is DomainResult.Success -> {
                val response = memberRepository.updateMember(memberEntity.copy(profileImageUrl = result.data))
                return when(response){
                    is DomainResult.Error -> false
                    is DomainResult.Success -> true
                }
            }
        }
    }
}
