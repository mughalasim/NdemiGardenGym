package com.ndemi.garden.gym.ui.screens.members

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.widgets.MemberStatusWidget
import cv.domain.entities.MemberEntity
import cv.domain.entities.getMockMemberEntity

@Composable
fun MembersListScreen(
    members: List<MemberEntity>,
    onMemberTapped:(memberEntity: MemberEntity) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(padding_screen)
    ) {
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
