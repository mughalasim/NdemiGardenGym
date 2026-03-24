package com.ndemi.garden.gym.ui.mock

import cv.domain.presentationModels.AttendancePresentationModel

fun getMockAttendancePresentationModel() =
    AttendancePresentationModel(
        memberId = "1234567890",
        startDateDay = "18 Monday",
        startTime = "12:00",
        endTime = "13:00",
    )
