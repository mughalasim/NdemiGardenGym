package cv.domain.repositories

import cv.domain.enums.unit.CurrencyUnit
import cv.domain.enums.unit.HeightUnit
import cv.domain.enums.unit.WeightUnit

interface UnitProviderRepository {
    fun getWeightUnit(): WeightUnit

    fun getWeightUnits(): List<WeightUnit>

    fun getHeightUnit(): HeightUnit

    fun getHeightUnits(): List<HeightUnit>

    fun getCurrencyUnit(): CurrencyUnit

    fun getCurrencyUnits(): List<CurrencyUnit>
}
