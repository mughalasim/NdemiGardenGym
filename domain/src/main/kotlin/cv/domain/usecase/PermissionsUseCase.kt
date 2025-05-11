package cv.domain.usecase

import cv.domain.entities.MemberType
import cv.domain.repositories.AuthRepository

// TODO - Return one permission data class that can be used across any viewModel
class PermissionsUseCase(
    private val authRepository: AuthRepository,
) {
    fun isAuthenticated() = authRepository.isAuthenticated()

    fun isNotMember() =
        authRepository.getMemberType() == MemberType.ADMIN ||
            authRepository.getMemberType() == MemberType.SUPERVISOR

    fun hasAdminRights() = authRepository.getMemberType() == MemberType.ADMIN

    fun canEditMember(memberId: String) = memberId.isEmpty() || memberId == authRepository.getMemberId() || hasAdminRights()

    fun canDeleteMember() = hasAdminRights()

    fun canUpdatePayment() = hasAdminRights()

    fun canDeleteAttendance(memberId: String) = memberId.isEmpty() || memberId == authRepository.getMemberId() || hasAdminRights()
}
