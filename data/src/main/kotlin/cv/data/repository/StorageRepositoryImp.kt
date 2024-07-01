package cv.data.repository

import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import cv.data.retrofit.toDomainError
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.repositories.AppLogLevel
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.StorageRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await

class StorageRepositoryImp(
    private val pathUserImage: String,
    private val logger: AppLoggerRepository,
): StorageRepository  {
    private val storageReference = Firebase.storage.reference

    override suspend fun updateImageForMember(memberId: String, byteArray: ByteArray): DomainResult<String> {
        if(memberId.isEmpty()){
            logger.log("Not Authorised", AppLogLevel.ERROR)
            return DomainResult.Error(DomainError.UNAUTHORISED)
        }
        val completable: CompletableDeferred<DomainResult<String>> = CompletableDeferred()

        storageReference.child("$pathUserImage$memberId.jpg").putBytes(byteArray).await().storage.downloadUrl
            .addOnSuccessListener { result ->
                val url = "https://" + result.encodedAuthority + result.encodedPath + "?" + result.encodedQuery
                completable.complete(DomainResult.Success(url))
            }
            .addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }
        return completable.await()
    }
}
