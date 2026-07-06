package cv.domain.validator.password

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PasswordValidatorImpTest {
    private lateinit var validator: PasswordValidatorImp

    @Before
    fun setUp() {
        validator = PasswordValidatorImp()
    }

    @Test
    fun `isNotValid should return false for valid passwords`() {
        assertFalse(validator.isNotValid("pass123"))
        assertFalse(validator.isNotValid("Valid1"))
        assertFalse(validator.isNotValid("P@ssword1"))
    }

    @Test
    fun `isNotValid should return true for invalid passwords`() {
        assertTrue(validator.isNotValid(""))
        assertTrue(validator.isNotValid("short")) // < 6
        assertTrue(validator.isNotValid("password")) // no digit
        assertTrue(validator.isNotValid("123456")) // no letter
    }
}
