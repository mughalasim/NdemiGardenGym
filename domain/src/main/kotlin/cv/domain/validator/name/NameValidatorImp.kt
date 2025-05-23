package cv.domain.validator.name

import cv.domain.validator.Validator

class NameValidatorImp : Validator {
    override fun isNotValid(value: String): Boolean =
        when {
            value.isNotEmpty() && value.matches(Regex(REGEX_NAME)) -> false

            else -> true
        }
}

private const val REGEX_NAME = "^[a-zA-Z]+(([',.\\s\\-][a-zA-Z\\s])?[a-zA-Z]*)*\$"
