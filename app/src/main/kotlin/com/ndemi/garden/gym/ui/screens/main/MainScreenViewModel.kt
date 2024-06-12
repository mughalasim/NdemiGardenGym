package com.ndemi.garden.gym.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.ndemi.garden.gym.navigation.NavigationService
import cv.domain.usecase.AuthUseCase

class MainScreenViewModel(
    private val navigationService: NavigationService,
    private val authUseCase: AuthUseCase,
) : ViewModel() {
    fun setNavController(navController: NavHostController) =
        navigationService.setNavController(navController)

    fun getNavigationService(): NavigationService = navigationService

    fun isAuthenticated()  = authUseCase.isAuthenticated()

    fun isAdmin() = authUseCase.isAdmin()
}
