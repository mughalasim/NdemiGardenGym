package com.ndemi.garden.gym.ui.screens.members

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toAppCardStyle
import com.ndemi.garden.gym.ui.widgets.EditTextWidget
import com.ndemi.garden.gym.ui.widgets.TextWidget

@Composable
fun SearchMemberComponent(
    textInput: String,
    isVisible: Boolean,
    memberCount: Int,
    onTextChanged: (String) -> Unit = {},
) {
    if (isVisible) {
        Box(
            modifier =
                Modifier
                    .padding(vertical = padding_screen_small)
                    .fillMaxWidth()
                    .toAppCardStyle(),
        ) {
            if (textInput.isEmpty()) {
                TextWidget(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.txt_search_members, memberCount.toString()),
                    style = AppTheme.textStyles.small,
                )
            }
            EditTextWidget(
                textInput = textInput,
                onValueChanged = onTextChanged,
            )
        }
    }
}

@AppPreview
@Composable
fun SearchMemberComponentPreview() =
    AppThemeComposable {
        SearchMemberComponent(
            textInput = "",
            isVisible = true,
            memberCount = 4,
        )
    }
