package cv.domain.validator.phoneNumber

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PhoneNumberValidatorImpTest {
    private lateinit var validator: PhoneNumberValidatorImp

    @Before
    fun setUp() {
        validator = PhoneNumberValidatorImp()
    }

    @Test
    fun `isNotValid should return false for valid phone numbers`() {
        assertFalse(validator.isNotValid(""))
        assertFalse(validator.isNotValid("0123456789"))
        assertFalse(validator.isNotValid("0123456789012"))
        assertFalse(validator.isNotValid("+1234567890"))
    }

    @Test
    fun `isNotValid should return true for invalid phone numbers`() {
        assertTrue(validator.isNotValid("123456789")) // < 10
        assertTrue(validator.isNotValid("12345678901234")) // > 13
        assertTrue(validator.isNotValid("012345678a"))
        assertTrue(validator.isNotValid("++1234567890"))
    }
}
