package com.ndemi.garden.gym.navigation

import androidx.navigation.NavController

interface NavigationService {
    fun setNavController(navController: NavController)

    fun open(
        route: Route,
        removeCurrentFromStack: Boolean = false,
    )

    fun popBack()

    fun getCurrentRoute(): Route

    fun getInitialRoute(): Route
}
