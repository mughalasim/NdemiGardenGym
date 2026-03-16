package cv.data.repository

import cv.domain.enums.unit.HeightUnit
import cv.domain.enums.unit.WeightUnit
import cv.domain.repositories.UnitProviderRepository

class UnitProviderRepositoryImp : UnitProviderRepository {
    override fun getWeightUnit(): WeightUnit = WeightUnit.KILOS

    override fun getHeightUnit(): HeightUnit = HeightUnit.CENTIMETERS

    override fun getCurrencyUnit(): String = "KES"
}
