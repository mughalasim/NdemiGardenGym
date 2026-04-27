package com.ndemi.garden.gym.ui.appSnackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AppSnackbarViewModel(
    val hostState: SnackbarHostState,
    val appSnackbar: AppSnackbar,
) : ViewModel() {
    private var appSnackbarJob: Job? = null
    private val appSnackbarState: MutableStateFlow<AppSnackbarState> = MutableStateFlow(AppSnackbarState.Gone)

    fun showSnackbar(data: AppSnackbarData) {
        appSnackbarJob?.let { if (it.isActive) it.cancel() }
        appSnackbarJob =
            viewModelScope.launch {
                appSnackbarState.value = AppSnackbarState.Visible(data)
                delay(SNACKBAR_CLEAR_TIME)
                appSnackbarState.value = AppSnackbarState.Gone
            }
    }

    @Composable
    fun ObserveAppSnackbar() {
        val snackbarState by appSnackbarState.collectAsStateWithLifecycle()
        when (val state = snackbarState) {
            is AppSnackbarState.Visible -> {
                appSnackbar.Show(
                    hostState = hostState,
                    data = state.data,
                )
            }

            else -> {}
        }
    }

    override fun onCleared() {
        super.onCleared()
        appSnackbarJob?.cancel()
    }

    @Immutable
    sealed interface AppSnackbarState {
        data object Gone : AppSnackbarState

        data class Visible(
            val data: AppSnackbarData,
        ) : AppSnackbarState
    }
}

private const val SNACKBAR_CLEAR_TIME = 5000L
