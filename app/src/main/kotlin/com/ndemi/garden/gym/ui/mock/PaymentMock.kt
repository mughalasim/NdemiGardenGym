package com.ndemi.garden.gym.ui.mock

import cv.domain.entities.PaymentEntity
import org.joda.time.DateTime

@Suppress("detekt.MagicNumber")
fun getMockActivePaymentEntity() =
    PaymentEntity(
        paymentId = "123456",
        memberId = "987654321",
        startDateMillis = DateTime.now().millis,
        endDateMillis = DateTime.now().plusMonths(1).millis,
        amount = 1200.00,
    )

@Suppress("detekt.MagicNumber")
fun getMockExpiredPaymentEntity() =
    PaymentEntity(
        paymentId = "123456789",
        memberId = "65435147",
        startDateMillis = DateTime.now().minusDays(22).millis,
        endDateMillis = DateTime.now().minusDays(2).millis,
        amount = 3500.00,
    )
