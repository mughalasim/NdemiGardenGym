package com.ndemi.garden.gym.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationHost
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.widgets.BottomNavItem
import com.ndemi.garden.gym.ui.widgets.BottomNavigationWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainDetailsScreen(
    viewModel: MainScreenViewModel = koinViewModel<MainScreenViewModel>(),
    isAuthenticated: Boolean,
    isAdmin: Boolean,
    showEmailVerificationWarning: Boolean,
    navController: NavHostController,
    navigationService: NavigationService,
) {
    Scaffold(
        topBar = {},
        bottomBar = {
            // TODO - Add a SuperAdmin User that can do the following
            // - CRUDs all user types
            // - Force admins to verify email before using the app
            // - Accepts admin requests
            val bottomNavItems =
                when {
                    isAuthenticated && isAdmin -> BottomNavItem.getAdminBottomItems()
                    isAuthenticated -> BottomNavItem.getMemberBottomItems()
                    else -> BottomNavItem.getLoginBottomItems()
                }
            BottomNavigationWidget(navController, bottomNavItems)
        },
        snackbarHost = {
            SnackbarHost(hostState = viewModel.snackbarHostState.hostState) {
                viewModel.snackbarHostState.SnackbarContent(snackbarData = it)
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(AppTheme.colors.backgroundScreen),
        ) {
            // TODO - Add an extra feature to force users to verify emails first
            if (showEmailVerificationWarning) {
                WarningWidget(message = stringResource(R.string.error_email_not_verified))
            }
            NavigationHost(
                navController = navController,
                navigationService = navigationService,
                snackbarHostState = viewModel.snackbarHostState,
            )
        }
    }
}
