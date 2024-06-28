package com.ndemi.garden.gym.ui.mock

import cv.domain.entities.AttendanceEntity
import org.joda.time.DateTime

@Suppress("detekt.MagicNumber")
fun getMockAttendanceEntity() = AttendanceEntity(
    memberId = "1234567890",
    startDateMillis = DateTime.now().millis,
    endDateMillis = DateTime.now().plusHours(1).plusMinutes(13).millis,
)
