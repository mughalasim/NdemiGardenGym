package com.ndemi.garden.gym.di

import com.ndemi.garden.gym.ui.screens.main.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {

        viewModel { MainScreenViewModel(get()) }
    }
