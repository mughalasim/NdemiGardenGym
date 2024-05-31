package com.ndemi.garden.gym.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.ndemi.garden.gym.navigation.NavigationService

class MainScreenViewModel(
    private val navigationService: NavigationService,
) : ViewModel() {
    fun setNavController(navController: NavHostController) = navigationService.setNavController(navController)

    fun getNavigationService(): NavigationService = navigationService
}
