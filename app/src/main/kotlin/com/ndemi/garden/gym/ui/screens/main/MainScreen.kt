package com.ndemi.garden.gym.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ndemi.garden.gym.navigation.NavigationHost
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
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
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {},
        bottomBar = {
            val bottomNavItems = when (currentRoute) {
                Route.LoginScreen.routeName -> BottomNavItem.getLoginBottomItems()
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

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun MainScreenPreview() {
    AppThemeComposable {
        MainScreen()
    }
}
