package com.ndemi.garden.gym.ui.screens.members

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.MemberStatusWidget
import cv.domain.entities.MemberEntity
import cv.domain.entities.getMockMemberEntity

@Composable
fun MembersListScreen(
    members: List<MemberEntity>,
    onMemberTapped:(memberEntity: MemberEntity) -> Unit = {}
) {
    Column {
        repeat(members.size) {
            MemberStatusWidget(
                memberEntity = members[it],
                showDetails = true,
                onMemberTapped = onMemberTapped
            )
        }
    }
}

@AppPreview
@Composable
fun MembersListScreenPreview(){
    AppThemeComposable {
        MembersListScreen(members = listOf(
            getMockMemberEntity(),
            getMockMemberEntity(),
            getMockMemberEntity()
        ))
    }
}
