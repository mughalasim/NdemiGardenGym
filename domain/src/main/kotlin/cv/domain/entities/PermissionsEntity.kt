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
    val canSetMemberType: Boolean = false,
)

fun getSuperAdminPermissions() =
    PermissionsEntity(
        canAddMember = true,
        canViewMemberDetails = true,
        canViewMemberStats = true,
        canAssignCoach = true,
        canEditMember = true,
        canDeleteMember = true,
        canUpdatePayment = true,
        canDeleteAttendance = true,
        canSetMemberType = true,
    )

fun getAdminPermissions() =
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

fun getSupervisorPermissions() =
    PermissionsEntity(
        canViewMemberDetails = true,
        canViewMemberStats = true,
    )
