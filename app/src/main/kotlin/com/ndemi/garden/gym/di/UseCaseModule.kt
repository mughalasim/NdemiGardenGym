package com.ndemi.garden.gym.di

import cv.domain.mappers.AttendancePresentationMapper
import cv.domain.mappers.AttendancePresentationMapperImp
import cv.domain.mappers.MemberPresentationMapper
import cv.domain.mappers.MemberPresentationMapperImp
import cv.domain.mappers.PaymentPresentationMapper
import cv.domain.mappers.PaymentPresentationMapperImp
import cv.domain.mappers.WeightPresentationMapper
import cv.domain.mappers.WeightPresentationMapperImp
import cv.domain.usecase.AccessUseCase
import cv.domain.usecase.AdminDashboardUseCase
import cv.domain.usecase.AttendanceUseCase
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.NumberFormatUseCase
import cv.domain.usecase.PaymentUseCase
import cv.domain.usecase.PermissionsUseCase
import cv.domain.usecase.SettingsUseCase
import cv.domain.usecase.StorageUseCase
import cv.domain.usecase.WeightUseCase
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

        singleOf(::AdminDashboardUseCase)

        singleOf(::NumberFormatUseCase)

        singleOf(::WeightUseCase)

        singleOf(::SettingsUseCase)

        single<AttendancePresentationMapper> { AttendancePresentationMapperImp(get()) }

        single<MemberPresentationMapper> { MemberPresentationMapperImp(get(), get()) }

        single<PaymentPresentationMapper> { PaymentPresentationMapperImp(get(), get()) }

        single<WeightPresentationMapper> { WeightPresentationMapperImp(get(), get()) }
    }
