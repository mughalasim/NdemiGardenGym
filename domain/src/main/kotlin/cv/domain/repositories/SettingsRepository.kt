package cv.domain.repositories

interface SettingsRepository {
    fun setBool(settingName: String, value: Boolean)

    fun getBool(settingName: String): Boolean

    fun getString(settingName: String): String

    fun setString(settingName: String, value: String)
}
