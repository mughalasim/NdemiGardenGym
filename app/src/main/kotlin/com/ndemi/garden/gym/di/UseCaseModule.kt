package com.ndemi.garden.gym.di

import cv.domain.usecase.AccessUseCase
import cv.domain.usecase.AttendanceUseCase
import cv.domain.usecase.AuthUseCase
import cv.domain.usecase.MemberUseCase
import cv.domain.usecase.PaymentUseCase
import cv.domain.usecase.PermissionsUseCase
import cv.domain.usecase.StorageUseCase
import cv.domain.validator.MemberValidators
import cv.domain.validator.Validator
import cv.domain.validator.apartment.ApartmentNumberValidatorImp
import cv.domain.validator.height.HeightValidatorImp
import cv.domain.validator.name.NameValidatorImp
import cv.domain.validator.phone.PhoneValidatorImp
import cv.domain.validator.weight.WeightValidatorImp
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule =
    module {
        singleOf(::AuthUseCase)

        singleOf(::AccessUseCase)

        singleOf(::PermissionsUseCase)

        singleOf(::MemberUseCase)

        singleOf(::AttendanceUseCase)

        singleOf(::PaymentUseCase)

        singleOf(::StorageUseCase)

        @WeightValidator
        single<Validator> {
            WeightValidatorImp()
        }

        single<MemberValidators> {
            MemberValidators(
                name = @NameValidator NameValidatorImp(),
                phone = @PhoneValidator PhoneValidatorImp(),
                apartmentNumber = @ApartmentNumberValidator ApartmentNumberValidatorImp(),
                height = @HeightValidator HeightValidatorImp(),
            )
        }
    }
