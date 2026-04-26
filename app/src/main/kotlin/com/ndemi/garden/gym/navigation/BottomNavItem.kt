package com.ndemi.garden.gym.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.ContactMail
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.InsertChartOutlined
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.NoAccounts
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Security
import androidx.compose.ui.graphics.vector.ImageVector
import com.ndemi.garden.gym.R

sealed class BottomNavItem(
    val route: Route,
    val icon: ImageVector,
    val label: Int,
) {
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
        Icons.Rounded.Dashboard,
        R.string.txt_dashboard,
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

    data object PaymentsScreen : BottomNavItem(
        Route.PaymentsScreen(),
        Icons.Rounded.MonetizationOn,
        R.string.txt_payments,
    )

    data object AllMembersScreen : BottomNavItem(
        Route.AllMembersScreen,
        Icons.Rounded.Group,
        R.string.txt_all_members,
    )

    data object NonMembersScreen : BottomNavItem(
        Route.NonMembersScreen,
        Icons.Rounded.Security,
        R.string.txt_management,
    )

    data object MembersExpiredScreen : BottomNavItem(
        Route.MembersExpiredScreen,
        Icons.Rounded.NoAccounts,
        R.string.txt_inactive,
    )

    data object MembersActiveScreen : BottomNavItem(
        Route.MembersActiveScreen,
        Icons.AutoMirrored.Rounded.DirectionsRun,
        R.string.txt_in_the_gym,
    )
}
