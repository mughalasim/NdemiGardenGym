package com.ndemi.garden.gym.ui.widgets

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.theme.AppTheme

@Composable
fun BottomNavigationWidget(navHostController: NavHostController) {
    BottomNavigation(
        backgroundColor = AppTheme.colors.highLight,
    ) {
        val tabList =
            listOf(
                BottomNavItem.LoginScreen,
                BottomNavItem.RegisterScreen,
                BottomNavItem.ResetPasswordScreen,
            )
        val navStackBackEntry by navHostController.currentBackStackEntryAsState()
        val currentDestination = navStackBackEntry?.destination

        tabList.forEach { item ->
            val isCurrentSelection = currentDestination?.hierarchy?.any { it.route == item.route } == true
            val selectedColor =
                if (isCurrentSelection) AppTheme.colors.textPrimary else AppTheme.colors.backgroundScreen
            BottomNavigationItem(
                selected = isCurrentSelection,
                onClick = {
                    if (!isCurrentSelection) {
                        navHostController.navigate(item.route) {
                            popUpTo(navHostController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = selectedColor,
                    )
                },
                label = {
                    Text(
                        item.label,
                        color = selectedColor,
                    )
                },
            )
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object LoginScreen :
        BottomNavItem(Route.LoginScreen.routeName, Icons.Default.CheckCircle, "Login")

    data object RegisterScreen :
        BottomNavItem(Route.RegisterScreen.routeName, Icons.Default.Email, "Register")

    data object ResetPasswordScreen :
        BottomNavItem(Route.ResetPasswordScreen.routeName, Icons.Default.Create, "Reset Password")
}
