package cv.domain.validator.name

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NameValidatorImpTest {
    private lateinit var validator: NameValidatorImp

    @Before
    fun setUp() {
        validator = NameValidatorImp()
    }

    @Test
    fun `isNotValid should return false for valid names`() {
        assertFalse(validator.isNotValid("John"))
        assertFalse(validator.isNotValid("Jane Doe"))
        assertFalse(validator.isNotValid("O'Connor"))
        assertFalse(validator.isNotValid("Anne-Marie"))
        assertFalse(validator.isNotValid("St. John"))
    }

    @Test
    fun `isNotValid should return true for invalid names`() {
        assertTrue(validator.isNotValid(""))
        assertTrue(validator.isNotValid("123"))
        assertTrue(validator.isNotValid("John@"))
        assertTrue(validator.isNotValid("!Name"))
    }
}
