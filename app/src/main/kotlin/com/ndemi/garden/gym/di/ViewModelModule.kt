package com.ndemi.garden.gym.di

import com.ndemi.garden.gym.ui.screens.attendance.AttendanceScreenViewModel
import com.ndemi.garden.gym.ui.screens.live.LiveAttendanceScreenViewModel
import com.ndemi.garden.gym.ui.screens.login.LoginScreenViewModel
import com.ndemi.garden.gym.ui.screens.main.MainScreenViewModel
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel
import com.ndemi.garden.gym.ui.screens.members.MembersActiveScreenViewModel
import com.ndemi.garden.gym.ui.screens.members.MembersExpiredScreenViewModel
import com.ndemi.garden.gym.ui.screens.members.MembersScreenViewModel
import com.ndemi.garden.gym.ui.screens.membersattendances.MembersAttendancesScreenViewModel
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel
import com.ndemi.garden.gym.ui.screens.payments.PaymentsScreenViewModel
import com.ndemi.garden.gym.ui.screens.profile.ProfileScreenViewModel
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {

        viewModel { MainScreenViewModel(get(), get(), get()) }

        viewModel { LoginScreenViewModel(get(), get(), get()) }

        viewModel { ProfileScreenViewModel(get(), get(), get(), get(), get(), get()) }

        viewModel { AttendanceScreenViewModel(get(), get()) }

        viewModel { PaymentsScreenViewModel(get(), get(), get(), get()) }

        viewModel { PaymentAddScreenViewModel(get(), get(), get()) }

        viewModel { LiveAttendanceScreenViewModel(get(), get()) }

        viewModel { MembersScreenViewModel(get(), get(), get(), get()) }

        viewModel { MembersExpiredScreenViewModel(get(), get(), get(), get()) }

        viewModel { MembersActiveScreenViewModel(get(), get(), get(), get()) }

        viewModel { MemberEditScreenViewModel(get(), get(), get(), get(), get()) }

        viewModel { MembersAttendancesScreenViewModel(get(), get(), get(), get()) }

        viewModel { RegisterScreenViewModel(get(), get(), get(), get()) }

        viewModel { ResetPasswordScreenViewModel(get(), get()) }
    }
