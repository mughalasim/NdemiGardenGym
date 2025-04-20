package com.ndemi.garden.gym.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.main.MainScreenViewModel.AuthState
import com.ndemi.garden.gym.ui.screens.main.MainScreenViewModel.VersionState
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.widgets.LoadingScreenWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: MainScreenViewModel = koinViewModel<MainScreenViewModel>()) {
    val navController = rememberNavController()
    viewModel.setNavController(navController)
    val versionState = viewModel.appVersion.observeAsState(initial = VersionState.Loading)

    when (versionState.value) {
        VersionState.Loading -> LoadingScreenWidget()

        is VersionState.UpdateRequired ->
            NewVersionScreen(url = (versionState.value as VersionState.UpdateRequired).url)

        VersionState.Success -> {
            val authState =
                viewModel.authState.observeAsState(initial = AuthState.Loading)

            when (authState.value) {
                AuthState.Loading -> LoadingScreenWidget()

                AuthState.UnAuthorised ->
                    MainDetailsScreen(
                        navController = navController,
                        navigationService = viewModel.getNavigationService(),
                    )

                AuthState.Authorised -> {
                    val data =
                        viewModel.loggedInMember.observeAsState(initial = MainScreenViewModel.UiState.Loading)

                    when (val response = data.value) {
                        MainScreenViewModel.UiState.Loading -> LoadingScreenWidget()

                        is MainScreenViewModel.UiState.Error -> {
                            Scaffold { innerPadding ->
                                Column(
                                    modifier =
                                        Modifier.padding(innerPadding)
                                            .fillMaxSize()
                                            .background(AppTheme.colors.backgroundScreen),
                                ) {
                                    ToolBarWidget(
                                        title = stringResource(R.string.app_name),
                                        secondaryIcon = Icons.AutoMirrored.Filled.Logout,
                                        onSecondaryIconPressed = viewModel::onLogOutTapped,
                                    )
                                    WarningWidget(message = response.message)
                                }
                            }
                        }

                        is MainScreenViewModel.UiState.Success ->
                            MainDetailsScreen(
                                isAuthenticated = true,
                                isAdmin = response.member.isAdmin(),
                                showEmailVerificationWarning = !response.member.emailVerified,
                                navController = navController,
                                navigationService = viewModel.getNavigationService(),
                            )
                    }
                }
            }
        }
    }
}
