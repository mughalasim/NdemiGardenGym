package cv.domain.repositories

import cv.domain.DomainResult

interface StorageRepository {

    suspend fun updateImageForMember(memberId: String, byteArray: ByteArray): DomainResult<String>
}
