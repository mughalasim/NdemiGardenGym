package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun EditTextWidget(
    modifier: Modifier = Modifier,
    textInput: String = "",
    hint: String = "Hint",
    isError: Boolean = false,
    isEnabled: Boolean = true,
    onValueChanged: (String) -> Unit = {},
) {
    var text by remember { mutableStateOf(textInput) }
    OutlinedTextField(
        modifier = modifier,
        value = text,
        enabled = isEnabled,
        isError = isError,
        singleLine = true,
        onValueChange = {
            text = it
            onValueChanged(text)
        },
        label = { Text(hint)},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
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
        modifier = modifier,
        value = password,
        onValueChange = {
            password = it
            onValueChanged(password)
        },
        label = { Text(hint) },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Outlined.Lock
            else Icons.Filled.Lock

            IconButton(onClick = {passwordVisible = !passwordVisible}){
                Icon(imageVector  = image, null)
            }
        },
        isError = isError,
        enabled = isEnabled,
    )
}
