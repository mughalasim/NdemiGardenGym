package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ndemi.garden.gym.navigation.BottomNavItem
import com.ndemi.garden.gym.navigation.getAdminBottomNavItems
import com.ndemi.garden.gym.navigation.getMemberBottomNavItems
import com.ndemi.garden.gym.navigation.getSuperAdminBottomNavItems
import com.ndemi.garden.gym.navigation.getUnauthenticatedBottomNavItems
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview

@Composable
fun BottomNavigationWidget(
    navHostController: NavHostController = rememberNavController(),
    navBottomItems: List<BottomNavItem> = listOf(),
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    if (navBottomItems.all { currentDestination?.hasRoute(it.route::class) == false }) {
        return
    }

    NavigationBar(
        modifier =
            Modifier
                .background(AppTheme.colors.backgroundScreen)
                .background(
                    color = AppTheme.colors.backgroundButtonDisabled,
                ),
        containerColor = Color.Transparent,
    ) {
        navBottomItems.forEach { item ->
            val isCurrentSelection = currentDestination?.hasRoute(item.route::class) == true
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
                    TextWidget(
                        textAlign = TextAlign.Center,
                        text = stringResource(id = item.label),
                        style = AppTheme.textStyles.small,
                        color = item.icon.tintColor,
                    )
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
private fun BottomNavigationWidgetPreview() =
    AppThemeComposable {
        Column {
            BottomNavigationWidget(
                navBottomItems = getUnauthenticatedBottomNavItems(),
            )
            Spacer(modifier = Modifier.padding(padding_screen))
            BottomNavigationWidget(
                navBottomItems = getMemberBottomNavItems(),
            )
            Spacer(modifier = Modifier.padding(padding_screen))
            BottomNavigationWidget(
                navBottomItems = getAdminBottomNavItems(),
            )
            Spacer(modifier = Modifier.padding(padding_screen))
            BottomNavigationWidget(
                navBottomItems = getSuperAdminBottomNavItems(),
            )
        }
    }
