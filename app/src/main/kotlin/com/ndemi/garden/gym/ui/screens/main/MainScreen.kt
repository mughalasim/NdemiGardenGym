package com.ndemi.garden.gym.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.rememberNavController
import com.ndemi.garden.gym.ui.widgets.LoadingScreenWidget
import com.ndemi.garden.gym.ui.widgets.WarningWidget
import cv.domain.entities.MemberType
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = koinViewModel<MainScreenViewModel>(),
) {
    val navController = rememberNavController()
    viewModel.setNavController(navController)
    val authState = viewModel.authState.observeAsState()

    when(authState.value){
        MainScreenViewModel.AuthState.Authorised -> {
            val data = viewModel.loggedInMember.observeAsState()
            when(val response = data.value){

                is MainScreenViewModel.UiState.Error -> {
                    WarningWidget(title = response.message)
                }

                MainScreenViewModel.UiState.Loading -> {
                    LoadingScreenWidget()
                }

                is MainScreenViewModel.UiState.Success -> {
                    MainDetailsScreen(
                        isAuthenticated = true,
                        isAdmin = response.member.memberType != MemberType.MEMBER,
                        navController = navController,
                        navigationService = viewModel.getNavigationService()
                    )
                }

                null -> {
                    LoadingScreenWidget()
                }
            }
        }

        MainScreenViewModel.AuthState.UnAuthorised -> {
            MainDetailsScreen(
                isAuthenticated = false,
                isAdmin = false,
                navController = navController,
                navigationService = viewModel.getNavigationService()
            )
        }

        null -> {
            LoadingScreenWidget()
        }
    }
}

