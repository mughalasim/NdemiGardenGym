package com.ndemi.garden.gym.ui.screens.register

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.rememberImeState
import com.ndemi.garden.gym.ui.widgets.ButtonWidget
import com.ndemi.garden.gym.ui.widgets.EditPasswordTextWidget
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextLarge

@Composable
fun RegisterScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding_screen)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextLarge(text = "Register")
        Spacer(modifier = Modifier.padding(padding_screen))
        EditTextWidget(hint = "First name"){

        }
        Spacer(modifier = Modifier.padding(padding_screen))
        EditTextWidget(hint = "Last Name"){

        }
        Spacer(modifier = Modifier.padding(padding_screen))
        EditTextWidget(hint = "Email"){

        }
        Spacer(modifier = Modifier.padding(padding_screen))
        EditPasswordTextWidget(hint = "Password"){

        }
        Spacer(modifier = Modifier.padding(padding_screen))
        EditPasswordTextWidget(hint = "Confirm password"){

        }
        Spacer(modifier = Modifier.padding(padding_screen))
        ButtonWidget(title = "Register", isEnabled = true) {

        }
    }
}
