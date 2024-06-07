package cv.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.AuthRepository
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class AuthRepositoryImp(
    private val logger: AppLoggerRepository,
) : AuthRepository {
    private val backgroundExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    override fun isAuthenticated() = Firebase.auth.currentUser != null

    override fun isAdmin() = Firebase.auth.currentUser?.email?.contains("admin") == true

    override fun logOut() = Firebase.auth.signOut()

    override fun register(email: String, password: String, callback: (String) -> Unit) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(backgroundExecutor) { task ->
                logger.log(task.isSuccessful.toString())
                if (task.isSuccessful) {
                    Firebase.auth.currentUser?.let {
                        logger.log("New member ID registered: ${it.uid}")
                        callback.invoke(it.uid)
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
