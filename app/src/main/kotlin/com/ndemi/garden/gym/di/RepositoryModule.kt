package com.ndemi.garden.gym.di

import com.ndemi.garden.gym.BuildConfig
import cv.data.repository.AuthRepositoryImp
import cv.data.repository.MemberRepositoryImp
import cv.data.repository.SharedPrefsRepositoryImp
import cv.domain.repositories.AuthRepository
import cv.domain.repositories.MemberRepository
import cv.domain.repositories.SharedPrefsRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<AuthRepository> { AuthRepositoryImp(listOf(BuildConfig.ADMIN_LIVE, BuildConfig.ADMIN_STAGING), get()) }

        single<SharedPrefsRepository> { SharedPrefsRepositoryImp(get()) }

        single<MemberRepository> { MemberRepositoryImp(BuildConfig.PATH_USER, BuildConfig.PATH_ATTENDANCE, get()) }
    }
