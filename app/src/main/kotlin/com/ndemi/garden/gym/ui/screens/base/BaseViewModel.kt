package com.ndemi.garden.gym.ui.screens.base

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.ui.enums.SnackbarType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@Suppress("ktPropBy")
open class BaseViewModel<State : BaseState, Action : BaseAction<State>>(initialViewState: State) : ViewModel() {
    private val _uiStateFlow = MutableStateFlow(initialViewState)
    val uiStateFlow = _uiStateFlow.asStateFlow()

    private val _snackbarState: MutableStateFlow<SnackbarState> = MutableStateFlow(SnackbarState.Gone)
    val snackbarState: StateFlow<SnackbarState> = _snackbarState

    private var viewState by Delegates.observable(initialViewState) { _, old, new ->
        if (old != new) {
            viewModelScope.launch {
                _uiStateFlow.value = new
            }
        }
    }

    fun sendAction(action: Action) {
        viewState = action.reduce(state = viewState)
    }

    fun showSnackbar(
        type: SnackbarType,
        message: String,
    ) {
        viewModelScope.launch {
            _snackbarState.value = SnackbarState.Visible(type, message)
            delay(SNACKBAR_CLEAR_TIME)
            _snackbarState.value = SnackbarState.Gone
        }
    }

    @Immutable
    sealed interface SnackbarState {
        data object Gone : SnackbarState

        data class Visible(val snackbarType: SnackbarType, val message: String) : SnackbarState
    }
}

private const val SNACKBAR_CLEAR_TIME = 5000L
