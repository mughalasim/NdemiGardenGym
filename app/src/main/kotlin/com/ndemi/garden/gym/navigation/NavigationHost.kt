package com.ndemi.garden.gym.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ndemi.garden.gym.ui.screens.attendance.AttendanceScreen
import com.ndemi.garden.gym.ui.screens.live.LiveAttendanceScreen
import com.ndemi.garden.gym.ui.screens.login.LoginScreen
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreen
import com.ndemi.garden.gym.ui.screens.members.MembersScreen
import com.ndemi.garden.gym.ui.screens.membersattendances.MembersAttendancesScreen
import com.ndemi.garden.gym.ui.screens.profile.ProfileScreen
import com.ndemi.garden.gym.ui.screens.register.RegisterScreen
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreen

@Composable
fun NavigationHost(
    navController: NavHostController,
    navigationService: NavigationService,
) {
    NavHost(
        navController = navController,
        startDestination = navigationService.getInitialRoute().routeName,
    ) {
        composable(route = Route.LoginScreen.routeName) { LoginScreen() }
        composable(route = Route.ResetPasswordScreen.routeName) { ResetPasswordScreen() }
        composable(route = Route.RegisterScreen.routeName) { RegisterScreen() }
        composable(route = Route.ProfileScreen.routeName) { ProfileScreen() }
        composable(route = Route.AttendanceScreen.routeName) { AttendanceScreen() }
        composable(route = Route.LiveAttendanceScreen.routeName) { LiveAttendanceScreen() }
        composable(route = Route.MembersScreen.routeName) { MembersScreen() }
        composable(route = Route.MemberEditScreen.routeName) { MemberEditScreen() }
        composable(route = Route.MembersAttendancesScreen.routeName) { MembersAttendancesScreen() }
    }
}
