package com.ndemi.garden.gym.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import cv.data.repository.AnalyticsRepositoryImp
import cv.data.repository.AppLoggerRepositoryImp
import cv.domain.repositories.AnalyticsRepository
import cv.domain.repositories.AppLoggerRepository
import com.ndemi.garden.gym.BuildConfig
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.NavigationServiceImp
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverterImp
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val applicationModule =
    module {

        single { FirebaseApp.initializeApp(androidApplication()) }

        single<ErrorCodeConverter> { ErrorCodeConverterImp(androidApplication()) }

        single<AppLoggerRepository> { AppLoggerRepositoryImp(isEnabled = BuildConfig.DEBUG) }

        single<AnalyticsRepository> { AnalyticsRepositoryImp(Firebase.analytics, get()) }

        single<NavigationService> { NavigationServiceImp(get()) }

        single<SharedPreferences> {
            androidApplication().getSharedPreferences(
                androidContext().getString(R.string.app_name),
                Context.MODE_PRIVATE,
            )
        }
    }