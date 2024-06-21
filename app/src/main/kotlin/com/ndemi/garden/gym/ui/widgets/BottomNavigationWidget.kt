package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.navigation.Route.Companion.toRoute
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun BottomNavigationWidget(
    navHostController: NavHostController,
    navBottomItems: List<BottomNavItem> = listOf(),
) {
    NavigationBar(
        modifier = Modifier
            .background(AppTheme.colors.backgroundScreen)
            .border(
                border = BorderStroke(line_thickness, AppTheme.colors.textSecondary),
                shape = RoundedCornerShape(
                    topStart = border_radius,
                    topEnd = border_radius
                )
            )
            .background(
                color = AppTheme.colors.backgroundButtonDisabled,
                shape = RoundedCornerShape(
                    topStart = border_radius,
                    topEnd = border_radius
                )
            ),
        containerColor = Color.Transparent
    ) {
        val navStackBackEntry by navHostController.currentBackStackEntryAsState()
        val currentDestination = navStackBackEntry?.destination

        navBottomItems.forEach { item ->
            val isCurrentSelection =
                currentDestination?.hierarchy?.any { it.route?.toRoute() == item.route } == true
            NavigationBarItem(
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
                    )
                },
                label = {
                    Text(text = item.label, style = AppTheme.textStyles.regular)
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = AppTheme.colors.textPrimary,
                    selectedTextColor = AppTheme.colors.textPrimary,
                    selectedIndicatorColor = AppTheme.colors.highLight,
                    disabledIconColor = AppTheme.colors.backgroundError,
                    disabledTextColor = AppTheme.colors.backgroundError,
                    unselectedIconColor = AppTheme.colors.textSecondary,
                    unselectedTextColor = AppTheme.colors.textSecondary
                )
            )
        }
    }
}

sealed class BottomNavItem(val route: Route, val icon: ImageVector, val label: String) {
    data object LoginScreen : BottomNavItem(
        Route.LoginScreen, Icons.Default.CheckCircle, "Login"
    )

    data object RegisterScreen : BottomNavItem(
        Route.RegisterScreen, Icons.Default.Email, "Register"
    )

    data object ResetPasswordScreen : BottomNavItem(
        Route.ResetPasswordScreen, Icons.Default.Create, "Reset"
    )

    data object ProfileScreen : BottomNavItem(
        Route.ProfileScreen, Icons.Default.AccountCircle, "Profile"
    )

    data object AttendanceScreen : BottomNavItem(
        Route.AttendanceScreen, Icons.Default.Favorite, "Attendance"
    )

    data object LiveAttendanceScreen : BottomNavItem(
        Route.LiveAttendanceScreen, Icons.Default.Info, "Live View"
    )

    data object MembersScreen : BottomNavItem(
        Route.MembersScreen, Icons.Default.Info, "Members"
    )

    companion object {
        fun getMemberBottomItems() = listOf(
            ProfileScreen,
            AttendanceScreen,
            LiveAttendanceScreen,
        )

        fun getLoginBottomItems() = listOf(
            LoginScreen,
            RegisterScreen,
            ResetPasswordScreen,
        )

        fun getAdminBottomItems() = listOf(
            ProfileScreen,
            MembersScreen,
        )
    }
}

@AppPreview
@Composable
fun BottomNavigationWidgetPreview() {
    AppThemeComposable {
        Column {
            BottomNavigationWidget(
                navHostController = rememberNavController(),
                BottomNavItem.getLoginBottomItems()
            )
            BottomNavigationWidget(
                navHostController = rememberNavController(),
                BottomNavItem.getMemberBottomItems()
            )
            BottomNavigationWidget(
                navHostController = rememberNavController(),
                BottomNavItem.getAdminBottomItems()
            )
        }
    }
}
