package com.ndemi.garden.gym.navigation

import androidx.annotation.Keep
import androidx.navigation.NavController
import com.ndemi.garden.gym.ui.utils.toRoute
import cv.domain.Variables.EVENT_NAME_NAVIGATE
import cv.domain.Variables.PARAM_SCREEN_NAME
import cv.domain.repositories.AnalyticsRepository
import cv.domain.usecase.AuthUseCase
import kotlinx.serialization.Serializable

class NavigationServiceImp(
    private val analyticsRepository: AnalyticsRepository,
    private val authUseCase: AuthUseCase,
) : NavigationService {
    private lateinit var navController: NavController
    private lateinit var initialRoute: Route

    override fun setNavController(navController: NavController) {
        this.navController = navController
        val initialRoute = Route.getInitialRoute(authUseCase.isAuthenticated())
        this.initialRoute = initialRoute
    }

    override fun open(
        route: Route,
        removeCurrentFromStack: Boolean,
    ) {
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
            EVENT_NAME_NAVIGATE,
            listOf(
                Pair(PARAM_SCREEN_NAME, route.toString()),
            ),
        )
    }

    override fun popBack() {
        navController.navigateUp()
        analyticsRepository.logEvent(
            EVENT_NAME_NAVIGATE,
            listOf(
                Pair(PARAM_SCREEN_NAME, getCurrentRoute().toString()),
            ),
        )
    }

    override fun getCurrentRoute(): Route =
        navController.currentDestination?.route?.toRoute() ?:
        Route.getInitialRoute(authUseCase.isAuthenticated())

    override fun getInitialRoute(): Route = initialRoute
}

@Keep
@Serializable
sealed class Route {
    @Keep
    @Serializable
    data object LoginScreen : Route()

    @Keep
    @Serializable
    data object ResetPasswordScreen : Route()

    @Keep
    @Serializable
    data object RegisterScreen : Route()

    @Keep
    @Serializable
    data object RegisterNewScreen : Route()

    @Keep
    @Serializable
    data object ProfileScreen : Route()

    @Keep
    @Serializable
    data object AttendanceScreen : Route()

    @Keep
    @Serializable
    data object LiveAttendanceScreen : Route()

    @Keep
    @Serializable
    data object MembersScreen : Route()

    @Keep
    @Serializable
    data class MembersAttendancesScreen(
        val memberId: String,
        val memberName: String,
    ) : Route()

    @Keep
    @Serializable
    data class MemberEditScreen(
        val memberId: String,
    ) : Route()

    companion object {
        fun getInitialRoute(isAuthenticated: Boolean): Route =
            if (isAuthenticated) ProfileScreen else LoginScreen
    }
}
