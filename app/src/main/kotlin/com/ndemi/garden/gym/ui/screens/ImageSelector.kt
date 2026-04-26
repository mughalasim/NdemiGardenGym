package com.ndemi.garden.gym.ui.screens

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.runtime.Composable

class ImageSelector {
    private lateinit var launcher: ActivityResultLauncher<String>

    @Composable
    fun SetUpResult(
        context: Context,
        callBack: (ByteArray) -> Unit,
    ) {
        launcher =
            rememberLauncherForActivityResult(GetContent()) { imageUri ->
                imageUri?.let {
                    context.contentResolver
                        .openInputStream(imageUri)
                        ?.use { it.buffered().readBytes() }
                        ?.let { callBack.invoke(it) }
                }
            }
    }

    fun openImages() {
        launcher.launch("image/*")
    }
}
