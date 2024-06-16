package cv.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import cv.data.retrofit.toDomainError
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.AuthRepository

class AuthRepositoryImp(
    private val adminEmails: List<String>,
    private val logger: AppLoggerRepository,
) : AuthRepository {

    override fun isAuthenticated() = Firebase.auth.currentUser != null

    override fun isAdmin() = adminEmails.contains(Firebase.auth.currentUser?.email)

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
                logger.log("Exception: $it")
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
                logger.log("Exception: $it")
                callback.invoke(DomainResult.Error(it.toDomainError()))
            }
    }

    override fun resetPasswordForEmail(email: String, callback: (DomainResult<Unit>) -> Unit){
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                callback.invoke(DomainResult.Success(Unit))
            }.addOnFailureListener {
                logger.log("Exception: $it")
                callback.invoke(DomainResult.Error(it.toDomainError()))
            }
    }
}
