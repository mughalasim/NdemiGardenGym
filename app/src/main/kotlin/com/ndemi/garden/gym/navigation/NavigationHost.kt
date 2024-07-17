package com.ndemi.garden.gym.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ndemi.garden.gym.ui.screens.attendance.AttendanceScreen
import com.ndemi.garden.gym.ui.screens.live.LiveAttendanceScreen
import com.ndemi.garden.gym.ui.screens.login.LoginScreen
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreen
import com.ndemi.garden.gym.ui.screens.members.MembersActiveScreen
import com.ndemi.garden.gym.ui.screens.members.MembersExpiredScreen
import com.ndemi.garden.gym.ui.screens.members.MembersScreen
import com.ndemi.garden.gym.ui.screens.membersattendances.MembersAttendancesScreen
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreen
import com.ndemi.garden.gym.ui.screens.payments.PaymentsScreen
import com.ndemi.garden.gym.ui.screens.profile.ProfileScreen
import com.ndemi.garden.gym.ui.screens.register.RegisterNewScreen
import com.ndemi.garden.gym.ui.screens.register.RegisterScreen
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreen

@Composable
fun NavigationHost(
    navController: NavHostController,
    navigationService: NavigationService,
) {
    NavHost(
        navController = navController,
        startDestination = navigationService.getInitialRoute(),
    ) {
        composable<Route.LoginScreen> { LoginScreen() }

        composable<Route.ResetPasswordScreen> { ResetPasswordScreen() }

        composable<Route.RegisterScreen> { RegisterScreen() }

        composable<Route.RegisterNewScreen> { RegisterNewScreen() }

        composable<Route.ProfileScreen> { ProfileScreen() }

        composable<Route.AttendanceScreen> { AttendanceScreen() }

        composable<Route.LiveAttendanceScreen> { LiveAttendanceScreen() }

        composable<Route.MembersScreen> { MembersScreen() }

        composable<Route.MembersActiveScreen> { MembersActiveScreen() }

        composable<Route.MembersExpiredScreen> { MembersExpiredScreen() }

        composable<Route.MemberEditScreen> {
            val args = it.toRoute<Route.MemberEditScreen>()
            MemberEditScreen(args.memberId)
        }

        composable<Route.MembersAttendancesScreen> {
            val args = it.toRoute<Route.MembersAttendancesScreen>()
            MembersAttendancesScreen(args.memberId, args.memberName)
        }

        composable<Route.PaymentsScreen> {
            val args = it.toRoute<Route.PaymentsScreen>()
            PaymentsScreen(args.memberId, args.memberName)
        }

        composable<Route.PaymentAddScreen> {
            val args = it.toRoute<Route.PaymentAddScreen>()
           PaymentAddScreen(args.memberId)
        }
    }
}
