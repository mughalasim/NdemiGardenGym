package com.ndemi.garden.gym.di

import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.ndemi.garden.gym.BuildConfig
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val firebaseModule =
    module {
        single {
            Firebase.crashlytics.isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
            FirebaseApp.initializeApp(androidApplication())
        }

        single<FirebaseAnalytics> { Firebase.analytics }

        single<FirebaseFirestore> { Firebase.firestore }

        single<FirebaseAuth> { Firebase.auth }

        single<StorageReference> { Firebase.storage.reference }
    }
