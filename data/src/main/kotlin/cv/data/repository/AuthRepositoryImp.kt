package cv.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import cv.data.mappers.toMemberEntity
import cv.data.models.MemberModel
import cv.data.models.VersionModel
import cv.data.retrofit.toDomainError
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.entities.MemberType
import cv.domain.repositories.AppLogLevel
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthRepositoryImp(
    private val pathUser: String,
    private val pathVersion: String,
    private val pathVersionType: String,
    private val currentAppVersion: Int,
    private val logger: AppLoggerRepository,
) : AuthRepository {

    private lateinit var memberEntity: MemberEntity

    override fun isAuthenticated() = Firebase.auth.currentUser != null

    override fun getMemberType() = if (this::memberEntity.isInitialized) {
        memberEntity.memberType
    } else {
        MemberType.MEMBER
    }

    override fun logOut() = Firebase.auth.signOut()

    override fun register(email: String, password: String, callback: (DomainResult<String>) -> Unit) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                result.user?.let {
                    callback.invoke(DomainResult.Success(it.uid))
                } ?: run{
                    callback.invoke(DomainResult.Error(DomainError.NO_DATA))
                }
            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                callback.invoke(DomainResult.Error(it.toDomainError()))
            }
    }

    override fun login(email: String, password: String, callback: (DomainResult<Unit>) -> Unit) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                result.user?.let {
                    callback.invoke(DomainResult.Success(Unit))
                } ?: run{
                    callback.invoke(DomainResult.Error(DomainError.NO_DATA))
                }
            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                callback.invoke(DomainResult.Error(it.toDomainError()))
            }
    }

    override fun resetPasswordForEmail(email: String, callback: (DomainResult<Unit>) -> Unit){
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                callback.invoke(DomainResult.Success(Unit))
            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                callback.invoke(DomainResult.Error(it.toDomainError()))
            }
    }

    override suspend fun getAuthState(): Flow<DomainResult<Unit>> = callbackFlow {
        Firebase.auth.addAuthStateListener {
            if(it.uid.isNullOrEmpty()){
                trySend(DomainResult.Error(DomainError.UNAUTHORISED)).isSuccess
            } else {
                trySend(DomainResult.Success(Unit)).isSuccess
            }
        }
        awaitClose { }
    }

    override suspend fun getAppVersion() = callbackFlow {
        val eventDocument = Firebase.firestore.collection(pathVersion).document(pathVersionType)

        val subscription = eventDocument.addSnapshotListener { snapshot, error ->
            snapshot?.let { response ->
                logger.log("Data received: $response")
                val versionModel = response.toObject<VersionModel>()
                versionModel?.let {
                    if (it.version > currentAppVersion) {
                        trySend(DomainResult.Success(it.url)).isSuccess
                    } else {
                        trySend(DomainResult.Error(DomainError.NO_DATA)).isSuccess
                    }
                } ?: run {
                    trySend(DomainResult.Error(DomainError.NO_DATA)).isSuccess
                }
            }
            error?.let {
                logger.log("Exception: $error", AppLogLevel.ERROR)
                trySend(DomainResult.Error(error.toDomainError())).isSuccess
            }
        }
        awaitClose{ subscription.remove() }
    }

    override suspend fun getLoggedInUser(): Flow<DomainResult<MemberEntity>> = callbackFlow {
        Firebase.auth.currentUser?.uid?.let {
            val eventDocument = Firebase.firestore.collection(pathUser).document(it)

            val subscription = eventDocument.addSnapshotListener { snapshot, error ->
                snapshot?.let { response ->
                    logger.log("Data received: $response")
                    val memberModel = response.toObject<MemberModel>()
                    memberModel?.let {
                        memberEntity = memberModel.toMemberEntity()
                        trySend(DomainResult.Success(memberEntity)).isSuccess
                    } ?: run {
                        trySend(DomainResult.Error(DomainError.UNAUTHORISED)).isSuccess
                    }
                }
                error?.let {
                    logger.log("Exception: $error", AppLogLevel.ERROR)
                    trySend(DomainResult.Error(error.toDomainError())).isSuccess
                }
            }

            awaitClose{subscription.remove()}
        }.run {
            logger.log("Member UID is null or empty", AppLogLevel.ERROR)
            trySend(DomainResult.Error(DomainError.UNAUTHORISED)).isSuccess
            awaitClose {  }
        }
    }
}
