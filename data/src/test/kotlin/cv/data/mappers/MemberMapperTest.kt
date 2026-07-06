package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.MemberModel
import cv.domain.entities.MemberEntity
import cv.domain.enums.MemberType
import cv.domain.repositories.DateProviderRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.Date

class MemberMapperTest {
    private lateinit var mapper: MemberMapper
    private val dateProviderRepository: DateProviderRepository = mockk()

    @Before
    fun setUp() {
        mapper = MemberMapperImp(dateProviderRepository)
    }

    @Test
    fun `getModel should return correct MemberModel with formatted names`() {
        // Given
        val regDateMillis = 1672531200000L
        val activeDateMillis = 1672617600000L
        val renewalDateMillis = 1675209600000L

        val entity =
            MemberEntity(
                id = "1",
                firstName = " john ",
                lastName = " doe ",
                email = "john@example.com",
                registrationDateMillis = regDateMillis,
                activeNowDateMillis = activeDateMillis,
                renewalFutureDateMillis = renewalDateMillis,
                apartmentNumber = " a1 ",
                profileImageUrl = "url",
                hasCoach = true,
                amountDue = 100.0,
                phoneNumber = "123456789",
                memberType = MemberType.ADMIN,
                height = 180.0,
            )

        // When
        val result = mapper.getModel(entity)

        // Then
        assertEquals("john", result.firstName)
        assertEquals("doe", result.lastName)
        assertEquals("a1", result.apartmentNumber)
        assertEquals(Timestamp(Date(regDateMillis)), result.registrationDate)
        assertEquals(Timestamp(Date(activeDateMillis)), result.activeNowDate)
        assertEquals(Timestamp(Date(renewalDateMillis)), result.renewalFutureDate)
        assertEquals("ADMIN", result.memberType)
    }

    @Test
    fun `getEntity should return correct MemberEntity and verify future date`() {
        // Given
        val regDateMillis = 1672531200000L
        val activeDateMillis = 1672617600000L
        val renewalDateMillis = 1675209600000L

        val model =
            MemberModel(
                id = "1",
                firstName = "John",
                lastName = "Doe",
                email = "john@example.com",
                registrationDate = Timestamp(Date(regDateMillis)),
                activeNowDate = Timestamp(Date(activeDateMillis)),
                renewalFutureDate = Timestamp(Date(renewalDateMillis)),
                apartmentNumber = "A1",
                profileImageUrl = "url",
                hasCoach = true,
                amountDue = 100.0,
                phoneNumber = "123456789",
                memberType = "SUPER_ADMIN",
                height = 180.0,
            )

        every { dateProviderRepository.isAfterNow(renewalDateMillis) } returns true

        // When
        val result = mapper.getEntity(model, emailVerified = true)

        // Then
        assertEquals(model.firstName, result.firstName)
        assertEquals(regDateMillis, result.registrationDateMillis)
        assertEquals(activeDateMillis, result.activeNowDateMillis)
        assertEquals(renewalDateMillis, result.renewalFutureDateMillis)
        assertEquals(MemberType.SUPER_ADMIN, result.memberType)
        assertEquals(true, result.emailVerified)
    }

    @Test
    fun `getEntity should return null for renewalFutureDate if it is not after now`() {
        // Given
        val renewalDateMillis = 1675209600000L
        val model =
            MemberModel(
                renewalFutureDate = Timestamp(Date(renewalDateMillis)),
            )

        every { dateProviderRepository.isAfterNow(renewalDateMillis) } returns false

        // When
        val result = mapper.getEntity(model)

        // Then
        assertNull(result.renewalFutureDateMillis)
    }
}
