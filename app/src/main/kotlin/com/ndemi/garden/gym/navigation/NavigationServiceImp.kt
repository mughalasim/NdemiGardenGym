package com.ndemi.garden.gym.navigation

import androidx.annotation.Keep
import androidx.navigation.NavController
import com.ndemi.garden.gym.navigation.Route.Companion.toRoute
import cv.domain.Variables.EVENT_NAVIGATE
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
        val initialRoute = Route.getInitialRoute(authUseCase.isAuthenticated(), authUseCase.isNotMember())
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
        navController.currentDestination?.route?.toRoute() ?:
        Route.getInitialRoute(authUseCase.isAuthenticated(), authUseCase.isNotMember())

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
    data class PaymentsScreen(
        val memberId: String = "",
        val memberName: String = "",
    ) : Route()

    @Keep
    @Serializable
    data class PaymentAddScreen(
        val memberId: String,
    ) : Route()

    @Keep
    @Serializable
    data class MemberEditScreen(
        val memberId: String,
    ) : Route()

    companion object {
        fun getInitialRoute(isAuthenticated: Boolean, isAdmin: Boolean): Route =
            if (isAuthenticated && isAdmin) {
                MembersScreen
            } else if (isAuthenticated){
                ProfileScreen
            } else {
                LoginScreen
            }

        fun String.toRoute(): Route {
            return when {
                this.contains(ResetPasswordScreen.javaClass.simpleName) -> ResetPasswordScreen
                this.contains(RegisterScreen.javaClass.simpleName)-> RegisterScreen
                this.contains(RegisterNewScreen.javaClass.simpleName) -> RegisterNewScreen
                this.contains(ProfileScreen.javaClass.simpleName) -> ProfileScreen
                this.contains(LiveAttendanceScreen.javaClass.simpleName) -> LiveAttendanceScreen
                this.contains(AttendanceScreen.javaClass.simpleName) -> AttendanceScreen
                this.contains(MembersScreen.javaClass.simpleName) -> MembersScreen
                this.contains("MembersAttendancesScreen") -> MembersAttendancesScreen("", "")
                this.contains("PaymentsScreen") -> PaymentsScreen()
                this.contains("PaymentAddScreen") -> PaymentAddScreen("")
                this.contains("MemberEditScreen") -> MemberEditScreen("")
                else  -> LoginScreen
            }
        }
    }
}
