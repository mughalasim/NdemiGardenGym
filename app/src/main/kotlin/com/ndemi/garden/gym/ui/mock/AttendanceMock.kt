package com.ndemi.garden.gym.ui.mock

import cv.domain.entities.AttendanceEntity

@Suppress("detekt.MagicNumber")
fun getMockAttendanceEntity() =
    AttendanceEntity(
        memberId = "1234567890",
        startDateDay = "18 Monday",
        startTime = "12:00",
        endTime = "13:00",
    )
