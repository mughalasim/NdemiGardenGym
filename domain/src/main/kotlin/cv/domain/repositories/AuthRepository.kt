package cv.domain.repositories

import cv.domain.entities.MemberEntity

interface AuthRepository {
    fun isAuthenticated(): Boolean

    fun isAdmin(): Boolean

    fun logOut()

    fun getMember(callback: (String) -> Unit)

    fun register(memberEntity: MemberEntity, password: String, callback: (String) -> Unit)

    fun login(email: String, password: String, callback: (String) -> Unit)
}
