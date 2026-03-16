package cv.data.repository

import cv.domain.repositories.JobRepository
import kotlinx.coroutines.Job

class JobRepositoryImp : JobRepository {
    private val jobs = mutableMapOf<String, Job>()

    override fun add(
        job: Job,
        name: String,
    ) {
        cancel(name)
        jobs[name] = job
    }

    override fun cancel(name: String) {
        jobs[name]?.cancel()
        jobs.remove(name)
    }

    override fun cancelAll() {
        jobs.values.forEach { it.cancel() }
        jobs.clear()
    }
}
