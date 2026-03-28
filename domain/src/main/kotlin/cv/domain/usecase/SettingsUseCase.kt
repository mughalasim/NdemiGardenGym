package cv.domain.usecase

import cv.domain.PREF_SETTING_CURRENCY
import cv.domain.PREF_SETTING_HEIGHT
import cv.domain.PREF_SETTING_WEIGHT
import cv.domain.enums.unit.CurrencyUnit
import cv.domain.enums.unit.HeightUnit
import cv.domain.enums.unit.WeightUnit
import cv.domain.repositories.LocalStorageRepository
import cv.domain.repositories.UnitProviderRepository

class SettingsUseCase(
    private val localStorageRepository: LocalStorageRepository,
    private val unitProviderRepository: UnitProviderRepository,
) {
    fun getWeightUnitList() = unitProviderRepository.getWeightUnits()

    fun getHeightUnitList() = unitProviderRepository.getHeightUnits()

    fun getCurrencyUnitList() = unitProviderRepository.getCurrencyUnits()

    fun saveWeightSetting(weightUnit: WeightUnit) {
        localStorageRepository.setString(PREF_SETTING_WEIGHT, weightUnit.name)
    }

    fun saveHeightSetting(heightUnit: HeightUnit) {
        localStorageRepository.setString(PREF_SETTING_HEIGHT, heightUnit.name)
    }

    fun saveCurrencySetting(currencyUnit: CurrencyUnit) {
        localStorageRepository.setString(PREF_SETTING_CURRENCY, currencyUnit.name)
    }

    fun observeSettingsChanged() = localStorageRepository.onStorageChanged()
}
