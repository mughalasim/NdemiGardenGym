package com.ndemi.garden.gym

import android.app.Application
import com.ndemi.garden.gym.di.apiModule
import com.ndemi.garden.gym.di.applicationModule
import com.ndemi.garden.gym.di.firebaseModule
import com.ndemi.garden.gym.di.repositoryModule
import com.ndemi.garden.gym.di.useCaseModule
import com.ndemi.garden.gym.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    firebaseModule,
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
