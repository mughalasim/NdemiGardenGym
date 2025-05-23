package cv.domain.validator.height

import cv.domain.validator.Validator

class HeightValidatorImp : Validator {
    override fun isNotValid(value: String): Boolean =
        when {
            value.isEmpty() -> false

            value.matches(Regex(REGEX_HEIGHT)) -> false

            else -> true
        }
}

private const val REGEX_HEIGHT = "^[4-9]{1,2}(\\.[0-9]+)?\$"
