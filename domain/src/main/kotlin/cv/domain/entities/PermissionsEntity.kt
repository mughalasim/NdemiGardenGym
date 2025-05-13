package cv.domain.entities

data class PermissionsEntity(
    val canAddMember: Boolean = false,
    val canViewMemberDetails: Boolean = false,
    val canViewMemberStats: Boolean = false,
    val canAssignCoach: Boolean = false,
    val canEditMember: Boolean = false,
    val canDeleteMember: Boolean = false,
    val canUpdatePayment: Boolean = false,
    val canDeleteAttendance: Boolean = false,
)

internal fun getAdminPermissions() =
    PermissionsEntity(
        canAddMember = true,
        canViewMemberDetails = true,
        canViewMemberStats = true,
        canAssignCoach = true,
        canEditMember = true,
        canDeleteMember = true,
        canUpdatePayment = true,
        canDeleteAttendance = true,
    )

internal fun getSupervisorPermissions() =
    PermissionsEntity(
        canAddMember = false,
        canViewMemberDetails = true,
        canViewMemberStats = true,
        canAssignCoach = false,
        canEditMember = false,
        canDeleteMember = false,
        canUpdatePayment = false,
        canDeleteAttendance = false,
    )
