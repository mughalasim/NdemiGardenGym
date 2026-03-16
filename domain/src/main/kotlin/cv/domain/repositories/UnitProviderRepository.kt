package cv.domain.repositories

import cv.domain.enums.unit.HeightUnit
import cv.domain.enums.unit.WeightUnit

interface UnitProviderRepository {
    fun getWeightUnit(): WeightUnit

    fun getHeightUnit(): HeightUnit

    fun getCurrencyUnit(): String
}
