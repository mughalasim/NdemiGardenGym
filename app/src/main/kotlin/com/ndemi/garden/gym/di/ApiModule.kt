package com.ndemi.garden.gym.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ndemi.garden.gym.BuildConfig
import cv.data.retrofit.ApiAdapterFactory
import cv.data.service.ApiService
import cv.domain.repositories.AppLoggerRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit

val apiModule =
    module {
        single<OkHttpClient> {
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor { message ->
                        get<AppLoggerRepository>().log("Http: $message")
                    }.apply {
                        level =
                            if (BuildConfig.DEBUG) {
                                HttpLoggingInterceptor.Level.BODY
                            } else {
                                HttpLoggingInterceptor.Level.NONE
                            }
                    },
                )
                .build()
        }

        single<Converter.Factory> {
            @Suppress("JSON_FORMAT_REDUNDANT")
            kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                isLenient = true
            }.asConverterFactory("application/json".toMediaType())
        }

        single<Retrofit> {
            Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .client(get())
                .addConverterFactory(get())
                .addCallAdapterFactory(ApiAdapterFactory())
                .build()
        }

        single<ApiService> {
            val retrofit: Retrofit = get()
            retrofit.create(ApiService::class.java)
        }
    }
