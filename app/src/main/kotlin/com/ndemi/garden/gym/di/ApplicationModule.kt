package com.ndemi.garden.gym.di

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.material3.SnackbarHostState
import com.ndemi.garden.gym.BuildConfig
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.navigation.NavigationService
import com.ndemi.garden.gym.navigation.NavigationServiceImp
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbar
import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarImp
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverter
import com.ndemi.garden.gym.ui.utils.ErrorCodeConverterImp
import cv.data.repository.AnalyticsRepositoryImp
import cv.data.repository.AppLoggerRepositoryImp
import cv.domain.dispatchers.ScopeProvider
import cv.domain.dispatchers.ScopeProviderImp
import cv.domain.repositories.AnalyticsRepository
import cv.domain.repositories.AppLoggerRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val applicationModule =
    module {
        single<SnackbarHostState> { SnackbarHostState() }

        single<AppSnackbar> { AppSnackbarImp() }

        single<ScopeProvider> { ScopeProviderImp() }

        single<AnalyticsRepository> {
            AnalyticsRepositoryImp(
                firebaseAnalytics = get(),
                logger = get(),
            )
        }

        single<ErrorCodeConverter> {
            ErrorCodeConverterImp(
                application = androidApplication(),
                analyticsRepository = get(),
            )
        }

        single<AppLoggerRepository> {
            AppLoggerRepositoryImp(
                isEnabled = BuildConfig.DEBUG,
            )
        }

        single<NavigationService> {
            NavigationServiceImp(
                analyticsRepository = get(),
                permissionsUseCase = get(),
            )
        }

        single<SharedPreferences> {
            androidApplication().getSharedPreferences(
                androidContext().getString(R.string.app_name),
                Context.MODE_PRIVATE,
            )
        }
    }
