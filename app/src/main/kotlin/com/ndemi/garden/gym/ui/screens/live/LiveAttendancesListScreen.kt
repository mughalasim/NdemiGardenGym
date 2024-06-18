package com.ndemi.garden.gym.ui.screens.live

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.MemberStatusWidget
import cv.domain.entities.MemberEntity
import cv.domain.entities.getMockMemberEntity

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
fun LiveAttendanceListScreenPreview() {
    AppThemeComposable {
        LiveAttendanceListScreen(
            members = listOf(
                getMockMemberEntity(),
                getMockMemberEntity(),
                getMockMemberEntity()
            )
        )
    }
}
