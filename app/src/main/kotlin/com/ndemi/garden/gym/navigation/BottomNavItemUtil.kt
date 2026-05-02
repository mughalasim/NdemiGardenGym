package com.ndemi.garden.gym.navigation

fun getUnauthenticatedBottomNavItems(): List<BottomNavItem> =
    listOf(
        BottomNavItem.LoginScreen,
        BottomNavItem.RegisterScreen,
        BottomNavItem.ResetPasswordScreen,
    )

fun getMemberBottomNavItems(): List<BottomNavItem> =
    listOf(
        BottomNavItem.ProfileMemberScreen,
        BottomNavItem.AttendanceScreen,
        BottomNavItem.PaymentsScreen,
        BottomNavItem.MembersActiveScreen,
    )

fun getAdminBottomNavItems(): List<BottomNavItem> =
    listOf(
        BottomNavItem.ProfileAdminScreen,
        BottomNavItem.AllMembersScreen,
        BottomNavItem.MembersExpiredScreen,
        BottomNavItem.MembersActiveScreen,
    )

fun getSuperAdminBottomNavItems(): List<BottomNavItem> =
    listOf(
        BottomNavItem.ProfileAdminScreen,
        BottomNavItem.NonMembersScreen,
        BottomNavItem.AllMembersScreen,
    )
