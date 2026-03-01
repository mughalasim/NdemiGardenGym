package com.ndemi.garden.gym.di

import com.ndemi.garden.gym.BuildConfig
import cv.data.mappers.AttendanceMapper
import cv.data.mappers.AttendanceMapperImp
import cv.data.mappers.MemberMapper
import cv.data.mappers.MemberMapperImp
import cv.data.mappers.PaymentMapper
import cv.data.mappers.PaymentMapperImp
import cv.data.models.AuthRepositoryUrls
import cv.data.repository.AccessRepositoryImp
import cv.data.repository.AttendanceRepositoryImp
import cv.data.repository.AuthRepositoryImp
import cv.data.repository.DateProviderRepositoryImp
import cv.data.repository.MemberRepositoryImp
import cv.data.repository.PaymentRepositoryImp
import cv.data.repository.StorageRepositoryImp
import cv.domain.repositories.AccessRepository
import cv.domain.repositories.AttendanceRepository
import cv.domain.repositories.AuthRepository
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.MemberRepository
import cv.domain.repositories.PaymentRepository
import cv.domain.repositories.StorageRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<DateProviderRepository> { DateProviderRepositoryImp(get()) }

        single<AttendanceMapper> { AttendanceMapperImp(get()) }

        single<MemberMapper> { MemberMapperImp(get()) }

        single<PaymentMapper> { PaymentMapperImp(get()) }

        single<AuthRepository> {
            AuthRepositoryImp(
                firebaseAuth = get(),
                firebaseFirestore = get(),
                repositoryUrls =
                    AuthRepositoryUrls(
                        pathUser = BuildConfig.PATH_USER,
                        pathVersion = BuildConfig.PATH_APP_VERSION,
                        pathVersionType = BuildConfig.PATH_APP_VERSION_TYPE,
                        currentAppVersion = BuildConfig.VERSION_CODE,
                    ),
                logger = get(),
                memberMapper = get(),
            )
        }

        single<AccessRepository> {
            AccessRepositoryImp(
                firebaseAuth = get(),
                logger = get(),
            )
        }

        single<MemberRepository> {
            MemberRepositoryImp(
                firebaseFirestore = get(),
                pathUser = BuildConfig.PATH_USER,
                logger = get(),
                memberMapper = get(),
            )
        }

        single<AttendanceRepository> {
            AttendanceRepositoryImp(
                firebaseAuth = get(),
                firebaseFirestore = get(),
                pathAttendance = BuildConfig.PATH_ATTENDANCE,
                logger = get(),
                dateProviderRepository = get(),
                attendanceMapper = get(),
            )
        }

        single<PaymentRepository> {
            PaymentRepositoryImp(
                firebaseAuth = get(),
                firebaseFirestore = get(),
                pathPayment = BuildConfig.PATH_PAYMENT,
                pathPaymentPlan = BuildConfig.PATH_PAYMENT_PLAN,
                logger = get(),
                dateProviderRepository = get(),
                paymentMapper = get(),
            )
        }

        single<StorageRepository> {
            StorageRepositoryImp(
                storageReference = get(),
                pathUserImage = BuildConfig.PATH_USER_IMAGES,
                logger = get(),
            )
        }
    }
