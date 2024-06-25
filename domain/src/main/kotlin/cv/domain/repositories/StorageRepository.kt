package cv.domain.repositories

import cv.domain.DomainResult

interface StorageRepository {

    suspend fun updateImageForMember(byteArray: ByteArray): DomainResult<String>
}