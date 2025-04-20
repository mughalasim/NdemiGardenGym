package com.ndemi.garden.gym.ui.screens.live

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
fun LiveAttendanceListScreen(members: List<MemberEntity>) {
    Column {
        repeat(members.size) {
            MemberStatusWidget(memberEntity = members[it])
        }
    }
}

@AppPreview
@Composable
private fun LiveAttendanceListScreenPreview() {
    AppThemeComposable {
        LiveAttendanceListScreen(
            members =
                listOf(
                    getMockRegisteredMemberEntity(),
                    getMockActiveMemberEntity(),
                    getMockExpiredMemberEntity(),
                ),
        )
    }
}
