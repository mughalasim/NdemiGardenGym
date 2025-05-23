package cv.domain.validator.apartment

import cv.domain.validator.Validator

class ApartmentNumberValidatorImp : Validator {
    override fun isNotValid(value: String): Boolean =
        when {
            value.isEmpty() -> false

            value.matches(Regex(REGEX_APARTMENT_NUMBER)) -> false

            else -> true
        }
}

private const val REGEX_APARTMENT_NUMBER = "^[A-Da-d](?:[1-9][0-4][0-4][0-4]?|1404)\$"
