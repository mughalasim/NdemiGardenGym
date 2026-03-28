package cv.data.repository

import cv.domain.PREF_SETTING_CURRENCY
import cv.domain.PREF_SETTING_HEIGHT
import cv.domain.PREF_SETTING_WEIGHT
import cv.domain.enums.unit.CurrencyUnit
import cv.domain.enums.unit.HeightUnit
import cv.domain.enums.unit.WeightUnit
import cv.domain.repositories.LocalStorageRepository
import cv.domain.repositories.UnitProviderRepository

class UnitProviderRepositoryImp(
    private val localStorageRepository: LocalStorageRepository,
) : UnitProviderRepository {
    override fun getWeightUnit(): WeightUnit {
        val savedWeightUnit = localStorageRepository.getString(PREF_SETTING_WEIGHT)
        return WeightUnit.entries.find { it.name == savedWeightUnit } ?: WeightUnit.KILOS
    }

    override fun getWeightUnits(): List<WeightUnit> = WeightUnit.entries.toList()

    override fun getHeightUnit(): HeightUnit {
        val savedHeightUnit = localStorageRepository.getString(PREF_SETTING_HEIGHT)
        return HeightUnit.entries.find { it.name == savedHeightUnit } ?: HeightUnit.CENTIMETERS
    }

    override fun getHeightUnits(): List<HeightUnit> = HeightUnit.entries.toList()

    override fun getCurrencyUnit(): CurrencyUnit {
        val savedCurrencyUnit = localStorageRepository.getString(PREF_SETTING_CURRENCY)
        return CurrencyUnit.entries.find { it.name == savedCurrencyUnit } ?: CurrencyUnit.KES
    }

    override fun getCurrencyUnits(): List<CurrencyUnit> = CurrencyUnit.entries.toList()
}
