package cv.domain.validator

data class MemberValidators(
    val name: Validator,
    val phone: Validator,
    val apartmentNumber: Validator,
    val height: Validator,
)
