package cv.domain.validator.email

import cv.domain.validator.Validator

class EmailValidatorImp : Validator {
    override fun isNotValid(value: String): Boolean =
        when {
            value.isEmpty() || !value.matches(Regex(REGEX_EMAIL, RegexOption.IGNORE_CASE)) -> true
            else -> false
        }
}

private const val REGEX_EMAIL = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$"
