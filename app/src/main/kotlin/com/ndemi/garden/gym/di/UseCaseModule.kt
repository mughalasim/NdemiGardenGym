package com.ndemi.garden.gym.di

import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.SettingsUseCase
import org.koin.dsl.module

val useCaseModule =
    module {

        single { SettingsUseCase(get(), get()) }

        single { AuthUseCase(get()) }

        single { MemberUseCase(get()) }
    }
