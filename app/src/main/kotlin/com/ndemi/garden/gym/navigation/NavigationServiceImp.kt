package com.ndemi.garden.gym.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.toRoute
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
        this.initialRoute = getInitialRoute()
    }

    override fun open(route: Route) {
        if (route == getCurrentRoute()) {
            return
        }
        navController.navigate(route)
        analyticsRepository.logEvent(EVENT_NAVIGATE, PARAM_SCREEN_NAME, route.javaClass.simpleName)
    }

    override fun popBack() {
        navController.navigateUp()
        analyticsRepository.logEvent(EVENT_NAVIGATE, PARAM_SCREEN_NAME, getCurrentRoute().javaClass.simpleName)
    }

    override fun getCurrentRoute(): Route {
        val entry = navController.currentBackStackEntry ?: return getInitialRoute()
        val destination = entry.destination

        return appRoutes
            .find { destination.hasRoute(it::class) }
            ?.let { entry.toRoute(it::class) }
            ?: getInitialRoute()
    }

    override fun getInitialRoute(): Route =
        when {
            !permissionsUseCase.isAuthenticated() -> Route.LoginScreen
            permissionsUseCase.getMemberType() == MemberType.MEMBER -> Route.ProfileMemberScreen
            else -> Route.ProfileAdminScreen
        }

    override fun getBottomNavItems(): List<BottomNavItem> =
        when {
            !permissionsUseCase.isAuthenticated() -> getUnauthenticatedBottomNavItems()
            permissionsUseCase.getMemberType() == MemberType.MEMBER -> getMemberBottomNavItems()
            permissionsUseCase.getMemberType() == MemberType.ADMIN -> getAdminBottomNavItems()
            permissionsUseCase.getMemberType() == MemberType.SUPERVISOR -> getAdminBottomNavItems()
            permissionsUseCase.getMemberType() == MemberType.SUPER_ADMIN -> getSuperAdminBottomNavItems()
            else -> getUnauthenticatedBottomNavItems()
        }
}
