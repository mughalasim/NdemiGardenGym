package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContactMail
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.InsertChartOutlined
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.navigation.Route.Companion.toRoute
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun BottomNavigationWidget(
    navHostController: NavHostController,
    navBottomItems: List<BottomNavItem> = listOf(),
) {
    NavigationBar(
        modifier =
            Modifier
                .background(AppTheme.colors.backgroundScreen)
                .background(
                    color = AppTheme.colors.backgroundButtonDisabled,
                ),
        containerColor = Color.Transparent,
    ) {
        val navStackBackEntry by navHostController.currentBackStackEntryAsState()
        val currentDestination = navStackBackEntry?.destination

        navBottomItems.forEach { item ->
            val isCurrentSelection =
                currentDestination?.hierarchy?.any {
                    it.route?.toRoute() == item.route
                } == true
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
                    TextWidget(text = stringResource(id = item.label), style = AppTheme.textStyles.small, color = item.icon.tintColor)
                },
                colors =
                    NavigationBarItemColors(
                        selectedIconColor = AppTheme.colors.backgroundScreen,
                        selectedTextColor = AppTheme.colors.textPrimary,
                        selectedIndicatorColor = AppTheme.colors.textPrimary,
                        disabledIconColor = AppTheme.colors.error,
                        disabledTextColor = AppTheme.colors.error,
                        unselectedIconColor = AppTheme.colors.textSecondary,
                        unselectedTextColor = AppTheme.colors.textSecondary,
                    ),
            )
        }
    }
}

sealed class BottomNavItem(val route: Route, val icon: ImageVector, val label: Int) {
    data object LoginScreen : BottomNavItem(
        Route.LoginScreen,
        Icons.Rounded.Lock,
        R.string.txt_login,
    )

    data object RegisterScreen : BottomNavItem(
        Route.RegisterScreen,
        Icons.Rounded.ContactMail,
        R.string.txt_register,
    )

    data object ResetPasswordScreen : BottomNavItem(
        Route.ResetPasswordScreen,
        Icons.Rounded.Key,
        R.string.txt_reset,
    )

    data object ProfileAdminScreen : BottomNavItem(
        Route.ProfileAdminScreen,
        Icons.Rounded.Person,
        R.string.txt_profile,
    )

    data object ProfileMemberScreen : BottomNavItem(
        Route.ProfileMemberScreen,
        Icons.Rounded.Person,
        R.string.txt_profile,
    )

    data object AttendanceScreen : BottomNavItem(
        Route.AttendanceScreen,
        Icons.Rounded.InsertChartOutlined,
        R.string.txt_attendance,
    )

    data object LiveAttendanceScreen : BottomNavItem(
        Route.LiveAttendanceScreen,
        Icons.Rounded.Groups,
        R.string.txt_live_view,
    )

    data object PaymentsScreen : BottomNavItem(
        Route.PaymentsScreen(),
        Icons.Rounded.MonetizationOn,
        R.string.txt_payments,
    )

    data object MembersScreen : BottomNavItem(
        Route.MembersScreen,
        Icons.Rounded.Group,
        R.string.txt_active,
    )

    data object MembersExpiredScreen : BottomNavItem(
        Route.MembersExpiredScreen,
        Icons.Rounded.Group,
        R.string.txt_inactive,
    )

    data object MembersActiveScreen : BottomNavItem(
        Route.MembersActiveScreen,
        Icons.Rounded.Group,
        R.string.txt_in_the_gym,
    )

    companion object {
        fun getMemberBottomItems() =
            listOf(
                ProfileMemberScreen,
                AttendanceScreen,
                PaymentsScreen,
                LiveAttendanceScreen,
            )

        fun getLoginBottomItems() =
            listOf(
                LoginScreen,
                RegisterScreen,
                ResetPasswordScreen,
            )

        fun getAdminBottomItems() =
            listOf(
                MembersScreen,
                MembersExpiredScreen,
                MembersActiveScreen,
                ProfileAdminScreen,
            )
    }
}

@AppPreview
@Composable
private fun BottomNavigationWidgetPreview() {
    AppThemeComposable {
        Column {
            BottomNavigationWidget(
                navHostController = rememberNavController(),
                BottomNavItem.getLoginBottomItems(),
            )
            BottomNavigationWidget(
                navHostController = rememberNavController(),
                BottomNavItem.getMemberBottomItems(),
            )
            BottomNavigationWidget(
                navHostController = rememberNavController(),
                BottomNavItem.getAdminBottomItems(),
            )
        }
    }
}
