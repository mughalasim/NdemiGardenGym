package com.ndemi.garden.gym.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.widgets.LoadingScreenWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: MainScreenViewModel = koinViewModel<MainScreenViewModel>()) {
    val navController = rememberNavController()
    viewModel.setNavController(navController)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.startUp()

    when (val state = uiState) {
        MainScreenViewModel.UiState.Loading -> LoadingScreenWidget()

        is MainScreenViewModel.UiState.UpdateRequired -> NewVersionScreen(state.url)

        is MainScreenViewModel.UiState.UserNotFound ->
            Scaffold { innerPadding ->
                Column(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(AppTheme.colors.backgroundScreen),
                ) {
                    ToolBarWidget(
                        title = stringResource(R.string.app_name),
                        secondaryIcon = Icons.AutoMirrored.Filled.Logout,
                        onSecondaryIconPressed = viewModel::onLogOutTapped,
                    )
                    WarningWidget(message = state.message)
                }
            }

        is MainScreenViewModel.UiState.Ready -> {
            MainDetailsScreen(
                isAuthenticated = state.isAuthenticated,
                isAdmin = state.isAdmin,
                showEmailVerificationWarning = state.showEmailVerificationWarning,
                navController = navController,
                navigationService = viewModel.getNavigationService(),
            )
        }
    }
}
