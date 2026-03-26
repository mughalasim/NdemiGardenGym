package cv.domain.validator.height

import cv.domain.enums.unit.HeightUnit
import cv.domain.repositories.UnitProviderRepository
import cv.domain.validator.Validator

class HeightValidatorImp(
    private val unitProviderRepository: UnitProviderRepository,
) : Validator {
    override fun isNotValid(value: String): Boolean =
        when {
            value.isEmpty() -> false

            !value.matches(
                Regex(
                    when (unitProviderRepository.getHeightUnit()) {
                        HeightUnit.CENTIMETERS -> REGEX_HEIGHT_CENTIMETERS
                        HeightUnit.FEET -> REGEX_HEIGHT_FEET
                        HeightUnit.METERS -> REGEX_HEIGHT_METERS
                    },
                ),
            ) -> true

            else -> false
        }
}

// from 50 to 250
private const val REGEX_HEIGHT_CENTIMETERS = "^(5[0-9]|[6-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|250)(\\.\\d+)?$"

// from 1.0 to 2.5
private const val REGEX_HEIGHT_METERS = "^(0\\.[5-9]\\d*|1(\\.\\d*)?|2(\\.[0-5]\\d*)?|2)?$"

// from 4 to 8
private const val REGEX_HEIGHT_FEET = "^([2-7](\\.\\d+)?|8(\\.[0-5]\\d*)?)?$"
