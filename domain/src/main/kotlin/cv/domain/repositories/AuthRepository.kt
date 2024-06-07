package cv.domain.repositories

interface AuthRepository {
    fun isAuthenticated(): Boolean

    fun isAdmin(): Boolean

    fun logOut()

    fun register(email: String, password: String, callback: (String) -> Unit)

    fun login(email: String, password: String, callback: (String) -> Unit)
}
