package cv.domain.validator

data class RegisterScreenValidators(
    val name: Validator,
    val phoneNumber: Validator,
    val apartmentNumber: Validator,
    val height: Validator,
    val email: Validator,
    val password: Validator,
)
