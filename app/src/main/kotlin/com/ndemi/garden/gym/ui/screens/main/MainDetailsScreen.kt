package com.ndemi.garden.gym.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationHost
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import com.ndemi.garden.gym.ui.widgets.BottomNavItem
import com.ndemi.garden.gym.ui.widgets.BottomNavigationWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget

@Composable
fun MainDetailsScreen(
    isAuthenticated: Boolean = false,
    isAdmin: Boolean = false,
    showEmailVerificationWarning: Boolean = false,
    navController: NavHostController,
    navigationService: NavigationService,
) {
    val snackbarHostState = remember { AppSnackbarHostState() }
    Scaffold(
        topBar = {},
        bottomBar = {
            val bottomNavItems =
                if (isAuthenticated) {
                    if (isAdmin) {
                        BottomNavItem.getAdminBottomItems()
                    } else {
                        BottomNavItem.getMemberBottomItems()
                    }
                } else {
                    BottomNavItem.getLoginBottomItems()
                }
            BottomNavigationWidget(
                navController,
                bottomNavItems,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState.hostState) {
                snackbarHostState.SnackbarContent(snackbarData = it)
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
            if (showEmailVerificationWarning) {
                WarningWidget(message = stringResource(R.string.error_email_not_verified))
            }
            NavigationHost(
                navController = navController,
                navigationService = navigationService,
                snackbarHostState = snackbarHostState,
            )
        }
    }
}
