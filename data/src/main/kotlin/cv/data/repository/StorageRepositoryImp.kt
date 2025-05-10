package cv.data.repository

import com.google.firebase.storage.StorageReference
import cv.data.retrofit.handleError
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.repositories.AppLogLevel
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.StorageRepository
import kotlinx.coroutines.tasks.await

class StorageRepositoryImp(
    private val storageReference: StorageReference,
    private val pathUserImage: String,
    private val logger: AppLoggerRepository,
) : StorageRepository {
    override suspend fun updateImageForMember(
        memberId: String,
        byteArray: ByteArray,
    ): DomainResult<String> {
        if (memberId.isEmpty()) {
            logger.log("Not Authorised", AppLogLevel.ERROR)
            return DomainResult.Error(DomainError.UNAUTHORISED)
        }

        val task = storageReference
            .child("$pathUserImage$memberId.jpg")
            .putBytes(byteArray)
            .await()
            .storage
            .downloadUrl

        task.await()

        return if (task.isSuccessful){
            val result = task.result
            val url = "https://${result.encodedAuthority}${result.encodedPath}?${result.encodedQuery}"
            DomainResult.Success(url)
        } else {
            handleError(task, logger)
        }
    }
}
