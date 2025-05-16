package com.ndemi.garden.gym.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationHost
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.enums.SnackbarType
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.widgets.BottomNavigationWidget
import com.ndemi.garden.gym.ui.widgets.VerifyEmailWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainDetailsScreen(
    viewModel: MainScreenViewModel = koinViewModel<MainScreenViewModel>(),
    navController: NavHostController,
    navigationService: NavigationService,
) {
    val emailVerifyState by viewModel.emailVerifiedState.collectAsStateWithLifecycle()

    AppThemeComposable(
        bottomBar = {
            BottomNavigationWidget(navController, navigationService.getBottomNavItems())
        },
        snackbarHost = {
            SnackbarHost(hostState = viewModel.snackbarHostState.hostState) {
                viewModel.snackbarHostState.SnackbarContent(snackbarData = it)
            }
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (emailVerifyState is MainScreenViewModel.EmailVerifiedState.Visible) {
                VerifyEmailWidget(viewModel::verifyEmail)
            }
            NavigationHost(
                navController = navController,
                navigationService = navigationService,
                snackbarHostState = viewModel.snackbarHostState,
            )
        }
    }

    if (emailVerifyState is MainScreenViewModel.EmailVerifiedState.Success) {
        viewModel.snackbarHostState.Show(
            type = SnackbarType.SUCCESS,
            message = stringResource(R.string.txt_email_successfully_sent),
        )
    }
}
