package com.ndemi.garden.gym.ui.mock

import cv.domain.presentationModels.PaymentPresentationModel

fun getMockActivePaymentPresentationModel() =
    PaymentPresentationModel(
        paymentId = "123456",
        memberId = "987654321",
        startDateDayMonthYear = "12/05/2023",
        endDateDayMonthYear = "12/06/2023",
        amount = "Kes 1,200.00",
        paymentPlanDuration = "10 days",
        paymentPlanWarningLevel = 2,
    )

fun getMockExpiredPaymentPresentationModel() =
    PaymentPresentationModel(
        paymentId = "123456789",
        memberId = "65435147",
        startDateDayMonthYear = "12/05/2023",
        endDateDayMonthYear = "12/06/2023",
        amount = "USD 3,500.00",
    )
