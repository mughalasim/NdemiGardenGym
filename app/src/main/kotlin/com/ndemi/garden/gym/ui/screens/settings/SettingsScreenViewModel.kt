package com.ndemi.garden.gym.ui.screens.settings

import android.app.Application
import androidx.compose.runtime.Immutable
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.enums.SettingType
import com.ndemi.garden.gym.ui.screens.base.BaseAction
import com.ndemi.garden.gym.ui.screens.base.BaseState
import com.ndemi.garden.gym.ui.screens.base.BaseViewModel
import cv.domain.usecase.AccessUseCase
import cv.domain.usecase.NumberFormatUseCase
import cv.domain.usecase.SettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsScreenViewModel(
    private val app: Application,
    private val numberFormatUseCase: NumberFormatUseCase,
    private val navigationService: NavigationService,
    private val accessUseCase: AccessUseCase,
    private val settingsUseCase: SettingsUseCase,
) : BaseViewModel<SettingsScreenViewModel.UiState, SettingsScreenViewModel.Action>(UiState.Loading) {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _dialogState = MutableStateFlow(SettingsDialogState())
    val dialogState: StateFlow<SettingsDialogState> = _dialogState.asStateFlow()

    data class SettingsDialogState(
        val showDialog: Boolean = false,
        val settingType: SettingType = SettingType.WEIGHT,
        val title: String = "",
        val message: String = "",
        val listItems: List<String> = emptyList(),
    )

    data class SettingsUiState(
        val weightSetting: String = "",
        val heightSetting: String = "",
        val currencySetting: String = "",
    )

    init {
        refreshUiState()
    }

    fun navigateBack() {
        navigationService.popBack()
    }

    fun logOut() {
        accessUseCase.logOut()
    }

    fun showDialog(settingType: SettingType) {
        val dialogState = SettingsDialogState(showDialog = true, settingType = settingType)
        _dialogState.value =
            when (settingType) {
                SettingType.WEIGHT -> {
                    dialogState.copy(
                        title = app.getString(R.string.txt_settings_dialog_title, app.getString(R.string.txt_settings_weight_unit)),
                        message = app.getString(R.string.txt_settings_dialog_message, app.getString(R.string.txt_settings_weight_unit)),
                        listItems = settingsUseCase.getWeightUnitList().map { it.description },
                    )
                }

                SettingType.HEIGHT -> {
                    dialogState.copy(
                        title = app.getString(R.string.txt_settings_dialog_title, app.getString(R.string.txt_settings_height_unit)),
                        message = app.getString(R.string.txt_settings_dialog_message, app.getString(R.string.txt_settings_height_unit)),
                        listItems = settingsUseCase.getHeightUnitList().map { it.description },
                    )
                }

                SettingType.CURRENCY -> {
                    dialogState.copy(
                        title = app.getString(R.string.txt_settings_dialog_title, app.getString(R.string.txt_settings_currency_unit)),
                        message = app.getString(R.string.txt_settings_dialog_message, app.getString(R.string.txt_settings_currency_unit)),
                        listItems = settingsUseCase.getCurrencyUnitList().map { it.description },
                    )
                }
            }
    }

    fun onDialogOptionSelected(
        selectedOptionDescription: String,
        settingType: SettingType,
    ) {
        resetDialog()
        if (selectedOptionDescription.isEmpty()) {
            return
        }
        sendAction(Action.SetLoading)
        when (settingType) {
            SettingType.WEIGHT -> {
                settingsUseCase.getWeightUnitList().find { it.description == selectedOptionDescription }?.let {
                    if (numberFormatUseCase.getWeightUnit() == it) return@let
                    settingsUseCase.saveWeightSetting(it)
                    sendAction(
                        Action.ShowMessage(
                            app.getString(
                                R.string.txt_settings_success_message,
                                app.getString(R.string.txt_settings_weight_unit),
                                it.description,
                            ),
                        ),
                    )
                }
            }

            SettingType.HEIGHT -> {
                settingsUseCase.getHeightUnitList().find { it.description == selectedOptionDescription }?.let {
                    if (numberFormatUseCase.getHeightUnit() == it) return@let
                    settingsUseCase.saveHeightSetting(it)
                    sendAction(
                        Action.ShowMessage(
                            app.getString(
                                R.string.txt_settings_success_message,
                                app.getString(R.string.txt_settings_height_unit),
                                it.description,
                            ),
                        ),
                    )
                }
            }

            SettingType.CURRENCY -> {
                settingsUseCase.getCurrencyUnitList().find { it.description == selectedOptionDescription }?.let {
                    if (numberFormatUseCase.getCurrencyUnit() == it) return@let
                    settingsUseCase.saveCurrencySetting(it)
                    sendAction(
                        Action.ShowMessage(
                            app.getString(
                                R.string.txt_settings_success_message,
                                app.getString(R.string.txt_settings_currency_unit),
                                it.description,
                            ),
                        ),
                    )
                }
            }
        }
        refreshUiState()
    }

    private fun resetDialog() {
        _dialogState.value = SettingsDialogState()
        sendAction(Action.SetReady)
    }

    private fun refreshUiState() {
        _uiState.value =
            SettingsUiState(
                weightSetting = numberFormatUseCase.getWeightUnit().description,
                heightSetting = numberFormatUseCase.getHeightUnit().description,
                currencySetting = numberFormatUseCase.getCurrencyUnit().description,
            )
    }

    @Immutable
    sealed interface UiState : BaseState {
        data object Loading : UiState

        data object Ready : UiState

        data class Message(
            val message: String,
        ) : UiState
    }

    sealed interface Action : BaseAction<UiState> {
        data object SetReady : Action {
            override fun reduce(state: UiState) = UiState.Ready
        }

        data object SetLoading : Action {
            override fun reduce(state: UiState): UiState = UiState.Loading
        }

        data class ShowMessage(
            val message: String,
        ) : Action {
            override fun reduce(state: UiState): UiState = UiState.Message(message)
        }
    }
}
