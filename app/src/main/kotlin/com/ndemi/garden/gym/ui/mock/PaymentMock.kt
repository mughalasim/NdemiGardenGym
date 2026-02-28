package com.ndemi.garden.gym.ui.mock

import cv.domain.entities.PaymentEntity

@Suppress("detekt.MagicNumber")
fun getMockActivePaymentEntity() =
    PaymentEntity(
        paymentId = "123456",
        memberId = "987654321",
        amount = 1200.00,
    )

@Suppress("detekt.MagicNumber")
fun getMockExpiredPaymentEntity() =
    PaymentEntity(
        paymentId = "123456789",
        memberId = "65435147",
        amount = 3500.00,
    )
