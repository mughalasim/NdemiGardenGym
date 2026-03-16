package com.ndemi.garden.gym.ui.mock

import cv.domain.presentationModels.WeightPresentationModel

@Suppress("detekt.MagicNumber")
fun getMockWeightPresentationModel() =
    WeightPresentationModel(
        weight = "55 Kgs",
        dateDayMonthYear = "12/05/2023",
    )
