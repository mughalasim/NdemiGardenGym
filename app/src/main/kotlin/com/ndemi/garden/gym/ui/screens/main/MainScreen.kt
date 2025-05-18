package com.ndemi.garden.gym.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.screens.main.MainScreenViewModel.UiState
import com.ndemi.garden.gym.ui.utils.isValidUri
import com.ndemi.garden.gym.ui.widgets.LoadingScreenWidget
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: MainScreenViewModel = koinViewModel<MainScreenViewModel>()) {
    val navController = rememberNavController()
    viewModel.setNavController(navController)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    when (val state = uiState) {
        UiState.Loading -> LoadingScreenWidget()

        is UiState.UpdateRequired ->
            MessageScreen(
                title = stringResource(R.string.txt_app_update_title),
                message = stringResource(R.string.txt_app_update_desc),
                buttonText = stringResource(R.string.txt_download),
                onButtonTapped = {
                    if (state.url.isValidUri()) {
                        uriHandler.openUri(state.url)
                    }
                },
            )

        is UiState.UserNotFound ->
            MessageScreen(
                title = stringResource(R.string.txt_alert),
                message = state.message,
                buttonText = stringResource(R.string.txt_logout),
                onButtonTapped = viewModel::onLogOutTapped,
            )

        is UiState.Ready -> {
            MainDetailsScreen(
                navController = navController,
                navigationService = viewModel.getNavigationService(),
            )
        }
    }
}
