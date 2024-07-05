package com.ndemi.garden.gym.ui.screens.members

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.ndemi.garden.gym.ui.mock.getMockActiveMemberEntity
import com.ndemi.garden.gym.ui.mock.getMockExpiredMemberEntity
import com.ndemi.garden.gym.ui.mock.getMockRegisteredMemberEntity
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.member.MemberStatusWidget
import cv.domain.entities.MemberEntity

@Composable
fun MembersListScreen(
    members: List<MemberEntity>,
    onMemberTapped: (memberEntity: MemberEntity) -> Unit = {},
    onPaymentsTapped: (memberEntity: MemberEntity) -> Unit = {},
    onAttendanceTapped: (memberEntity: MemberEntity) -> Unit = {},
) {
    Column {
        repeat(members.size) {
            MemberStatusWidget(
                memberEntity = members[it],
                showDetails = true,
                onMemberTapped = onMemberTapped,
                onPaymentsTapped = onPaymentsTapped,
                onAttendanceTapped = onAttendanceTapped
            )
        }
    }
}

@AppPreview
@Composable
fun MembersListScreenPreview() {
    AppThemeComposable {
        MembersListScreen(
            members =
            listOf(
                getMockRegisteredMemberEntity(),
                getMockActiveMemberEntity(),
                getMockExpiredMemberEntity(),
            )
        )
    }
}
