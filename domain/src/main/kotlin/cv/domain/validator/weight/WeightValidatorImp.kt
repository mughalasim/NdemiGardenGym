package cv.domain.validator.weight

import cv.domain.enums.unit.WeightUnit
import cv.domain.repositories.UnitProviderRepository
import cv.domain.validator.Validator

class WeightValidatorImp(
    private val unitProviderRepository: UnitProviderRepository,
) : Validator {
    override fun isNotValid(value: String): Boolean =
        when {
            value.isEmpty() -> true

            !value.matches(
                Regex(
                    when (unitProviderRepository.getWeightUnit()) {
                        WeightUnit.KILOS -> REGEX_WEIGHT_KILOS
                        WeightUnit.POUNDS -> REGEX_WEIGHT_POUNDS
                    },
                ),
            ) -> true

            else -> false
        }
}

// from 20 to 300
private const val REGEX_WEIGHT_KILOS = "^(2[0-9][0-9]|300|[2-9][0-9]|[1-9])\$|^([2-9][0-9]|[1-9])\\.\\d+\$|^1[0-9][0-9](\\.\\d+)?\$"

// from 50 to 999
private const val REGEX_WEIGHT_POUNDS = "^(5[0-9]{1,2}|[6-9][0-9]{1,2}|[1-9][0-9]{2}(\\.\\d+)?|[5-9][0-9](\\.\\d+)?)\$"
