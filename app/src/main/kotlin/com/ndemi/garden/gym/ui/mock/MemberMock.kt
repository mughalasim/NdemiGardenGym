package com.ndemi.garden.gym.ui.mock

import cv.domain.entities.MemberEntity
import org.joda.time.DateTime

@Suppress("detekt.MagicNumber")
fun getMockActiveMemberEntity() =
    MemberEntity(
        id = "1234567890",
        firstName = "Asim",
        lastName = "Active",
        email = "asim@test.com",
        registrationDateMillis = DateTime.now().millis,
        renewalFutureDateMillis = DateTime.now().plusDays(25).millis,
        activeNowDateMillis = DateTime.now().minusHours(1).minusMinutes(13).millis,
        apartmentNumber = "B903",
        profileImageUrl = "",
        hasCoach = true,
        amountDue = 1200.0,
    )

@Suppress("detekt.MagicNumber")
fun getMockRegisteredMemberEntity() =
    MemberEntity(
        id = "1234567890",
        firstName = "Asim",
        lastName = "Registered",
        email = "asim@test.com",
        registrationDateMillis = DateTime.now().millis,
        renewalFutureDateMillis = DateTime.now().plusDays(25).millis,
        activeNowDateMillis = null,
        apartmentNumber = "",
        profileImageUrl = "",
        hasCoach = false,
        amountDue = 3000.0,
        phoneNumber = "0722123456",
    )

@Suppress("detekt.MagicNumber")
fun getMockExpiredMemberEntity() =
    MemberEntity(
        id = "1234567890",
        firstName = "Asim",
        lastName = "Expired",
        email = "asim@test.com",
        registrationDateMillis = DateTime.now().millis,
        renewalFutureDateMillis = null,
        activeNowDateMillis = null,
        apartmentNumber = "",
        profileImageUrl = "",
        hasCoach = false,
    )
