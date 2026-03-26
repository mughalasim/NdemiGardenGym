package com.ndemi.garden.gym.di

import com.ndemi.garden.gym.ui.screens.attendance.AttendanceScreenViewModel
import com.ndemi.garden.gym.ui.screens.login.LoginScreenViewModel
import com.ndemi.garden.gym.ui.screens.main.MainScreenViewModel
import com.ndemi.garden.gym.ui.screens.memberedit.MemberEditScreenViewModel
import com.ndemi.garden.gym.ui.screens.members.MembersScreenViewModel
import com.ndemi.garden.gym.ui.screens.paymentadd.PaymentAddScreenViewModel
import com.ndemi.garden.gym.ui.screens.payments.PaymentsScreenViewModel
import com.ndemi.garden.gym.ui.screens.profile.admin.ProfileAdminScreenViewModel
import com.ndemi.garden.gym.ui.screens.profile.member.ProfileMemberScreenViewModel
import com.ndemi.garden.gym.ui.screens.register.RegisterScreenViewModel
import com.ndemi.garden.gym.ui.screens.reset.ResetPasswordScreenViewModel
import com.ndemi.garden.gym.ui.screens.weight.edit.WeightEditScreenViewModel
import com.ndemi.garden.gym.ui.screens.weight.graph.WeightGraphComponentViewModel
import com.ndemi.garden.gym.ui.screens.weight.list.WeightListScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModelOf(::MainScreenViewModel)

        viewModelOf(::LoginScreenViewModel)

        viewModelOf(::ProfileMemberScreenViewModel)

        viewModelOf(::ProfileAdminScreenViewModel)

        viewModelOf(::PaymentAddScreenViewModel)

        viewModel { params ->
            AttendanceScreenViewModel(
                memberId = params.get(),
                jobRepository = get(),
                converter = get(),
                attendanceUseCase = get(),
                permissionsUseCase = get(),
                navigationService = get(),
                dateProviderRepository = get(),
            )
        }

        viewModel { params ->
            PaymentsScreenViewModel(
                memberId = params.get(),
                jobRepository = get(),
                converter = get(),
                paymentUseCase = get(),
                permissionsUseCase = get(),
                navigationService = get(),
                numberFormatUseCase = get(),
                paymentPresentationMapper = get(),
                dateProviderRepository = get(),
            )
        }

        viewModel { params ->
            MembersScreenViewModel(
                screenType = params.get(),
                jobRepository = get(),
                converter = get(),
                memberUseCase = get(),
                attendanceUseCase = get(),
                permissionsUseCase = get(),
                navigationService = get(),
                dateProviderRepository = get(),
                memberPresentationMapper = get(),
            )
        }

        viewModel { params ->
            MemberEditScreenViewModel(
                memberId = params.get(),
                memberUseCase = get(),
                validators = get(),
                converter = get(),
                storageUseCase = get(),
                navigationService = get(),
                permissionsUseCase = get(),
                numberFormatUseCase = get(),
                memberPresentationMapper = get(),
            )
        }

        viewModelOf(::ResetPasswordScreenViewModel)

        viewModelOf(::WeightGraphComponentViewModel)

        viewModel { params ->
            WeightEditScreenViewModel(
                weightId = params.get(),
                weight = params.get(),
                dateMillis = params.get(),
                application = get(),
                weightPresentationMapper = get(),
                dateProviderRepository = get(),
                navigationService = get(),
                weightValidator = get(),
                weightUseCase = get(),
                converter = get(),
            )
        }

        viewModelOf(::WeightListScreenViewModel)

        viewModel(named<CreateMember>()) {
            RegisterScreenViewModel(
                converter = get(),
                accessUseCase = get(),
                memberUseCase = get(),
                navigationService = get(),
                validators = get(),
                hidePassword = true,
                dateProviderRepository = get(),
            )
        }
        viewModel(named<RegisterMember>()) {
            RegisterScreenViewModel(
                converter = get(),
                accessUseCase = get(),
                memberUseCase = get(),
                navigationService = get(),
                validators = get(),
                hidePassword = false,
                dateProviderRepository = get(),
            )
        }
    }
