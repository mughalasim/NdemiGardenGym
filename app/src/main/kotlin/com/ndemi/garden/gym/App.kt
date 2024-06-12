package com.ndemi.garden.gym

import android.app.Application
import android.content.res.Resources
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.ndemi.garden.gym.di.apiModule
import com.ndemi.garden.gym.di.applicationModule
import com.ndemi.garden.gym.di.repositoryModule
import com.ndemi.garden.gym.di.useCaseModule
import com.ndemi.garden.gym.di.viewModelModule
import dev.b3nedikt.app_locale.AppLocale
import dev.b3nedikt.restring.Restring
import dev.b3nedikt.reword.RewordInterceptor
import dev.b3nedikt.viewpump.ViewPump
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Restring.init(this)
        ViewPump.init(RewordInterceptor)
        Restring.locale = this.resources.configuration.locales.get(0)
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

    override fun getResources(): Resources {
        return AppLocale.wrapResources(applicationContext, super.getResources())
    }
}
