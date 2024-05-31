package com.ndemi.garden.gym.ui.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.b3nedikt.restring.Restring
import com.ndemi.garden.gym.ui.theme.AppThemeComposable

class MainActivity : ComponentActivity() {
    override fun getResources() = Restring.wrapResources(this, super.getResources())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppThemeComposable {
                MainScreen()
            }
        }
    }
}
