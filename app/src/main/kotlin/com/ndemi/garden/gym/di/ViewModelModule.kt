package com.ndemi.garden.gym.di

import com.ndemi.garden.gym.ui.appSnackbar.AppSnackbarViewModel
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
import com.ndemi.garden.gym.ui.screens.settings.SettingsScreenViewModel
import com.ndemi.garden.gym.ui.screens.weight.edit.WeightEditScreenViewModel
import com.ndemi.garden.gym.ui.screens.weight.graph.WeightGraphComponentViewModel
import com.ndemi.garden.gym.ui.screens.weight.list.WeightListScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModelOf(::AppSnackbarViewModel)

        viewModelOf(::WeightGraphComponentViewModel)

        viewModel { params ->
            MainScreenViewModel(
                showSnackbar = params.get(),
                jobRepository = get(),
                navigationService = get(),
                authUseCase = get(),
                accessUseCase = get(),
                converter = get(),
            )
        }

        viewModel { params ->
            LoginScreenViewModel(
                showSnackbar = params.get(),
                converter = get(),
                accessUseCase = get(),
                emailValidator = get(named<ValidatorEmail>()),
                passwordValidator = get(named<ValidatorPassword>()),
            )
        }

        viewModel { params ->
            ResetPasswordScreenViewModel(
                showSnackbar = params.get(),
                converter = get(),
                accessUseCase = get(),
                emailValidator = get(named<ValidatorEmail>()),
            )
        }

        viewModel(named<RegisterMember>()) { params ->
            RegisterScreenViewModel(
                converter = get(),
                accessUseCase = get(),
                memberUseCase = get(),
                navigationService = get(),
                validators = get(),
                hidePassword = false,
                dateProviderRepository = get(),
                showSnackbar = params.get(),
            )
        }

        viewModel { params ->
            ProfileMemberScreenViewModel(
                showSnackbar = params.get(),
                application = get(),
                jobRepository = get(),
                converter = get(),
                memberUseCase = get(),
                attendanceUseCase = get(),
                navigationService = get(),
                dateProviderRepository = get(),
                memberPresentationMapper = get(),
                weightUseCase = get(),
                settingsUseCase = get(),
                authUseCase = get(),
                storageUseCase = get(),
            )
        }

        viewModel { params ->
            AttendanceScreenViewModel(
                memberId = params[0],
                showSnackbar = params[1],
                jobRepository = get(),
                converter = get(),
                attendanceUseCase = get(),
                permissionsUseCase = get(),
                navigationService = get(),
                dateProviderRepository = get(),
            )
        }

        viewModel(named<CreateMember>()) { params ->
            RegisterScreenViewModel(
                converter = get(),
                accessUseCase = get(),
                memberUseCase = get(),
                navigationService = get(),
                validators = get(),
                hidePassword = true,
                dateProviderRepository = get(),
                showSnackbar = params.get(),
            )
        }

        viewModel { params ->
            ProfileAdminScreenViewModel(
                showSnackbar = params.get(),
                jobRepository = get(),
                adminDashboardUseCase = get(),
                navigationService = get(),
                settingsUseCase = get(),
                dateProviderRepository = get(),
            )
        }

        viewModel { params ->
            MembersScreenViewModel(
                screenType = params[0],
                showSnackbar = params[1],
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
            PaymentAddScreenViewModel(
                memberId = params[0],
                showSnackbar = params[1],
                converter = get(),
                paymentUseCase = get(),
                navigationService = get(),
                dateProviderRepository = get(),
                numberFormatUseCase = get(),
            )
        }

        viewModel { params ->
            SettingsScreenViewModel(
                showSnackbar = params.get(),
                app = get(),
                numberFormatUseCase = get(),
                navigationService = get(),
                accessUseCase = get(),
                settingsUseCase = get(),
            )
        }

        viewModel { params ->
            PaymentsScreenViewModel(
                memberId = params[0],
                showSnackbar = params[1],
                jobRepository = get(),
                converter = get(),
                paymentUseCase = get(),
                permissionsUseCase = get(),
                navigationService = get(),
                numberFormatUseCase = get(),
                paymentPresentationMapper = get(),
                dateProviderRepository = get(),
                settingsUseCase = get(),
            )
        }

        viewModel { params ->
            MemberEditScreenViewModel(
                memberId = params[0],
                showSnackbar = params[1],
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

        viewModel { params ->
            WeightEditScreenViewModel(
                weightId = params[0],
                weight = params[1],
                dateMillis = params[2],
                showSnackbar = params[3],
                application = get(),
                weightPresentationMapper = get(),
                dateProviderRepository = get(),
                navigationService = get(),
                weightValidator = get((named<ValidatorWeight>())),
                weightUseCase = get(),
                converter = get(),
            )
        }

        viewModel { params ->
            WeightListScreenViewModel(
                showSnackbar = params.get(),
                jobRepository = get(),
                weightPresentationMapper = get(),
                dateProviderRepository = get(),
                navigationService = get(),
                weightUseCase = get(),
                converter = get(),
                numberFormatUseCase = get(),
            )
        }
    }
