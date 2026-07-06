package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.PaymentModel
import cv.domain.entities.PaymentEntity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class PaymentMapperTest {
    private lateinit var mapper: PaymentMapper

    @Before
    fun setUp() {
        mapper = PaymentMapperImp()
    }

    @Test
    fun `getModel should return correct PaymentModel`() {
        // Given
        val startMillis = 1672531200000L
        val endMillis = 1675209600000L
        val entity =
            PaymentEntity(
                paymentId = "pay1",
                memberId = "member1",
                startDateMillis = startMillis,
                endDateMillis = endMillis,
                amount = 50.0,
            )

        // When
        val result = mapper.getModel(entity)

        // Then
        assertEquals(entity.paymentId, result.paymentId)
        assertEquals(entity.memberId, result.memberId)
        assertEquals(Timestamp(Date(startMillis)), result.startDate)
        assertEquals(Timestamp(Date(endMillis)), result.endDate)
        assertEquals(entity.amount, result.amount, 0.0)
    }

    @Test
    fun `getEntity should return correct PaymentEntity`() {
        // Given
        val startMillis = 1672531200000L
        val endMillis = 1675209600000L
        val model =
            PaymentModel(
                paymentId = "pay1",
                memberId = "member1",
                startDate = Timestamp(Date(startMillis)),
                endDate = Timestamp(Date(endMillis)),
                amount = 50.0,
            )

        // When
        val result = mapper.getEntity(model)

        // Then
        assertEquals(model.paymentId, result.paymentId)
        assertEquals(model.memberId, result.memberId)
        assertEquals(startMillis, result.startDateMillis)
        assertEquals(endMillis, result.endDateMillis)
        assertEquals(model.amount, result.amount, 0.0)
    }
}
