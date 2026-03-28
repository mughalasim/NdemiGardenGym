package cv.domain.usecase

import cv.domain.entities.WeightEntity
import cv.domain.enums.unit.HeightUnit
import cv.domain.enums.unit.WeightUnit
import cv.domain.repositories.UnitProviderRepository
import java.math.RoundingMode
import java.text.DecimalFormat

/**
The following are the base units on the server
Weight = Kgs
Height = centimeters
Be sure to convert back to these units when calling respective set functions
 */
class NumberFormatUseCase(
    private val unitProviderRepository: UnitProviderRepository,
) {
    fun getWeightUnit() = unitProviderRepository.getWeightUnit()

    fun getWeight(weight: Double): Double {
        if (weight == 0.0) return 0.0
        return when (unitProviderRepository.getWeightUnit()) {
            WeightUnit.KILOS -> {
                weight
            }

            WeightUnit.POUNDS -> {
                weight * POUND_CONVERT
            }
        }.roundHalfUp()
    }

    fun setWeight(weight: Double): Double {
        if (weight == 0.0) return 0.0
        return when (unitProviderRepository.getWeightUnit()) {
            WeightUnit.KILOS -> {
                weight
            }

            WeightUnit.POUNDS -> {
                weight / POUND_CONVERT
            }
        }.roundHalfUp()
    }

    fun getHeightUnit() = unitProviderRepository.getHeightUnit()

    fun getHeight(height: Double): Double {
        if (height == 0.0) return height
        return when (unitProviderRepository.getHeightUnit()) {
            HeightUnit.CENTIMETERS -> {
                height
            }

            HeightUnit.FEET -> {
                height / FEET_CONVERT
            }

            HeightUnit.METERS -> {
                height / METER_CONVERT
            }
        }.roundHalfUp()
    }

    fun setHeight(height: Double): Double {
        if (height == 0.0) return 0.0
        return when (unitProviderRepository.getHeightUnit()) {
            HeightUnit.CENTIMETERS -> {
                height
            }

            HeightUnit.FEET -> {
                height * FEET_CONVERT
            }

            HeightUnit.METERS -> {
                height * METER_CONVERT
            }
        }.roundHalfUp()
    }

    fun getCurrencyUnit() = unitProviderRepository.getCurrencyUnit()

    fun getCurrencyFormatted(input: Double): String =
        DecimalFormat("${unitProviderRepository.getCurrencyUnit().symbol} #,###").format(input)

    fun getBMI(
        entity: WeightEntity,
        height: Double,
    ): Double {
        // height is in cm, weight in KG's from server always
        return if (entity.weight == 0.0 || height == 0.0) {
            0.0
        } else {
            (entity.weight / (height * height) * BMI_CONVERT).roundHalfUp()
        }
    }

    private fun Double.roundHalfUp() = this.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
}

private const val POUND_CONVERT = 2.204623
private const val FEET_CONVERT = 30.48
private const val METER_CONVERT = 100
private const val BMI_CONVERT = 10_000
