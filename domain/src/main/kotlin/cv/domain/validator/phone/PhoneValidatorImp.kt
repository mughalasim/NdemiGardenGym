package cv.domain.validator.phone

import cv.domain.validator.Validator

class PhoneValidatorImp : Validator {
    override fun isNotValid(value: String): Boolean =
        when {
            value.isEmpty() -> false

            value.matches(Regex(REGEX_PHONE)) &&
                value.matches(Regex(REGEX_PHONE_INTERNATIONAL)) -> false

            else -> true
        }
}

private const val REGEX_PHONE = "^[+]?[0-9]{10,13}\$"
private const val REGEX_PHONE_INTERNATIONAL = "[+]?[0-9.-]+"
