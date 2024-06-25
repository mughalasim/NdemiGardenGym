package com.ndemi.garden.gym.di

import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.SharedPrefsUseCase
import cv.domain.usecase.StorageUseCase
import org.koin.dsl.module

val useCaseModule =
    module {

        single { SharedPrefsUseCase(get()) }

        single { AuthUseCase(get()) }

        single { MemberUseCase(get()) }

        single { StorageUseCase(get(), get()) }
    }
