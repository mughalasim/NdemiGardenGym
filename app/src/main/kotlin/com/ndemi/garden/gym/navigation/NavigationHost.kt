package com.ndemi.garden.gym.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ndemi.garden.gym.di.CreateMember
import com.ndemi.garden.gym.di.RegisterMember
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.screens.attendance.AttendanceScreen
import com.ndemi.garden.gym.ui.screens.attendance.AttendanceScreenViewModel
import com.ndemi.garden.gym.ui.screens.login.LoginScreen
import com.ndemi.garden.gym.ui.screens.login.LoginScreenViewModel
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreen
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel
import com.ndemi.garden.gym.ui.screens.members.MemberScreenType
import com.ndemi.garden.gym.ui.screens.members.MembersActiveScreen
import com.ndemi.garden.gym.ui.screens.members.MembersExpiredScreen
import com.ndemi.garden.gym.ui.screens.members.MembersScreen
import com.ndemi.garden.gym.ui.screens.members.MembersScreenViewModel
import com.ndemi.garden.gym.ui.screens.members.NonMembersScreen
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreen
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel
import com.ndemi.garden.gym.ui.screens.payments.PaymentsScreen
import com.ndemi.garden.gym.ui.screens.payments.PaymentsScreenViewModel
import com.ndemi.garden.gym.ui.screens.profile.admin.ProfileAdminScreen
import com.ndemi.garden.gym.ui.screens.profile.admin.ProfileAdminScreenViewModel
import com.ndemi.garden.gym.ui.screens.profile.member.ProfileMemberScreen
import com.ndemi.garden.gym.ui.screens.profile.member.ProfileMemberScreenViewModel
import com.ndemi.garden.gym.ui.screens.register.RegisterNewScreen
import com.ndemi.garden.gym.ui.screens.register.RegisterScreen
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreen
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreenViewModel
import com.ndemi.garden.gym.ui.screens.settings.SettingsScreen
import com.ndemi.garden.gym.ui.screens.settings.SettingsScreenViewModel
import com.ndemi.garden.gym.ui.screens.weight.edit.WeightEditScreen
import com.ndemi.garden.gym.ui.screens.weight.edit.WeightEditScreenViewModel
import com.ndemi.garden.gym.ui.screens.weight.list.WeightListScreen
import com.ndemi.garden.gym.ui.screens.weight.list.WeightListScreenViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

@Composable
fun NavigationHost(
    navController: NavHostController,
    initialRoute: Route,
    showSnackbar: (AppSnackbarData) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = initialRoute,
    ) {
        composable<Route.LoginScreen> {
            LoginScreen(
                viewModel =
                    koinViewModel<LoginScreenViewModel>(
                        parameters = { parametersOf(showSnackbar) },
                    ),
            )
        }

        composable<Route.ResetPasswordScreen> {
            ResetPasswordScreen(
                viewModel =
                    koinViewModel<ResetPasswordScreenViewModel>(
                        parameters = { parametersOf(showSnackbar) },
                    ),
            )
        }

        composable<Route.RegisterScreen> {
            RegisterScreen(
                viewModel =
                    koinViewModel<RegisterScreenViewModel>(
                        qualifier = named<RegisterMember>(),
                        parameters = { parametersOf(showSnackbar) },
                    ),
            )
        }

        composable<Route.ProfileMemberScreen> {
            ProfileMemberScreen(
                viewModel =
                    koinViewModel<ProfileMemberScreenViewModel>(
                        parameters = { parametersOf(showSnackbar) },
                    ),
            )
        }

        composable<Route.AttendanceScreen> {
            AttendanceScreen(
                viewModel =
                    koinViewModel<AttendanceScreenViewModel>(
                        parameters = { parametersOf("", showSnackbar) },
                    ),
            )
        }

        composable<Route.RegisterNewScreen> {
            RegisterNewScreen(
                viewModel =
                    koinViewModel<RegisterScreenViewModel>(
                        qualifier = named<CreateMember>(),
                        parameters = { parametersOf(showSnackbar) },
                    ),
            )
        }

        composable<Route.ProfileAdminScreen> {
            ProfileAdminScreen(
                viewModel =
                    koinViewModel<ProfileAdminScreenViewModel>(
                        parameters = { parametersOf(showSnackbar) },
                    ),
            )
        }

        composable<Route.AllMembersScreen> {
            MembersScreen(
                viewModel =
                    koinViewModel<MembersScreenViewModel>(
                        parameters = { parametersOf(MemberScreenType.ALL_MEMBERS, showSnackbar) },
                    ),
            )
        }

        composable<Route.MembersActiveScreen> {
            MembersActiveScreen(
                viewModel =
                    koinViewModel<MembersScreenViewModel>(
                        parameters = { parametersOf(MemberScreenType.LIVE_MEMBERS, showSnackbar) },
                    ),
            )
        }

        composable<Route.MembersExpiredScreen> {
            MembersExpiredScreen(
                viewModel =
                    koinViewModel<MembersScreenViewModel>(
                        parameters = { parametersOf(MemberScreenType.EXPIRED_MEMBERS, showSnackbar) },
                    ),
            )
        }

        composable<Route.NonMembersScreen> {
            NonMembersScreen(
                viewModel =
                    koinViewModel<MembersScreenViewModel>(
                        parameters = { parametersOf(MemberScreenType.NON_MEMBERS, showSnackbar) },
                    ),
            )
        }

        composable<Route.SettingsScreen> {
            SettingsScreen(
                viewModel =
                    koinViewModel<SettingsScreenViewModel>(
                        parameters = { parametersOf(showSnackbar) },
                    ),
            )
        }

        composable<Route.WeightListScreen> {
            WeightListScreen(
                viewModel =
                    koinViewModel<WeightListScreenViewModel>(
                        parameters = { parametersOf(showSnackbar) },
                    ),
            )
        }

        composable<Route.MemberEditScreen> {
            val args = it.toRoute<Route.MemberEditScreen>()
            MemberEditScreen(
                memberId = args.memberId,
                viewModel =
                    koinViewModel<MemberEditScreenViewModel>(
                        parameters = { parametersOf(args.memberId, showSnackbar) },
                    ),
            )
        }

        composable<Route.WeightEditScreen> {
            val args = it.toRoute<Route.WeightEditScreen>()
            WeightEditScreen(
                viewModel =
                    koinViewModel<WeightEditScreenViewModel>(
                        parameters = { parametersOf(args.weightId, args.weight, args.dateMillis, showSnackbar) },
                    ),
            )
        }

        composable<Route.MembersAttendancesScreen> {
            val args = it.toRoute<Route.MembersAttendancesScreen>()
            AttendanceScreen(
                memberName = args.memberName,
                viewModel =
                    koinViewModel<AttendanceScreenViewModel>(parameters = { parametersOf(args.memberId, showSnackbar) }),
            )
        }

        composable<Route.PaymentsScreen> {
            val args = it.toRoute<Route.PaymentsScreen>()
            PaymentsScreen(
                memberId = args.memberId,
                memberName = args.memberName,
                viewModel =
                    koinViewModel<PaymentsScreenViewModel>(parameters = { parametersOf(args.memberId, showSnackbar) }),
            )
        }

        composable<Route.PaymentAddScreen> {
            val args = it.toRoute<Route.PaymentAddScreen>()
            PaymentAddScreen(
                viewModel =
                    koinViewModel<PaymentAddScreenViewModel>(parameters = { parametersOf(args.memberId, showSnackbar) }),
            )
        }
    }
}
