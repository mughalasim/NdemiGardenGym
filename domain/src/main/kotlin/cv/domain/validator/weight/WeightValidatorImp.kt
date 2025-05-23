package cv.domain.validator.weight

import cv.domain.validator.Validator

class WeightValidatorImp : Validator {
    override fun isNotValid(value: String): Boolean =
        when {
            value.isEmpty() -> true

            !value.matches(Regex(REGEX_WEIGHT)) -> true

            value.toDouble() < LOWEST_WEIGHT || value.toDouble() > HIGHEST_WEIGHT -> true

            else -> false
        }
}

private const val LOWEST_WEIGHT = 25
private const val HIGHEST_WEIGHT = 500
private const val REGEX_WEIGHT = "^(?:0|[1-9]\\d*)(?:\\.\\d{1,2})?\$"
