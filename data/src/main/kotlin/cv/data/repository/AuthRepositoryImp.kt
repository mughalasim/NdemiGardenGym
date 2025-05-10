package cv.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import cv.data.mappers.toMemberEntity
import cv.data.models.MemberModel
import cv.data.models.VersionModel
import cv.data.retrofit.toDomainError
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.repositories.AppLogLevel
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow

class AuthRepositoryImp(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val repositoryUrls: AuthRepositoryUrls,
    private val logger: AppLoggerRepository,
) : AuthRepository {
    private val _memberEntity: MutableStateFlow<MemberEntity> = MutableStateFlow(MemberEntity())
    val memberEntity: StateFlow<MemberEntity> = _memberEntity

    override fun isAuthenticated() = firebaseAuth.currentUser != null

    override fun getMemberType() = _memberEntity.value.memberType

    override fun logOut() = firebaseAuth.signOut()

    // TODO - All should be suspend
    override fun register(
        email: String,
        password: String,
        callback: (DomainResult<String>) -> Unit,
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                result.user?.let {
                    callback.invoke(DomainResult.Success(it.uid))
                } ?: run {
                    callback.invoke(DomainResult.Error(DomainError.NO_DATA))
                }
            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                callback.invoke(DomainResult.Error(it.toDomainError()))
            }
    }

    override fun login(
        email: String,
        password: String,
        callback: (DomainResult<Unit>) -> Unit,
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                result.user?.let {
                    callback.invoke(DomainResult.Success(Unit))
                } ?: run {
                    callback.invoke(DomainResult.Error(DomainError.NO_DATA))
                }
            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                callback.invoke(DomainResult.Error(it.toDomainError()))
            }
    }

    override fun resetPasswordForEmail(
        email: String,
        callback: (DomainResult<Unit>) -> Unit,
    ) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                callback.invoke(DomainResult.Success(Unit))
            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                callback.invoke(DomainResult.Error(it.toDomainError()))
            }
    }

    override suspend fun getAuthState(): Flow<DomainResult<Unit>> =
        callbackFlow {
            firebaseAuth.addAuthStateListener {
                if (it.uid.isNullOrEmpty()) {
                    trySend(DomainResult.Error(DomainError.UNAUTHORISED)).isSuccess
                } else {
                    trySend(DomainResult.Success(Unit)).isSuccess
                }
            }
            awaitClose()
        }

    override suspend fun getAppVersion(): Flow<DomainResult<String>> =
        callbackFlow {
            val eventDocument = firebaseFirestore.collection(repositoryUrls.pathVersion).document(repositoryUrls.pathVersionType)

            val subscription =
                eventDocument.addSnapshotListener { snapshot, error ->
                    snapshot?.let { response ->
                        logger.log("Data received: $response")
                        val versionModel = response.toObject<VersionModel>()
                        versionModel?.let {
                            if (it.version > repositoryUrls.currentAppVersion) {
                                trySend(DomainResult.Success(it.url))
                            } else {
                                trySend(DomainResult.Error(DomainError.NO_DATA))
                            }
                        } ?: run {
                            trySend(DomainResult.Error(DomainError.NO_DATA))
                        }
                    }
                    error?.let {
                        logger.log("Exception: $error", AppLogLevel.ERROR)
                        trySend(DomainResult.Error(error.toDomainError()))
                    }
                }
            awaitClose { subscription.remove() }
        }

    override suspend fun getLoggedInUser(): Flow<DomainResult<MemberEntity>> =
        callbackFlow {
            firebaseAuth.currentUser?.uid?.let {
                val eventDocument = firebaseFirestore.collection(repositoryUrls.pathUser).document(it)

                val subscription =
                    eventDocument.addSnapshotListener { snapshot, error ->
                        snapshot?.let { response ->
                            logger.log("Data received: $response")
                            val memberModel = response.toObject<MemberModel>()
                            memberModel?.let {
                                _memberEntity.value = memberModel.toMemberEntity(firebaseAuth.currentUser?.isEmailVerified == true)
                                trySend(DomainResult.Success(_memberEntity.value))
                            } ?: run {
                                trySend(DomainResult.Error(DomainError.UNAUTHORISED))
                            }
                        }
                        error?.let {
                            logger.log("Exception: $error", AppLogLevel.ERROR)
                            trySend(DomainResult.Error(error.toDomainError()))
                        }
                    }
                awaitClose { subscription.remove() }
            }.run {
                logger.log("Member UID is null or empty", AppLogLevel.ERROR)
                trySend(DomainResult.Error(DomainError.UNAUTHORISED))
                awaitClose()
            }
        }

    override fun observeUser() = memberEntity
}

data class AuthRepositoryUrls(
    val pathUser: String,
    val pathVersion: String,
    val pathVersionType: String,
    val currentAppVersion: Int,
)
