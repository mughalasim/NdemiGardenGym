package com.ndemi.garden.gym.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ndemi.garden.gym.ui.screens.attendance.AttendanceScreen
import com.ndemi.garden.gym.ui.screens.login.LoginScreen
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreen
import com.ndemi.garden.gym.ui.screens.members.MembersActiveScreen
import com.ndemi.garden.gym.ui.screens.members.MembersExpiredScreen
import com.ndemi.garden.gym.ui.screens.members.MembersScreen
import com.ndemi.garden.gym.ui.screens.members.NonMembersScreen
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreen
import com.ndemi.garden.gym.ui.screens.payments.PaymentsScreen
import com.ndemi.garden.gym.ui.screens.profile.admin.ProfileAdminScreen
import com.ndemi.garden.gym.ui.screens.profile.member.ProfileMemberScreen
import com.ndemi.garden.gym.ui.screens.profile.superadmin.ProfileSuperAdminScreen
import com.ndemi.garden.gym.ui.screens.register.RegisterNewScreen
import com.ndemi.garden.gym.ui.screens.register.RegisterScreen
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreen
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState

@Composable
fun NavigationHost(
    navController: NavHostController,
    initialRoute: Route,
    snackbarHostState: AppSnackbarHostState,
) {
    NavHost(
        navController = navController,
        startDestination = initialRoute,
    ) {
        composable<Route.LoginScreen> { LoginScreen(snackbarHostState = snackbarHostState) }

        composable<Route.ResetPasswordScreen> { ResetPasswordScreen(snackbarHostState = snackbarHostState) }

        composable<Route.RegisterScreen> { RegisterScreen(snackbarHostState = snackbarHostState) }

        composable<Route.RegisterNewScreen> { RegisterNewScreen(snackbarHostState = snackbarHostState) }

        composable<Route.ProfileAdminScreen> { ProfileAdminScreen(snackbarHostState = snackbarHostState) }

        composable<Route.ProfileSuperAdminScreen> { ProfileSuperAdminScreen(snackbarHostState = snackbarHostState) }

        composable<Route.ProfileMemberScreen> { ProfileMemberScreen(snackbarHostState = snackbarHostState) }

        composable<Route.AttendanceScreen> { AttendanceScreen(snackbarHostState = snackbarHostState) }

        composable<Route.MembersScreen> { MembersScreen(snackbarHostState = snackbarHostState) }

        composable<Route.MembersActiveScreen> { MembersActiveScreen(snackbarHostState = snackbarHostState) }

        composable<Route.MembersExpiredScreen> { MembersExpiredScreen(snackbarHostState = snackbarHostState) }

        composable<Route.NonMembersScreen> { NonMembersScreen(snackbarHostState = snackbarHostState) }

        composable<Route.MemberEditScreen> {
            val args = it.toRoute<Route.MemberEditScreen>()
            MemberEditScreen(
                memberId = args.memberId,
                snackbarHostState = snackbarHostState,
            )
        }

        composable<Route.MembersAttendancesScreen> {
            val args = it.toRoute<Route.MembersAttendancesScreen>()
            AttendanceScreen(
                memberId = args.memberId,
                memberName = args.memberName,
                snackbarHostState = snackbarHostState,
            )
        }

        composable<Route.PaymentsScreen> {
            val args = it.toRoute<Route.PaymentsScreen>()
            PaymentsScreen(
                memberId = args.memberId,
                memberName = args.memberName,
                snackbarHostState = snackbarHostState,
            )
        }

        composable<Route.PaymentAddScreen> {
            val args = it.toRoute<Route.PaymentAddScreen>()
            PaymentAddScreen(
                memberId = args.memberId,
                snackbarHostState = snackbarHostState,
            )
        }
    }
}
