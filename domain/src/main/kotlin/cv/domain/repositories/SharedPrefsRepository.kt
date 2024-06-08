package cv.domain.repositories

interface SharedPrefsRepository {
    fun setBool(settingName: String, value: Boolean)

    fun getBool(settingName: String): Boolean

    fun getString(settingName: String): String

    fun setString(settingName: String, value: String)
}
