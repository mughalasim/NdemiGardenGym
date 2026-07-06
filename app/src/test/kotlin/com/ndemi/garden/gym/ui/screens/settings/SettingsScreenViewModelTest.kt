package com.ndemi.garden.gym.ui.screens.settings

import android.app.Application
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarData
import com.ndemi.garden.gym.ui.enums.SettingType
import com.ndemi.garden.gym.ui.screens.MainDispatcherRule
import cv.domain.enums.unit.CurrencyUnit
import cv.domain.enums.unit.HeightUnit
import cv.domain.enums.unit.WeightUnit
import cv.domain.usecase.AccessUseCase
import cv.domain.usecase.NumberFormatUseCase
import cv.domain.usecase.SettingsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val showSnackbar: (AppSnackbarData) -> Unit = mockk(relaxed = true)
    private val application: Application = mockk(relaxed = true)
    private val numberFormatUseCase: NumberFormatUseCase = mockk()
    private val navigationService: NavigationService = mockk(relaxed = true)
    private val accessUseCase: AccessUseCase = mockk(relaxed = true)
    private val settingsUseCase: SettingsUseCase = mockk()

    private lateinit var viewModel: SettingsScreenViewModel

    @Before
    fun setUp() {
        every { numberFormatUseCase.getWeightUnit() } returns WeightUnit.KILOS
        every { numberFormatUseCase.getHeightUnit() } returns HeightUnit.CENTIMETERS
        every { numberFormatUseCase.getCurrencyUnit() } returns CurrencyUnit.KES

        viewModel =
            SettingsScreenViewModel(
                showSnackbar,
                application,
                numberFormatUseCase,
                navigationService,
                accessUseCase,
                settingsUseCase,
            )
    }

    @Test
    fun `initial state should have correct settings values`() {
        assertEquals("Kilograms", viewModel.uiState.value.weightSetting)
        assertEquals("Centimeters", viewModel.uiState.value.heightSetting)
        assertEquals("Kenya Shillings", viewModel.uiState.value.currencySetting)
    }

    @Test
    fun `logOut should call accessUseCase logOut`() {
        // When
        viewModel.logOut()

        // Then
        verify { accessUseCase.logOut() }
    }

    @Test
    fun `showDialog should update dialogState`() {
        // Given
        every { settingsUseCase.getWeightUnitList() } returns listOf(WeightUnit.KILOS, WeightUnit.POUNDS)
        every { application.getString(any(), any()) } returns "Dialog Title"

        // When
        viewModel.showDialog(SettingType.WEIGHT)

        // Then
        assertTrue(viewModel.dialogState.value.showDialog)
        assertEquals("Dialog Title", viewModel.dialogState.value.title)
        assertEquals(2, viewModel.dialogState.value.listItems.size)
    }

    @Test
    fun `onDialogOptionSelected should save setting and update UI`() {
        // Given
        every { settingsUseCase.getWeightUnitList() } returns listOf(WeightUnit.KILOS, WeightUnit.POUNDS)
        every { numberFormatUseCase.getWeightUnit() } returns WeightUnit.KILOS andThen WeightUnit.POUNDS
        every { settingsUseCase.saveWeightSetting(WeightUnit.POUNDS) } returns Unit
        every { application.getString(any(), any(), any()) } returns "Success"

        // When
        viewModel.onDialogOptionSelected("Pounds", SettingType.WEIGHT)

        // Then
        verify { settingsUseCase.saveWeightSetting(WeightUnit.POUNDS) }
        assertEquals("Pounds", viewModel.uiState.value.weightSetting)
        verify { showSnackbar(any()) }
    }
}
