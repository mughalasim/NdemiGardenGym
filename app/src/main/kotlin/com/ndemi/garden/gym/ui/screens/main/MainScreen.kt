package com.ndemi.garden.gym.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationHost
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.BottomNavigationWidget
import com.ndemi.garden.gym.ui.widgets.ToolBarWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel = koinViewModel<MainScreenViewModel>()
    viewModel.setNavController(navController)

    Scaffold(
        topBar = {
            ToolBarWidget(stringResource(id = R.string.app_name))
        },
        bottomBar = {
            BottomNavigationWidget(navController)
        },
        floatingActionButton = {},
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding).background(color = AppTheme.colors.backgroundScreen),
            verticalArrangement = Arrangement.spacedBy(padding_screen),
        ) {
            NavigationHost(
                navController = navController,
                navigationService = viewModel.getNavigationService(),
            )
        }
    }
}
