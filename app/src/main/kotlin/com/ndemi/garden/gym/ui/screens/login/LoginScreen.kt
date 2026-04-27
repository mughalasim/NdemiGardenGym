package com.ndemi.garden.gym.ui.screens.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ndemi.garden.gym.ui.enums.LoginScreenInputType
import cv.domain.enums.MemberType

@Composable
fun LoginScreen(viewModel: LoginScreenViewModel) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val inputData by viewModel.inputData.collectAsStateWithLifecycle()

    LoginScreenDetails(
        uiState = uiState,
        listeners =
            LoginScreenListeners(
                onValueChanged = viewModel::setString,
                onLoginTapped = viewModel::onLoginTapped,
                onAutoCompleteTapped = viewModel::onAutoCompleteTapped,
            ),
        email = inputData.email,
        password = inputData.password,
    )
}

data class LoginScreenListeners(
    val onValueChanged: (String, LoginScreenInputType) -> Unit = { _, _ -> },
    val onAutoCompleteTapped: (MemberType) -> Unit = {},
    val onLoginTapped: () -> Unit = {},
)
