package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun BottomNavigationWidget(
    navHostController: NavHostController,
    navBottomItems: List<BottomNavItem> = listOf()
) {
    BottomNavigation(
        backgroundColor = AppTheme.colors.backgroundScreen,
    ) {
        val navStackBackEntry by navHostController.currentBackStackEntryAsState()
        val currentDestination = navStackBackEntry?.destination

        navBottomItems.forEach { item ->
            val isCurrentSelection = currentDestination?.hierarchy?.any { it.route?.toRoute() == item.route } == true
            val selectedColor =
                if (isCurrentSelection) AppTheme.colors.highLight else AppTheme.colors.backgroundButtonDisabled
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
                    TextSmall(
                        text = item.label,
                        color = selectedColor,
                    )
                },
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
        Route.LiveAttendanceScreen, Icons.Default.Info, "Live view"
    )

    data object MembersScreen : BottomNavItem(
        Route.MembersScreen, Icons.Default.Info, "Members"
    )

    companion object{
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
fun BottomNavigationWidgetPreview(){
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
