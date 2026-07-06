package cv.domain.validator.apartment

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ApartmentNumberValidatorImpTest {
    private lateinit var validator: ApartmentNumberValidatorImp

    @Before
    fun setUp() {
        validator = ApartmentNumberValidatorImp()
    }

    @Test
    fun `isNotValid should return false for valid apartment numbers`() {
        assertFalse(validator.isNotValid(""))
        assertFalse(validator.isNotValid("A101"))
        assertFalse(validator.isNotValid("B200"))
        assertFalse(validator.isNotValid("C1404"))
        assertFalse(validator.isNotValid("D444"))
        assertFalse(validator.isNotValid("a123"))
    }

    @Test
    fun `isNotValid should return true for invalid apartment numbers`() {
        assertTrue(validator.isNotValid("E101"))
        assertTrue(validator.isNotValid("A0"))
        assertTrue(validator.isNotValid("A1405"))
        assertTrue(validator.isNotValid("123"))
        assertTrue(validator.isNotValid("AB123"))
    }
}
