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
    data object ProfileSuperAdminScreen : Route()

    @Keep
    @Serializable
    data object AttendanceScreen : Route()

    @Keep
    @Serializable
    data object MembersScreen : Route()

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

    companion object {
        @Suppress("detekt.CyclomaticComplexMethod")
        fun String.toRoute(): Route {
            return when {
                this.contains(ResetPasswordScreen.javaClass.simpleName) -> ResetPasswordScreen
                this.contains(RegisterScreen.javaClass.simpleName) -> RegisterScreen
                this.contains(RegisterNewScreen.javaClass.simpleName) -> RegisterNewScreen
                this.contains(ProfileAdminScreen.javaClass.simpleName) -> ProfileAdminScreen
                this.contains(ProfileSuperAdminScreen.javaClass.simpleName) -> ProfileSuperAdminScreen
                this.contains(ProfileMemberScreen.javaClass.simpleName) -> ProfileMemberScreen
                this.contains(AttendanceScreen.javaClass.simpleName) -> AttendanceScreen
                this.contains(MembersScreen.javaClass.simpleName) -> MembersScreen
                this.contains(MembersActiveScreen.javaClass.simpleName) -> MembersActiveScreen
                this.contains(MembersExpiredScreen.javaClass.simpleName) -> MembersExpiredScreen
                this.contains(NonMembersScreen.javaClass.simpleName) -> NonMembersScreen
                this.contains("MembersAttendancesScreen") -> MembersAttendancesScreen()
                this.contains("PaymentsScreen") -> PaymentsScreen()
                this.contains("PaymentAddScreen") -> PaymentAddScreen()
                this.contains("MemberEditScreen") -> MemberEditScreen()
                else -> LoginScreen
            }
        }
    }
}
