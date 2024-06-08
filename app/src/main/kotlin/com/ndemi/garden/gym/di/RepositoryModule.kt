package com.ndemi.garden.gym.di

import cv.data.repository.AuthRepositoryImp
import cv.data.repository.LanguageRepositoryImp
import cv.data.repository.MemberRepositoryImp
import cv.data.repository.SharedPrefsRepositoryImp
import cv.domain.repositories.AuthRepository
import cv.domain.repositories.LanguageRepository
import cv.domain.repositories.MemberRepository
import cv.domain.repositories.SharedPrefsRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repositoryModule =
    module {
        single<AuthRepository> { AuthRepositoryImp(get()) }

        single<LanguageRepository> { LanguageRepositoryImp(androidApplication(), get()) }

        single<SharedPrefsRepository> { SharedPrefsRepositoryImp(get()) }

        single<MemberRepository> { MemberRepositoryImp(get()) }
    }
