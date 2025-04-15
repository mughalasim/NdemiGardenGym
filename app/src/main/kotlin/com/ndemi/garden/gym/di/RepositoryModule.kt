package com.ndemi.garden.gym.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ndemi.garden.gym.BuildConfig
import cv.data.repository.AttendanceRepositoryImp
import cv.data.repository.AuthRepositoryImp
import cv.data.repository.AuthRepositoryUrls
import cv.data.repository.MemberRepositoryImp
import cv.data.repository.PaymentRepositoryImp
import cv.data.repository.StorageRepositoryImp
import cv.domain.repositories.AttendanceRepository
import cv.domain.repositories.AuthRepository
import cv.domain.repositories.MemberRepository
import cv.domain.repositories.PaymentRepository
import cv.domain.repositories.StorageRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<AuthRepository> {
            AuthRepositoryImp(
                firebaseAuth = Firebase.auth,
                firebaseFirestore = Firebase.firestore,
                repositoryUrls = AuthRepositoryUrls(
                    pathUser = BuildConfig.PATH_USER,
                    pathVersion = BuildConfig.PATH_APP_VERSION,
                    pathVersionType = BuildConfig.PATH_APP_VERSION_TYPE,
                    currentAppVersion = BuildConfig.VERSION_CODE,
                ),
                logger = get()
            )
        }

        single<MemberRepository> {
            MemberRepositoryImp(
                firebaseAuth = Firebase.auth,
                firebaseFirestore = Firebase.firestore,
                pathUser = BuildConfig.PATH_USER,
                logger = get()
            )
        }

        single<AttendanceRepository> {
            AttendanceRepositoryImp(
                firebaseAuth = Firebase.auth,
                firebaseFirestore = Firebase.firestore,
                pathAttendance = BuildConfig.PATH_ATTENDANCE,
                logger = get()
            )
        }

        single<PaymentRepository> {
            PaymentRepositoryImp(
                firebaseAuth = Firebase.auth,
                firebaseFirestore = Firebase.firestore,
                pathPayment = BuildConfig.PATH_PAYMENT,
                pathPaymentPlan = BuildConfig.PATH_PAYMENT_PLAN,
                logger = get()
            )
        }

        single<StorageRepository> {
            StorageRepositoryImp(
                storageReference = Firebase.storage.reference,
                pathUserImage = BuildConfig.PATH_USER_IMAGES,
                logger = get()
            )
        }
    }
