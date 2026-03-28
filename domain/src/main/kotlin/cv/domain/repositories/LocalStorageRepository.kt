package cv.domain.repositories

import kotlinx.coroutines.flow.Flow

interface LocalStorageRepository {
    fun setString(
        key: String,
        value: String,
    )

    fun getString(key: String): String

    fun onStorageChanged(): Flow<Unit>
}
