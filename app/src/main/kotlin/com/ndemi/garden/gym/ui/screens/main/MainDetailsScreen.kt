package com.ndemi.garden.gym.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ndemi.garden.gym.navigation.BottomNavItem
import com.ndemi.garden.gym.navigation.NavigationHost
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarViewModel
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.widgets.BottomNavigationWidget
import com.ndemi.garden.gym.ui.widgets.VerifyEmailWidget

@Composable
fun MainDetailsScreen(
    viewModel: MainScreenViewModel,
    snackbarViewModel: AppSnackbarViewModel,
    navController: NavHostController,
    initialRoute: Route,
    bottomNavItems: List<BottomNavItem>,
) {
    snackbarViewModel.ObserveAppSnackbar()
    val emailVerifyState by viewModel.emailVerifiedState.collectAsStateWithLifecycle()

    AppThemeComposable(
        bottomBar = {
            BottomNavigationWidget(navHostController = navController, navBottomItems = bottomNavItems)
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarViewModel.hostState) {
                snackbarViewModel.appSnackbar.SetContent(it)
            }
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (emailVerifyState is MainScreenViewModel.EmailVerifiedState.Visible) {
                VerifyEmailWidget(viewModel::verifyEmail)
            }
            NavigationHost(
                navController = navController,
                initialRoute = initialRoute,
                showSnackbar = snackbarViewModel::showSnackbar,
            )
        }
    }
}
