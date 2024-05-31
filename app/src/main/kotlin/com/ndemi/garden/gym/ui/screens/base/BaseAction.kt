package com.ndemi.garden.gym.ui.screens.base

interface BaseAction<State> {
    fun reduce(state: State): State
}
