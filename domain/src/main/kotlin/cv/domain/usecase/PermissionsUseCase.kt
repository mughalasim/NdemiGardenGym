package cv.domain.usecase

import cv.domain.entities.PermissionsEntity
import cv.domain.entities.getAdminPermissions
import cv.domain.entities.getSupervisorPermissions
import cv.domain.enums.MemberType
import cv.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PermissionsUseCase(
    private val authRepository: AuthRepository,
) {
    fun isAuthenticated() = authRepository.isAuthenticated()

    fun getMemberType() = authRepository.getMemberType()

    fun getPermissions(memberId: String = ""): StateFlow<PermissionsEntity> {
        val memberType = authRepository.getMemberType()
        val canUpdateSelf = memberId.isEmpty() || memberId == authRepository.getMemberId()

        val permission =
            when (memberType) {
                MemberType.SUPER_ADMIN, MemberType.ADMIN -> getAdminPermissions()
                MemberType.SUPERVISOR -> getSupervisorPermissions()
                MemberType.MEMBER ->
                    getSupervisorPermissions().copy(
                        canEditMember = canUpdateSelf,
                        canDeleteAttendance = canUpdateSelf,
                    )
            }
        return MutableStateFlow(permission)
    }
}
