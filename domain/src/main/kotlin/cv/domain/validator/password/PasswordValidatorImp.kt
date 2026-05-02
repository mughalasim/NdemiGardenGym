package cv.domain.validator.password

import cv.domain.validator.Validator

class PasswordValidatorImp : Validator {
    override fun isNotValid(value: String): Boolean =
        when {
            value.isEmpty() || !value.matches(Regex(REGEX_PASSWORD)) -> true
            else -> false
        }
}

/**
 * ^                 : Start of string
 * (?=.*[A-Za-z])    : Assert at least one letter exists
 * (?=.*\d)          : Assert at least one digit exists
 * .{6,}             : Allow any character (letter, number, or special), 6 characters minimum
 * $                 : End of string
 */
private const val REGEX_PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d).{6,}$"
