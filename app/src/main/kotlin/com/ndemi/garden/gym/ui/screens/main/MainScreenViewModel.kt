package com.ndemi.garden.gym.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.widgets.BottomNavItem

class MainScreenViewModel(
    private val navigationService: NavigationService,
) : ViewModel() {
    fun setNavController(navController: NavHostController) = navigationService.setNavController(navController)

    fun getNavigationService(): NavigationService = navigationService

    fun getBottomNavigationItems(): List<BottomNavItem> {
        val loginNavBottomItems =
            listOf(
                BottomNavItem.LoginScreen,
                BottomNavItem.RegisterScreen,
                BottomNavItem.ResetPasswordScreen,
            )
        val loggedInMemberNavBottomItems =
            listOf(
                BottomNavItem.ProfileScreen,
                BottomNavItem.AttendanceScreen,
                BottomNavItem.LiveAttendanceScreen,
            )
        val loggedInAdminNavBottomItems =
            listOf(
                BottomNavItem.ProfileScreen,
                BottomNavItem.MembersScreen,
                BottomNavItem.MembersAttendancesScreen,
            )
        return loginNavBottomItems
    }
}
