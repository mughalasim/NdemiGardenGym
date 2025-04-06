package com.ndemi.garden.gym.ui.screens.main

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.navigation.NavHostController
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import cv.domain.DomainResult
import cv.domain.entities.MemberEntity
import cv.domain.usecase.AuthUseCase
import kotlinx.coroutines.Dispatchers

class MainScreenViewModel(
    private val navigationService: NavigationService,
    private val authUseCase: AuthUseCase,
    private val converter: ErrorCodeConverter,
) : ViewModel() {
    fun setNavController(navController: NavHostController) =
        navigationService.setNavController(navController)

    fun getNavigationService(): NavigationService = navigationService

    val authState = liveData(Dispatchers.IO) {
        emit(AuthState.Loading)
        authUseCase.getAuthState().collect{
            when(it){
                is DomainResult.Success ->
                    emit(AuthState.Authorised)

                is DomainResult.Error ->
                    emit(AuthState.UnAuthorised)
            }
        }
    }

    val appVersion = liveData(Dispatchers.IO) {
        emit(VersionState.Loading)
        authUseCase.getAppVersion().collect{
            when(it){
                is DomainResult.Success ->
                    emit(VersionState.UpdateRequired(it.data))

                is DomainResult.Error -> emit(VersionState.Success)
            }
        }
    }

    val loggedInMember = liveData(Dispatchers.IO) {
        emit(UiState.Loading)
        authUseCase.getLoggedInUser().collect {

            when (it) {
                is DomainResult.Success ->
                    emit(UiState.Success(it.data))

                is DomainResult.Error ->
                    emit(UiState.Error(converter.getMessage(it.error)))
            }
        }
    }

    @Immutable
    sealed interface VersionState {
        data object Loading : VersionState

        data object Success : VersionState

        data class UpdateRequired(val url: String) : VersionState
    }

    @Immutable
    sealed interface AuthState {
        data object Loading : AuthState

        data object Authorised : AuthState

        data object UnAuthorised : AuthState
    }

    @Immutable
    sealed interface UiState {
        data object Loading : UiState

        data class Error(val message: String) : UiState

        data class Success(val member: MemberEntity) : UiState
    }
}
