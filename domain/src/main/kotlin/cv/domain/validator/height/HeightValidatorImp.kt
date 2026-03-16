package cv.domain.validator.height

import cv.domain.enums.unit.HeightUnit
import cv.domain.repositories.UnitProviderRepository
import cv.domain.validator.Validator

class HeightValidatorImp(
    private val unitProviderRepository: UnitProviderRepository,
) : Validator {
    override fun isNotValid(value: String): Boolean =
        when {
            value.isEmpty() -> true

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
private const val REGEX_HEIGHT_CENTIMETERS = "^(1[5-9][0-9]|2[0-4][0-9]|250|5[0-9]|6[0-9]|7[0-9]|8[0-9]|9[0-9])\$"

// from 1.0 to 2.5
private const val REGEX_HEIGHT_METERS = "\"^(1\\.\\d{1,2}|2\\.[0-5]{1,2}|2)\$"

// from 4 to 8
private const val REGEX_HEIGHT_FEET = "^(4|5|6|7|8)(\\.\\d+)?\$"
