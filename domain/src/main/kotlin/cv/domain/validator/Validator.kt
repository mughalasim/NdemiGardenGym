package cv.domain.validator

interface Validator {
    fun isNotValid(value: String): Boolean
}
