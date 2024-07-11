package com.ndemi.garden.gym.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.imageResource
import androidx.navigation.NavHostController
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationHost
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.widgets.BottomNavItem
import com.ndemi.garden.gym.ui.widgets.BottomNavigationWidget


@Composable
fun MainDetailsScreen(
    isAuthenticated: Boolean,
    isAdmin: Boolean,
    navController: NavHostController,
    navigationService: NavigationService,
){
    Scaffold(
        topBar = {},
        bottomBar = {
            val bottomNavItems = if (isAuthenticated) {
                if (isAdmin) {
                    BottomNavItem.getAdminBottomItems()
                } else {
                    BottomNavItem.getMemberBottomItems()
                }
            } else {
                BottomNavItem.getLoginBottomItems()
            }
            BottomNavigationWidget(
                navController,
                bottomNavItems
            )
        },
    ) { innerPadding ->
        val image = ImageBitmap.imageResource(R.drawable.bg_pattern)
        val brush = remember(image) { ShaderBrush(ImageShader(image, TileMode.Repeated, TileMode.Repeated)) }
        Column(
            modifier =
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(AppTheme.colors.backgroundScreen)
                .background(brush, alpha = 0.05f),
            verticalArrangement = Arrangement.spacedBy(padding_screen),
        ) {
            NavigationHost(
                navController = navController,
                navigationService = navigationService,
            )
        }
    }
}
