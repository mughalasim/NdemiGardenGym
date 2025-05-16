package com.ndemi.garden.gym.navigation

import androidx.navigation.NavController
import com.ndemi.garden.gym.navigation.Route.Companion.toRoute
import cv.domain.Variables.EVENT_NAVIGATE
import cv.domain.Variables.PARAM_SCREEN_NAME
import cv.domain.enums.MemberType
import cv.domain.repositories.AnalyticsRepository
import cv.domain.usecase.PermissionsUseCase

class NavigationServiceImp(
    private val analyticsRepository: AnalyticsRepository,
    private val permissionsUseCase: PermissionsUseCase,
) : NavigationService {
    private lateinit var navController: NavController
    private lateinit var initialRoute: Route

    override fun setNavController(navController: NavController) {
        this.navController = navController
        val initialRoute = processInitialRoute()
        this.initialRoute = initialRoute
    }

    override fun open(route: Route, removeCurrentFromStack: Boolean) {
        if (route == getCurrentRoute()) {
            return
        }
        navController.navigate(route) {
            if (removeCurrentFromStack) {
                popUpTo(getCurrentRoute()) {
                    inclusive = true
                }
            }
        }
        analyticsRepository.logEvent(
            EVENT_NAVIGATE,
            listOf(
                Pair(PARAM_SCREEN_NAME, route.javaClass.simpleName),
            ),
        )
    }

    override fun popBack() {
        navController.navigateUp()
        analyticsRepository.logEvent(
            EVENT_NAVIGATE,
            listOf(
                Pair(PARAM_SCREEN_NAME, getCurrentRoute().javaClass.simpleName),
            ),
        )
    }

    override fun getCurrentRoute(): Route =
        navController.currentDestination?.route?.toRoute()
            ?: processInitialRoute()

    override fun getInitialRoute(): Route = initialRoute

    private fun processInitialRoute(): Route =
        if (!permissionsUseCase.isAuthenticated()) {
            Route.LoginScreen
        } else {
            when (permissionsUseCase.getMemberType()) {
                MemberType.MEMBER ->
                    Route.ProfileMemberScreen

                MemberType.ADMIN, MemberType.SUPERVISOR ->
                    Route.MembersScreen

                MemberType.SUPER_ADMIN ->
                    Route.ProfileSuperAdminScreen
            }
        }

    override fun getBottomNavItems(): List<BottomNavItem> =
        if (!permissionsUseCase.isAuthenticated()) {
            listOf(
                BottomNavItem.LoginScreen,
                BottomNavItem.RegisterScreen,
                BottomNavItem.ResetPasswordScreen,
            )
        } else {
            when (permissionsUseCase.getMemberType()) {
                MemberType.MEMBER -> listOf(
                    BottomNavItem.ProfileMemberScreen,
                    BottomNavItem.AttendanceScreen,
                    BottomNavItem.PaymentsScreen,
                    BottomNavItem.LiveAttendanceScreen,
                )
                MemberType.ADMIN, MemberType.SUPERVISOR ->
                    listOf(
                        BottomNavItem.MembersScreen,
                        BottomNavItem.MembersExpiredScreen,
                        BottomNavItem.MembersActiveScreen,
                        BottomNavItem.ProfileAdminScreen,
                    )
                MemberType.SUPER_ADMIN ->
                    listOf(
                        BottomNavItem.ProfileSuperAdminScreen,
                        BottomNavItem.NonMembersScreen,
                    )
            }
        }
}
