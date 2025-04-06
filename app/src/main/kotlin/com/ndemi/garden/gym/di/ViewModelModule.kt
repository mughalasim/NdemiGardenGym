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
        viewModel {
            MainScreenViewModel(
                navigationService = get(),
                authUseCase = get(),
                converter = get()
            )
        }

        viewModel {
            LoginScreenViewModel(
                converter = get(),
                authUseCase = get(),
                navigationService = get()
            )
        }

        viewModel {
            ProfileScreenViewModel(
                converter = get(),
                authUseCase = get(),
                memberUseCase = get(),
                attendanceUseCase = get(),
                storageUseCase = get(),
                navigationService = get()
            )
        }

        viewModel {
            AttendanceScreenViewModel(
                converter = get(),
                attendanceUseCase = get()
            )
        }

        viewModel {
            PaymentsScreenViewModel(
                converter = get(),
                paymentUseCase = get(),
                authUseCase = get(),
                navigationService = get()
            )
        }

        viewModel {
            PaymentAddScreenViewModel(
                converter = get(),
                memberUseCase = get(),
                paymentUseCase = get(),
                navigationService = get()
            )
        }

        viewModel {
            LiveAttendanceScreenViewModel(
                converter = get(),
                memberUseCase = get()
            )
        }

        viewModel {
            MembersScreenViewModel(
                converter = get(),
                memberUseCase = get(),
                attendanceUseCase = get(),
                authUseCase = get(),
                navigationService = get()
            )
        }

        viewModel {
            MembersExpiredScreenViewModel(
                converter = get(),
                memberUseCase = get(),
                attendanceUseCase = get(),
                authUseCase = get(),
                navigationService = get()
            )
        }

        viewModel {
            MembersActiveScreenViewModel(
                converter = get(),
                memberUseCase = get(),
                attendanceUseCase = get(),
                authUseCase = get(),
                navigationService = get()
            )
        }

        viewModel {
            MemberEditScreenViewModel(
                converter = get(),
                memberUseCase = get(),
                authUseCase = get(),
                storageUseCase = get(),
                navigationService = get()
            )
        }

        viewModel {
            MembersAttendancesScreenViewModel(
                converter = get(),
                attendanceUseCase = get(),
                authUseCase = get(),
                navigationService = get()
            )
        }

        viewModel {
            RegisterScreenViewModel(
                converter = get(),
                authUseCase = get(),
                memberUseCase = get(),
                navigationService = get()
            )
        }

        viewModel {
            ResetPasswordScreenViewModel(
                converter = get(),
                authUseCase = get()
            )
        }
    }
