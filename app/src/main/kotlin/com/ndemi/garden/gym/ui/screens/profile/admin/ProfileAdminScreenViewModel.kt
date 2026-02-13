package com.ndemi.garden.gym.ui.screens.profile.admin

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.enums.SnackbarType
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.AdminDashboard
import cv.domain.entities.MemberEntity
import cv.domain.enums.MemberUpdateType
import cv.domain.usecase.AccessUseCase
import cv.domain.usecase.AdminDashboardUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.StorageUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProfileAdminScreenViewModel(
    private val job: MutableList<Job>,
    private val converter: ErrorCodeConverter,
    private val adminDashboardUseCase: AdminDashboardUseCase,
    private val accessUseCase: AccessUseCase,
    private val memberUseCase: MemberUseCase,
    private val storageUseCase: StorageUseCase,
    private val navigationService: NavigationService,
) : BaseViewModel<ProfileAdminScreenViewModel.UiState, ProfileAdminScreenViewModel.Action>(UiState.Loading) {
    private val memberEntity = MutableStateFlow(MemberEntity())

    init {
        sendAction(Action.Loading)
        job +=
            viewModelScope.launch {
                adminDashboardUseCase.invoke().collect {
                    memberEntity.value = it.memberEntity
                    sendAction(Action.Success(it))
                }
            }
    }

    fun onEditDetailsTapped() {
        navigationService.open(Route.MemberEditScreen(memberId = memberEntity.value.id))
    }

    fun onLogOutTapped() {
        accessUseCase.logOut()
    }

    fun onImageDeleted() {
        viewModelScope.launch {
            memberUseCase
                .updateMember(
                    memberEntity.value.copy(profileImageUrl = ""),
                    MemberUpdateType.PHOTO_DELETE,
                )
        }
    }

    fun updateMemberImage(byteArray: ByteArray) {
        viewModelScope.launch {
            val snackbarState: Pair<SnackbarType, String> =
                when (val result = storageUseCase.updateImageForMember(memberEntity.value, byteArray)) {
                    is DomainResult.Error -> Pair(SnackbarType.ERROR, converter.getMessage(result.error))
                    else -> Pair(SnackbarType.SUCCESS, "Successfully updated")
                }
            showSnackbar(snackbarState.first, snackbarState.second)
        }
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data class Success(
            val adminDashboard: AdminDashboard,
        ) : UiState
    }

    sealed interface Action : BaseAction<UiState> {
        data object Loading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class Success(
            val adminDashboard: AdminDashboard,
        ) : Action {
            override fun reduce(state: UiState): UiState =
                UiState.Success(
                    adminDashboard = adminDashboard,
                )
        }
    }
}
