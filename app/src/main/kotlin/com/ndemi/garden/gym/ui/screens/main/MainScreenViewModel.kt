package com.ndemi.garden.gym.ui.screens.main

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.ndemi.garden.gym.navigation.BottomNavItem
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.Route
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import com.ndemi.garden.gym.ui.widgets.AppSnackbarHostState
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.usecase.AccessUseCase
import cv.domain.usecase.AuthUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val job: MutableList<Job>,
    private val navigationService: NavigationService,
    private val authUseCase: AuthUseCase,
    private val accessUseCase: AccessUseCase,
    private val converter: ErrorCodeConverter,
) : ViewModel() {
    private val authState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Loading)
    private val versionState: MutableStateFlow<VersionState> = MutableStateFlow(VersionState.Loading)
    private val memberState: MutableStateFlow<MemberState> = MutableStateFlow(MemberState.Loading)
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _emailVerifiedState: MutableStateFlow<EmailVerifiedState> = MutableStateFlow(EmailVerifiedState.Hidden)
    val emailVerifiedState: StateFlow<EmailVerifiedState> = _emailVerifiedState

    val snackbarHostState: AppSnackbarHostState = AppSnackbarHostState()

    init {
        combine(
            authState,
            versionState,
            memberState,
        ) { auth, version, member ->
            _emailVerifiedState.value = EmailVerifiedState.Hidden
            when {
                version is VersionState.UpdateRequired ->
                    _uiState.value = UiState.UpdateRequired(version.url)

                auth == AuthState.Authorised && member is MemberState.Authenticated -> {
                    if (!member.member.emailVerified) {
                        _emailVerifiedState.value = EmailVerifiedState.Visible
                    }
                    _uiState.value =
                        UiState.Ready(
                            initialRoute = navigationService.getInitialRoute(),
                            bottomNavItems = navigationService.getBottomNavItems(),
                        )
                }

                auth == AuthState.Authorised && member is MemberState.UserNotFound ->
                    _uiState.value = UiState.UserNotFound(member.message)

                auth == AuthState.UnAuthorised ->
                    _uiState.value =
                        UiState.Ready(
                            initialRoute = navigationService.getInitialRoute(),
                            bottomNavItems = navigationService.getBottomNavItems(),
                        )
            }
        }.launchIn(viewModelScope)
        getVersionState()
        getAuthState()
    }

    fun setNavController(navController: NavHostController) = navigationService.setNavController(navController)

    fun onLogOutTapped() {
        accessUseCase.logOut()
    }

    fun verifyEmail() {
        _emailVerifiedState.value = EmailVerifiedState.Hidden
        viewModelScope.launch {
            accessUseCase.verifyEmail().also { result ->
                when (result) {
                    is DomainResult.Success -> {
                        _emailVerifiedState.value = EmailVerifiedState.Success
                    }
                    is DomainResult.Error -> {
                        _emailVerifiedState.value = EmailVerifiedState.Error(converter.getMessage(result.error))
                    }
                }
            }
        }
    }

    private fun getVersionState() =
        viewModelScope.launch {
            authUseCase.getAppVersion().collect {
                when (it) {
                    is DomainResult.Success ->
                        versionState.value = VersionState.UpdateRequired(it.data)

                    is DomainResult.Error -> versionState.value = VersionState.UpdateNotRequired
                }
            }
        }

    private fun getAuthState() =
        viewModelScope.launch {
            authUseCase.getAuthState().collect {
                when (it) {
                    is DomainResult.Success -> {
                        authState.value = AuthState.Authorised
                        job += getMemberState()
                    }

                    is DomainResult.Error ->
                        authState.value = AuthState.UnAuthorised
                }
            }
        }

    private fun getMemberState() =
        viewModelScope.launch {
            authUseCase.getLoggedInUser().collect {
                when (it) {
                    is DomainResult.Success ->
                        memberState.value = MemberState.Authenticated(it.data)

                    is DomainResult.Error ->
                        memberState.value = MemberState.UserNotFound(converter.getMessage(it.error))
                }
            }
        }

    @Immutable
    sealed interface VersionState {
        data object Loading : VersionState

        data object UpdateNotRequired : VersionState

        data class UpdateRequired(val url: String) : VersionState
    }

    @Immutable
    sealed interface AuthState {
        data object Loading : AuthState

        data object Authorised : AuthState

        data object UnAuthorised : AuthState
    }

    @Immutable
    sealed interface MemberState {
        data object Loading : MemberState

        data class UserNotFound(val message: String) : MemberState

        data class Authenticated(val member: MemberEntity) : MemberState
    }

    @Immutable
    sealed interface EmailVerifiedState {
        data object Visible : EmailVerifiedState

        data object Hidden : EmailVerifiedState

        data object Success : EmailVerifiedState

        data class Error(val message: String) : EmailVerifiedState
    }

    @Immutable
    sealed interface UiState {
        data object Loading : UiState

        data class UpdateRequired(val url: String) : UiState

        data class Ready(val initialRoute: Route, val bottomNavItems: List<BottomNavItem>) : UiState

        data class UserNotFound(val message: String) : UiState
    }
}
