package cv.domain.validator.weight

import cv.domain.enums.unit.WeightUnit
import cv.domain.repositories.UnitProviderRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class WeightValidatorImpTest {
    private lateinit var validator: WeightValidatorImp
    private val unitProviderRepository: UnitProviderRepository = mockk()

    @Before
    fun setUp() {
        validator = WeightValidatorImp(unitProviderRepository)
    }

    @Test
    fun `isNotValid should return true for empty value`() {
        assertTrue(validator.isNotValid(""))
    }

    @Test
    fun `isNotValid in KILOS should return false for valid range 1-300`() {
        every { unitProviderRepository.getWeightUnit() } returns WeightUnit.KILOS
        assertFalse(validator.isNotValid("1"))
        assertFalse(validator.isNotValid("75.5"))
        assertFalse(validator.isNotValid("300"))
    }

    @Test
    fun `isNotValid in KILOS should return true for invalid values`() {
        every { unitProviderRepository.getWeightUnit() } returns WeightUnit.KILOS
        assertTrue(validator.isNotValid("0"))
        assertTrue(validator.isNotValid("301"))
        assertTrue(validator.isNotValid("abc"))
    }

    @Test
    fun `isNotValid in POUNDS should return false for valid range 50-999`() {
        every { unitProviderRepository.getWeightUnit() } returns WeightUnit.POUNDS
        assertFalse(validator.isNotValid("50"))
        assertFalse(validator.isNotValid("150.5"))
        assertFalse(validator.isNotValid("999"))
    }

    @Test
    fun `isNotValid in POUNDS should return true for invalid values`() {
        every { unitProviderRepository.getWeightUnit() } returns WeightUnit.POUNDS
        assertTrue(validator.isNotValid("49"))
        assertTrue(validator.isNotValid("1000"))
    }
}
