package cv.domain.validator.email

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EmailValidatorImpTest {
    private lateinit var validator: EmailValidatorImp

    @Before
    fun setUp() {
        validator = EmailValidatorImp()
    }

    @Test
    fun `isNotValid should return false for valid emails`() {
        assertFalse(validator.isNotValid("test@example.com"))
        assertFalse(validator.isNotValid("user.name@domain.co.uk"))
        assertFalse(validator.isNotValid("email@sub.domain.com"))
        assertFalse(validator.isNotValid("FIRST.LAST@DOMAIN.COM"))
    }

    @Test
    fun `isNotValid should return true for invalid emails`() {
        assertTrue(validator.isNotValid(""))
        assertTrue(validator.isNotValid("plainaddress"))
        assertTrue(validator.isNotValid("#@%^%#$@#$@#.com"))
        assertTrue(validator.isNotValid("@domain.com"))
        assertTrue(validator.isNotValid("Joe Smith <email@domain.com>"))
        assertTrue(validator.isNotValid("email.domain.com"))
        assertTrue(validator.isNotValid("email@domain@domain.com"))
    }
}
