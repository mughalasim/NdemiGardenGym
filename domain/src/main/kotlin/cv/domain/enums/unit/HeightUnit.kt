package cv.domain.enums.unit

enum class HeightUnit(
    val symbol: String,
    val description: String,
) {
    CENTIMETERS("cm", "Centimeters"),
    METERS("m", "Meters"),
    FEET("ft", "Feet"),
}
