package cv.domain.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

interface ScopeProvider {
    fun default(): CoroutineScope

    fun io(): CoroutineScope

    fun ioDispatcher(): CoroutineDispatcher
}

class ScopeProviderImp : ScopeProvider {
    private val defaultScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun default(): CoroutineScope = defaultScope

    override fun io(): CoroutineScope = ioScope

    override fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
