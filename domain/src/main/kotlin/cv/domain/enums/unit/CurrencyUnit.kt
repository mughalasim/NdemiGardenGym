package cv.domain.enums.unit

enum class CurrencyUnit(
    val symbol: String,
    val description: String,
) {
    KES("Kes", "Kenya Shillings"),
    USD("$", "Dollar"),
    GBP("£", "Pound Sterling"),
    EURO("€", "Euro"),
}
