package com.ndemi.garden.gym.ui.mock

import cv.domain.presentationModels.MemberDashboardPresentationModel
import cv.domain.presentationModels.MemberEditPresentationModel
import cv.domain.presentationModels.MemberPresentationModel

fun getMockActiveMemberPresentationModel() =
    MemberPresentationModel(
        id = "1234567890",
        fullName = "Asim Active",
        email = "asim@test.com",
        apartmentNumber = "B903",
        profileImageUrl = "",
        hasCoach = true,
    )

fun getMockRegisteredMemberPresentationModel() =
    MemberPresentationModel(
        id = "1234567890",
        fullName = "Asim Registered",
        email = "asim@test.com",
        profileImageUrl = "",
        hasCoach = false,
        amountDue = "3000.0",
        phoneNumber = "0722123456",
    )

fun getMockExpiredMemberPresentationModel() =
    MemberPresentationModel(
        id = "1234567890",
        fullName = "Asim",
        email = "asim@test.com",
        profileImageUrl = "",
        hasCoach = false,
        amountDue = "Kes 1,230.00",
        phoneNumber = "0722123456",
    )

fun getMockActiveMemberEditPresentationModel() =
    MemberEditPresentationModel(
        id = "1234567890",
        firstName = "Asim",
        lastName = "Active",
        email = "asim@test.com",
        apartmentNumber = "B903",
        profileImageUrl = "",
        hasCoach = true,
    )

fun getMockMemberDashboardPresentationModel() =
    MemberDashboardPresentationModel(
        id = "1234567890",
        fullName = "Asim Active",
        profileImageUrl = "",
        hasCoach = true,
        workouts = "20",
        height = "170",
        heightUnit = "cm",
        weight = "80",
        weightUnit = "kg",
        bmiValue = 25.0,
    )
