package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_large
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun EditTextWidget(
    modifier: Modifier = Modifier,
    textInput: String = "",
    hint: String = "Hint",
    isError: Boolean = false,
    isEnabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChanged: (String) -> Unit = {},
) {
    var text by remember { mutableStateOf(textInput) }
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = padding_screen),
        value = text,
        enabled = isEnabled,
        isError = isError,
        singleLine = true,
        onValueChange = {
            text = it
            onValueChanged(text)
        },
        textStyle = AppTheme.textStyles.regular,
        trailingIcon = {
            if (text.isNotEmpty()){
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "Clear text",
                    modifier = Modifier.clickable {
                        onValueChanged.invoke("")
                        text = ""
                    }
                )
            }
        },
        label = { TextSmall(text = hint) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = getAppTextColors(),
        shape = RoundedCornerShape(border_radius)
    )
}

@Composable
fun EditPasswordTextWidget(
    modifier: Modifier = Modifier,
    textInput: String = "",
    hint: String = "Password hint",
    isError: Boolean = false,
    isEnabled: Boolean = true,
    onValueChanged: (String) -> Unit = {},
) {
    var password: String by remember { mutableStateOf(textInput) }
    var passwordVisible: Boolean by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = padding_screen),
        value = password,
        onValueChange = {
            password = it
            onValueChanged(password)
        },
        textStyle = AppTheme.textStyles.regular,
        label = { TextSmall(text = hint) },
        singleLine = true,
        visualTransformation =
        if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible) {
                Icons.Outlined.Lock
            } else {
                Icons.Filled.Lock
            }

            IconButton(onClick = {passwordVisible = !passwordVisible}){
                Icon(imageVector  = image, null)
            }
        },
        isError = isError,
        enabled = isEnabled,
        colors = getAppTextColors(),
        shape = RoundedCornerShape(border_radius)
    )
}

@Composable
private fun getAppTextColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = AppTheme.colors.textPrimary,
    focusedTrailingIconColor = AppTheme.colors.highLight,
    focusedBorderColor = AppTheme.colors.highLight,

    unfocusedTextColor = AppTheme.colors.textPrimary,
    unfocusedTrailingIconColor = AppTheme.colors.backgroundButtonDisabled,
    unfocusedBorderColor = AppTheme.colors.backgroundButtonDisabled,

    disabledTextColor = AppTheme.colors.textPrimary,
    disabledTrailingIconColor = Color.Transparent,
    disabledBorderColor = Color.Transparent,

    errorTextColor = AppTheme.colors.backgroundError,
    errorTrailingIconColor = AppTheme.colors.backgroundError,
    errorBorderColor = AppTheme.colors.backgroundError,

    focusedContainerColor = AppTheme.colors.backgroundScreen,
    unfocusedContainerColor = AppTheme.colors.backgroundScreen,
    disabledContainerColor = AppTheme.colors.backgroundScreen,
    errorContainerColor = AppTheme.colors.backgroundScreen,
)

@AppPreview
@Composable
fun EditTextPreviewsNight(){
    AppThemeComposable {
        Column {
            EditPasswordTextWidget()
            EditPasswordTextWidget(textInput = "Normal")
            EditPasswordTextWidget(textInput = "Error", isError = true)
            EditPasswordTextWidget(textInput = "Disabled", isEnabled = false)

            Spacer(modifier = Modifier.height(padding_screen_large))
            EditTextWidget()
            EditTextWidget(textInput = "Normal")
            EditTextWidget(textInput = "Error", isError = true)
            EditTextWidget(textInput = "Disabled", isEnabled = false)
        }
    }
}
