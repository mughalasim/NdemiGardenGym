package cv.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import cv.domain.entities.MemberEntity
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.AuthRepository
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class AuthRepositoryImp(
    private val logger: AppLoggerRepository,
) : AuthRepository {
    private val backgroundExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private val database = Firebase.firestore

    override fun isAuthenticated() = Firebase.auth.currentUser != null

    override fun isAdmin() = Firebase.auth.currentUser?.email?.contains("admin") == true

    override fun logOut() = Firebase.auth.signOut()

    override fun getMember(callback: (String) -> Unit) {
        Firebase.auth.currentUser?.let {
            logger.log(it.uid)
            callback.invoke(it.uid)
        } ?: {
            logger.log("Failed to fetch user")
            callback.invoke("")
        }
     }

    override fun register(memberEntity: MemberEntity, password: String, callback: (String) -> Unit) {
        Firebase.auth.createUserWithEmailAndPassword(memberEntity.email, password)
            .addOnCompleteListener(backgroundExecutor) { task ->
                logger.log(task.isSuccessful.toString())
                if (task.isSuccessful) {
                    Firebase.auth.currentUser?.let {
                        logger.log("New member ID registered: ${it.uid}")
                        val newEntity = memberEntity.copy( id = it.uid )
                        database.collection("users")
                            .add(newEntity)
                            .addOnSuccessListener { documentReference ->
                                logger.log("DocumentSnapshot added with ID: ${documentReference.id}")
                                callback.invoke(it.uid)
                            }
                            .addOnFailureListener { e ->
                                logger.log("Error adding document $e")
                                callback.invoke("")
                            }
                    } ?: {
                        callback.invoke("")
                    }
                } else {
                    callback.invoke("")
                }
            }
    }

    override fun login(email: String, password: String, callback: (String) -> Unit) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(backgroundExecutor) { task ->
                logger.log(task.isSuccessful.toString())
                if (task.isSuccessful) {
                    Firebase.auth.currentUser?.let {
                        logger.log(it.uid)
                        callback.invoke(it.uid)
                    } ?: {
                        callback.invoke("")
                    }
                } else {
                    callback.invoke("")
                }
            }
    }
}
