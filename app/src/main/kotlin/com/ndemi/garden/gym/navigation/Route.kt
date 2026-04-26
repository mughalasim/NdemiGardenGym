package com.ndemi.garden.gym.navigation

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

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
    data object ProfileMemberScreen : Route()

    @Keep
    @Serializable
    data object ProfileAdminScreen : Route()

    @Keep
    @Serializable
    data object AttendanceScreen : Route()

    @Keep
    @Serializable
    data object AllMembersScreen : Route()

    @Keep
    @Serializable
    data object MembersExpiredScreen : Route()

    @Keep
    @Serializable
    data object NonMembersScreen : Route()

    @Keep
    @Serializable
    data object MembersActiveScreen : Route()

    @Keep
    @Serializable
    data object SettingsScreen : Route()

    @Keep
    @Serializable
    data class MembersAttendancesScreen(
        val memberId: String = "",
        val memberName: String = "",
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
        val memberId: String = "",
    ) : Route()

    @Keep
    @Serializable
    data class MemberEditScreen(
        val memberId: String = "",
    ) : Route()

    @Keep
    @Serializable
    data object WeightListScreen : Route()

    @Keep
    @Serializable
    data class WeightEditScreen(
        val weightId: String = "",
        val weight: String = "",
        val dateMillis: Long = 0L,
    ) : Route()
}
