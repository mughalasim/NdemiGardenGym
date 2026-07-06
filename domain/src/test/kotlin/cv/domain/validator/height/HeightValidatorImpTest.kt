package cv.domain.validator.height

import cv.domain.enums.unit.HeightUnit
import cv.domain.repositories.UnitProviderRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HeightValidatorImpTest {
    private lateinit var validator: HeightValidatorImp
    private val unitProviderRepository: UnitProviderRepository = mockk()

    @Before
    fun setUp() {
        validator = HeightValidatorImp(unitProviderRepository)
    }

    @Test
    fun `isNotValid should return false for empty value`() {
        assertFalse(validator.isNotValid(""))
    }

    @Test
    fun `isNotValid in CENTIMETERS should return false for valid range 50-250`() {
        every { unitProviderRepository.getHeightUnit() } returns HeightUnit.CENTIMETERS
        assertFalse(validator.isNotValid("50"))
        assertFalse(validator.isNotValid("175.5"))
        assertFalse(validator.isNotValid("250"))
    }

    @Test
    fun `isNotValid in CENTIMETERS should return true for invalid values`() {
        every { unitProviderRepository.getHeightUnit() } returns HeightUnit.CENTIMETERS
        assertTrue(validator.isNotValid("49"))
        assertTrue(validator.isNotValid("251"))
        assertTrue(validator.isNotValid("abc"))
    }

    @Test
    fun `isNotValid in METERS should return false for valid range 0_5 to 2_5`() {
        every { unitProviderRepository.getHeightUnit() } returns HeightUnit.METERS
        assertFalse(validator.isNotValid("0.5"))
        assertFalse(validator.isNotValid("1.8"))
        assertFalse(validator.isNotValid("2.5"))
    }

    @Test
    fun `isNotValid in METERS should return true for invalid values`() {
        every { unitProviderRepository.getHeightUnit() } returns HeightUnit.METERS
        assertTrue(validator.isNotValid("0.4"))
        assertTrue(validator.isNotValid("2.6"))
    }

    @Test
    fun `isNotValid in FEET should return false for valid range 2-8`() {
        every { unitProviderRepository.getHeightUnit() } returns HeightUnit.FEET
        assertFalse(validator.isNotValid("2"))
        assertFalse(validator.isNotValid("5.11"))
        assertFalse(validator.isNotValid("8.5"))
    }

    @Test
    fun `isNotValid in FEET should return true for invalid values`() {
        every { unitProviderRepository.getHeightUnit() } returns HeightUnit.FEET
        assertTrue(validator.isNotValid("1.9"))
        assertTrue(validator.isNotValid("8.6"))
    }
}
