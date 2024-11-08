package cv.data.repository

import android.content.SharedPreferences
import cv.domain.repositories.SharedPrefsRepository

class SharedPrefsRepositoryImp(
    private val sharedPreferences: SharedPreferences,
) : SharedPrefsRepository {

    override fun setBool(settingName: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(settingName, value).apply()
    }

    override fun getBool(settingName: String): Boolean {
        return sharedPreferences.getBoolean(settingName, false)
    }

    override fun setString(settingName: String, value: String) {
        sharedPreferences.edit().putString(settingName, value).apply()
    }

    override fun getString(settingName: String): String {
        return sharedPreferences.getString(settingName, "").orEmpty()
    }
}
