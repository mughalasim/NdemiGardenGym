package com.ndemi.garden.gym.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContactMail
import androidx.compose.material.icons.rounded.FormatListNumbered
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.InsertChartOutlined
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.ndemi.garden.gym.R

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

    data object ProfileSuperAdminScreen : BottomNavItem(
        Route.ProfileSuperAdminScreen,
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

    data object NonMembersScreen : BottomNavItem(
        Route.NonMembersScreen,
        Icons.Rounded.FormatListNumbered,
        R.string.txt_members,
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
}
