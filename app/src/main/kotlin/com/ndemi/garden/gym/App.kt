package com.ndemi.garden.gym

import android.app.Application
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.ndemi.garden.gym.di.apiModule
import com.ndemi.garden.gym.di.applicationModule
import com.ndemi.garden.gym.di.repositoryModule
import com.ndemi.garden.gym.di.useCaseModule
import com.ndemi.garden.gym.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Firebase.crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    applicationModule,
                    apiModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule,
                ),
            )
        }
    }
}
