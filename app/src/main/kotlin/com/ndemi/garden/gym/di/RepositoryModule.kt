package com.ndemi.garden.gym.di

import com.ndemi.garden.gym.BuildConfig
import cv.data.repository.AuthRepositoryImp
import cv.data.repository.MemberRepositoryImp
import cv.data.repository.SharedPrefsRepositoryImp
import cv.data.repository.StorageRepositoryImp
import cv.domain.repositories.AuthRepository
import cv.domain.repositories.MemberRepository
import cv.domain.repositories.SharedPrefsRepository
import cv.domain.repositories.StorageRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<AuthRepository> {
            AuthRepositoryImp(
                pathUser = BuildConfig.PATH_USER,
                logger = get()
            )
        }

        single<SharedPrefsRepository> {
            SharedPrefsRepositoryImp(get())
        }

        single<MemberRepository> {
            MemberRepositoryImp(
                pathUser = BuildConfig.PATH_USER,
                pathAttendance = BuildConfig.PATH_ATTENDANCE,
                pathPayment = BuildConfig.PATH_PAYMENT,
                pathPaymentPlan = BuildConfig.PATH_PAYMENT_PLAN,
                logger = get()
            )
        }

        single<StorageRepository> {
            StorageRepositoryImp(BuildConfig.PATH_USER_IMAGES, get())
        }
    }
