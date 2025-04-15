package com.ndemi.garden.gym.di

import cv.domain.usecase.AttendanceUseCase
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.PaymentUseCase
import cv.domain.usecase.StorageUseCase
import org.koin.dsl.module

val useCaseModule =
    module {
        single {
            AuthUseCase(
                authRepository = get(),
            )
        }

        single {
            MemberUseCase(
                memberRepository = get(),
                analyticsRepository = get(),
            )
        }

        single {
            AttendanceUseCase(
                attendanceRepository = get(),
                analyticsRepository = get(),
            )
        }

        single {
            PaymentUseCase(
                paymentRepository = get(),
            )
        }

        single {
            StorageUseCase(
                storageRepository = get(),
                memberRepository = get(),
                analyticsRepository = get(),
            )
        }
    }
