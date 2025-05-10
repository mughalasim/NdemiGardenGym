package cv.data.repository

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code
import com.google.firebase.storage.StorageReference
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
    ): DomainResult<String> =
        runCatching {
            if (memberId.isEmpty()) {
                logger.log("Not authorised", AppLogLevel.ERROR)
                throw FirebaseFirestoreException("", Code.UNAUTHENTICATED)
            }

            storageReference
                .child("$pathUserImage$memberId.jpg")
                .putBytes(byteArray)
                .await()
                .storage
                .downloadUrl
                .await()
        }.fold(
            onSuccess = { result ->
                val url = "https://${result.encodedAuthority}${result.encodedPath}?${result.encodedQuery}"
                DomainResult.Success(url)
            },
            onFailure = { handleError(it, logger) },
        )
}
