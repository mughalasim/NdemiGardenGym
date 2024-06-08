package cv.domain.usecase

import cv.domain.Variables.SHARED_PREF_SESSION_START
import cv.domain.repositories.SharedPrefsRepository

class SharedPrefsUseCase(
    private val sharedPrefsRepository: SharedPrefsRepository,
) {
    fun setStartedSession(sessionTime: String) = sharedPrefsRepository.setString(SHARED_PREF_SESSION_START, sessionTime)

    fun getStartedSession(): String = sharedPrefsRepository.getString(SHARED_PREF_SESSION_START)
}
