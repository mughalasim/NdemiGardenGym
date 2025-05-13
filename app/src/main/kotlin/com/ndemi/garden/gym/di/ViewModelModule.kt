package com.ndemi.garden.gym.di

import com.ndemi.garden.gym.ui.screens.attendance.AttendanceScreenViewModel
import com.ndemi.garden.gym.ui.screens.live.LiveAttendanceScreenViewModel
import com.ndemi.garden.gym.ui.screens.login.LoginScreenViewModel
import com.ndemi.garden.gym.ui.screens.main.MainScreenViewModel
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel
import com.ndemi.garden.gym.ui.screens.members.MembersScreenViewModel
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel
import com.ndemi.garden.gym.ui.screens.payments.PaymentsScreenViewModel
import com.ndemi.garden.gym.ui.screens.profile.ProfileScreenViewModel
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModelOf(::MainScreenViewModel)

        viewModelOf(::LoginScreenViewModel)

        viewModelOf(::ProfileScreenViewModel)

        viewModelOf(::AttendanceScreenViewModel)

        viewModelOf(::PaymentsScreenViewModel)

        viewModelOf(::PaymentAddScreenViewModel)

        viewModelOf(::LiveAttendanceScreenViewModel)

        viewModelOf(::MembersScreenViewModel)

        viewModelOf(::MemberEditScreenViewModel)

        viewModelOf(::ResetPasswordScreenViewModel)

        viewModel(named<CreateMember>()) {
            RegisterScreenViewModel(
                converter = get(),
                accessUseCase = get(),
                memberUseCase = get(),
                navigationService = get(),
                hidePassword = true,
            )
        }
        viewModel(named<RegisterMember>()) {
            RegisterScreenViewModel(
                converter = get(),
                accessUseCase = get(),
                memberUseCase = get(),
                navigationService = get(),
                hidePassword = false,
            )
        }
    }
