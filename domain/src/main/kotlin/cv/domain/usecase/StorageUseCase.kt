package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.repositories.MemberRepository
import cv.domain.repositories.StorageRepository

class StorageUseCase (
    private val storageRepository: StorageRepository,
    private val memberRepository: MemberRepository,
){
    suspend fun updateImageForMember(memberEntity: MemberEntity, byteArray: ByteArray): Boolean {
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
