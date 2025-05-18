package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ndemi.garden.gym.navigation.BottomNavItem
import com.ndemi.garden.gym.navigation.BottomNavItem.AttendanceScreen
import com.ndemi.garden.gym.navigation.BottomNavItem.LoginScreen
import com.ndemi.garden.gym.navigation.BottomNavItem.MembersActiveScreen
import com.ndemi.garden.gym.navigation.BottomNavItem.MembersExpiredScreen
import com.ndemi.garden.gym.navigation.BottomNavItem.MembersScreen
import com.ndemi.garden.gym.navigation.BottomNavItem.NonMembersScreen
import com.ndemi.garden.gym.navigation.BottomNavItem.PaymentsScreen
import com.ndemi.garden.gym.navigation.BottomNavItem.ProfileAdminScreen
import com.ndemi.garden.gym.navigation.BottomNavItem.ProfileMemberScreen
import com.ndemi.garden.gym.navigation.BottomNavItem.ProfileSuperAdminScreen
import com.ndemi.garden.gym.navigation.BottomNavItem.RegisterScreen
import com.ndemi.garden.gym.navigation.BottomNavItem.ResetPasswordScreen
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
                currentDestination?.hierarchy?.any { it.route?.toRoute() == item.route } == true
            NavigationBarItem(
                selected = isCurrentSelection,
                onClick = {
                    if (!isCurrentSelection) {
                        navHostController.navigate(item.route) {
                            popUpTo(navHostController.graph.findStartDestination().id) { saveState = true }
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

@AppPreview
@Composable
private fun BottomNavigationWidgetPreview() {
    val getMemberBottomItems =
        listOf(
            ProfileMemberScreen,
            AttendanceScreen,
            PaymentsScreen,
            MembersActiveScreen,
        )

    val getLoginBottomItems =
        listOf(
            LoginScreen,
            RegisterScreen,
            ResetPasswordScreen,
        )

    val getAdminBottomItems =
        listOf(
            MembersScreen,
            MembersExpiredScreen,
            MembersActiveScreen,
            ProfileAdminScreen,
        )
    val getSuperAdminBottomItems =
        listOf(
            ProfileSuperAdminScreen,
            NonMembersScreen,
        )

    AppThemeComposable {
        Column {
            BottomNavigationWidget(
                navHostController = rememberNavController(),
                getLoginBottomItems,
            )
            BottomNavigationWidget(
                navHostController = rememberNavController(),
                getMemberBottomItems,
            )
            BottomNavigationWidget(
                navHostController = rememberNavController(),
                getAdminBottomItems,
            )
            BottomNavigationWidget(
                navHostController = rememberNavController(),
                getSuperAdminBottomItems,
            )
        }
    }
}
