package cv.domain.repositories

import kotlinx.coroutines.Job

interface JobRepository {
    fun add(
        job: Job,
        name: String,
    )

    fun cancelAll()

    fun cancel(name: String)
}
