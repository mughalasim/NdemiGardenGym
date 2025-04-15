package com.ndemi.garden.gym.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.rememberNavController
import com.ndemi.garden.gym.ui.screens.main.MainScreenViewModel.AuthState
import com.ndemi.garden.gym.ui.screens.main.MainScreenViewModel.VersionState
import com.ndemi.garden.gym.ui.widgets.LoadingScreenWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import cv.domain.entities.MemberType
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
                        isAuthenticated = false,
                        isAdmin = false,
                        navController = navController,
                        navigationService = viewModel.getNavigationService(),
                    )

                AuthState.Authorised -> {
                    val data =
                        viewModel.loggedInMember.observeAsState(initial = MainScreenViewModel.UiState.Loading)

                    when (val response = data.value) {
                        MainScreenViewModel.UiState.Loading -> LoadingScreenWidget()

                        is MainScreenViewModel.UiState.Error ->
                            WarningWidget(message = response.message)

                        is MainScreenViewModel.UiState.Success ->
                            MainDetailsScreen(
                                isAuthenticated = true,
                                isAdmin = response.member.memberType != MemberType.MEMBER,
                                navController = navController,
                                navigationService = viewModel.getNavigationService(),
                            )
                    }
                }
            }
        }
    }
}
