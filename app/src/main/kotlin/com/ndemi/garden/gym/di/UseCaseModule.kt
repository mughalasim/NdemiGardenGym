package com.ndemi.garden.gym.di

import cv.domain.usecase.AccessUseCase
import cv.domain.usecase.AttendanceUseCase
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.PaymentUseCase
import cv.domain.usecase.PermissionsUseCase
import cv.domain.usecase.StorageUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule =
    module {
        singleOf(::AuthUseCase)

        singleOf(::AccessUseCase)

        singleOf(::PermissionsUseCase)

        singleOf(::MemberUseCase)

        singleOf(::AttendanceUseCase)

        singleOf(::PaymentUseCase)

        singleOf(::StorageUseCase)
    }
