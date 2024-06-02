package com.ndemi.garden.gym.navigation

import androidx.navigation.NavController
import cv.domain.Variables.EVENT_NAME_NAVIGATE
import cv.domain.Variables.PARAM_SCREEN_NAME
import cv.domain.repositories.AnalyticsRepository
import com.ndemi.garden.gym.ui.utils.toRoute

class NavigationServiceImp(
    private val analyticsRepository: AnalyticsRepository,
) : NavigationService {
    private lateinit var navController: NavController
    private lateinit var initialRoute: Route

    override fun setNavController(navController: NavController) {
        this.navController = navController
        val initialRoute = Route.getInitialRoute()
        this.initialRoute = initialRoute
    }

    override fun open(
        route: Route,
        removeCurrentFromStack: Boolean,
    ) {
        if (route == getCurrentRoute()) {
            return
        }
        navController.navigate(route.routeName) {
            if (removeCurrentFromStack) {
                popUpTo(getCurrentRoute().routeName) {
                    inclusive = true
                }
            }
        }
        analyticsRepository.logEvent(
            EVENT_NAME_NAVIGATE,
            listOf(
                Pair(PARAM_SCREEN_NAME, route.routeName),
            ),
        )
    }

    override fun popBack() {
        navController.popBackStack()
        analyticsRepository.logEvent(
            EVENT_NAME_NAVIGATE,
            listOf(
                Pair(PARAM_SCREEN_NAME, getCurrentRoute().routeName),
            ),
        )
    }

    override fun getCurrentRoute(): Route = navController.currentDestination?.route?.toRoute() ?: Route.LoginScreen

    override fun getInitialRoute(): Route = initialRoute
}

sealed class Route(val routeName: String, val isInitialRoute: Boolean = false) {

    data object MainScreen : Route("MainScreen")

    data object LoginScreen : Route("LoginScreen")
    data object ResetPasswordScreen : Route("ResetPasswordScreen")
    data object RegisterScreen : Route("RegisterScreen")
    data object ProfileScreen : Route("ProfileScreen")
    data object AttendanceScreen : Route("AttendanceScreen")
    data object LiveAttendanceScreen : Route("LiveAttendanceScreen")
    data object MembersScreen : Route("MembersScreen")
    data object MemberEditScreen : Route("MemberEditScreen")
    data object MembersAttendancesScreen : Route("MembersAttendancesScreen")

    companion object {
        fun getInitialRoute(): Route =
            Route::class.sealedSubclasses
                .firstOrNull { it.objectInstance?.isInitialRoute == true }
                ?.objectInstance
                ?: LoginScreen
    }
}
