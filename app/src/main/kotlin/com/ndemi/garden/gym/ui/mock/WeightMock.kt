package com.ndemi.garden.gym.ui.mock

import cv.domain.entities.WeightEntity
import org.joda.time.DateTime

@Suppress("detekt.MagicNumber")
fun getMockWeightEntity() =
    WeightEntity(
        dateMillis = DateTime.now().millis,
        weight = "55.0",
    )
