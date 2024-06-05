package com.ndemi.garden.gym.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.TextLarge
import com.ndemi.garden.gym.ui.widgets.TextRegular
import com.ndemi.garden.gym.ui.widgets.TextSmall
import cv.domain.entities.getMockMemberEntity

@Composable
fun ProfileScreen(
    // viewModel: ProfileScreenViewModel = koinViewModel<ProfileScreenViewModel>()
) {
//    val uiState = viewModel.uiStateFlow.collectAsState(
//        initial = UiState.Loading
//    )

    val memberEntity = getMockMemberEntity()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding_screen),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextLarge(text = "Profile")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = padding_screen_small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            TextSmall(text = "First name:")
            Spacer(modifier = Modifier.padding(padding_screen_small))
            TextRegular(text = memberEntity.firstName)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = padding_screen_small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            TextSmall(text = "Last name:")
            Spacer(modifier = Modifier.padding(padding_screen_small))
            TextRegular(text = memberEntity.lastName)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = padding_screen_small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            TextSmall(text = "Email:")
            Spacer(modifier = Modifier.padding(padding_screen_small))
            TextRegular(text = memberEntity.email)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = padding_screen_small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            TextSmall(text = "Registration Date:")
            Spacer(modifier = Modifier.padding(padding_screen_small))
            TextRegular(text = memberEntity.registrationDate)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = padding_screen_small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            TextSmall(text = "Membership renewal Date:")
            Spacer(modifier = Modifier.padding(padding_screen_small))
            memberEntity.renewalFutureDate?.let {
                TextRegular(text = it)
            } ?: run {
                TextRegular(text = "Not Paid")
            }
        }

        Spacer(modifier = Modifier.padding(padding_screen_small))
        ButtonWidget(title = "LogOut", isEnabled = true) {
//            viewModel.onLogOutTapped()
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    AppThemeComposable {
        ProfileScreen()
    }
}
