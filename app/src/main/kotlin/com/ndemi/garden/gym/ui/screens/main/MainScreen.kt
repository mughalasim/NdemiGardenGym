package com.ndemi.garden.gym.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ndemi.garden.gym.navigation.NavigationHost
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.navigation.Route.Companion.toRoute
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.BottomNavItem
import com.ndemi.garden.gym.ui.widgets.BottomNavigationWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = koinViewModel<MainScreenViewModel>(),
) {
    val navController = rememberNavController()
    viewModel.setNavController(navController)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.toRoute()

    Scaffold(
        topBar = {},
        bottomBar = {
            val bottomNavItems = when (currentRoute) {
                Route.LoginScreen -> BottomNavItem.getLoginBottomItems()
                else -> {
                    if (viewModel.isAuthenticated() && viewModel.isAdmin()){
                        BottomNavItem.getAdminBottomItems()
                    } else if (viewModel.isAuthenticated()){
                        BottomNavItem.getMemberBottomItems()
                    } else {
                        BottomNavItem.getLoginBottomItems()
                    }
                }
            }
            BottomNavigationWidget(
                navController,
                bottomNavItems
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = AppTheme.colors.backgroundScreen),
            verticalArrangement = Arrangement.spacedBy(padding_screen),
        ) {
            NavigationHost(
                navController = navController,
                navigationService = viewModel.getNavigationService(),
            )
        }
    }
}
