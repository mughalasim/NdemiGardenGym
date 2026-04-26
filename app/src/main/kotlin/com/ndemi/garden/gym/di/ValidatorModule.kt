package com.ndemi.garden.gym.di

import cv.domain.validator.RegisterScreenValidators
import cv.domain.validator.Validator
import cv.domain.validator.apartment.ApartmentNumberValidatorImp
import cv.domain.validator.email.EmailValidatorImp
import cv.domain.validator.height.HeightValidatorImp
import cv.domain.validator.name.NameValidatorImp
import cv.domain.validator.password.PasswordValidatorImp
import cv.domain.validator.phoneNumber.PhoneNumberValidatorImp
import cv.domain.validator.weight.WeightValidatorImp
import org.koin.core.qualifier.named
import org.koin.dsl.module

val validatorModule =
    module {
        single<Validator>(named<ValidatorWeight>()) { WeightValidatorImp(get()) }

        single<Validator>(named<ValidatorPhoneNumber>()) { PhoneNumberValidatorImp() }

        single<Validator>(named<ValidatorHeight>()) { HeightValidatorImp(get()) }

        single<Validator>(named<ValidatorApartmentNumber>()) { ApartmentNumberValidatorImp() }

        single<Validator>(named<ValidatorName>()) { NameValidatorImp() }

        single<Validator>(named<ValidatorEmail>()) { EmailValidatorImp() }

        single<Validator>(named<ValidatorPassword>()) { PasswordValidatorImp() }

        single<RegisterScreenValidators> {
            RegisterScreenValidators(
                name = get((named<ValidatorName>())),
                phoneNumber = get((named<ValidatorPhoneNumber>())),
                apartmentNumber = get((named<ValidatorApartmentNumber>())),
                height = get(named<ValidatorHeight>()),
                email = get(named<ValidatorEmail>()),
                password = get(named<ValidatorPassword>()),
            )
        }
    }
