package com.ndemi.garden.gym.ui.utils

import android.app.Application
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.enums.UiErrorType
import cv.domain.Variables.EVENT_ERROR
import cv.domain.Variables.PARAM_DOMAIN
import cv.domain.enums.DomainErrorType
import cv.domain.repositories.AnalyticsRepository

interface ErrorCodeConverter {
    fun getMessage(domainErrorType: DomainErrorType): String

    fun getMessage(uiErrorType: UiErrorType): String
}

class ErrorCodeConverterImp(
    private val application: Application,
    private val analyticsRepository: AnalyticsRepository,
) : ErrorCodeConverter {
    override fun getMessage(domainErrorType: DomainErrorType): String {
        analyticsRepository.logEvent(
            EVENT_ERROR,
            listOf(Pair(PARAM_DOMAIN, domainErrorType.name)),
        )
        return when (domainErrorType) {
            DomainErrorType.UNKNOWN -> application.resources.getString(R.string.error_unknown)
            DomainErrorType.SERVER -> application.resources.getString(R.string.error_server)
            DomainErrorType.NETWORK -> application.resources.getString(R.string.error_internet_connection)
            DomainErrorType.UNAUTHORISED -> application.resources.getString(R.string.error_unauthorised)
            DomainErrorType.NO_DATA -> application.resources.getString(R.string.error_no_data)
            DomainErrorType.INVALID_ARGUMENT -> application.resources.getString(R.string.error_invalid_info)
            DomainErrorType.EMAIL_ALREADY_EXISTS -> application.resources.getString(R.string.error_email_already_exists)
            DomainErrorType.INVALID_PASSWORD_LENGTH -> application.resources.getString(R.string.error_password_length)
            DomainErrorType.INVALID_LOGIN_CREDENTIALS -> application.resources.getString(R.string.error_invalid_credentials)
            DomainErrorType.USER_DISABLED -> application.resources.getString(R.string.error_account_disabled)
            DomainErrorType.INVALID_SESSION_TIME -> application.resources.getString(R.string.error_workout_session_length)
            DomainErrorType.UPLOAD_FAILURE -> application.resources.getString(R.string.error_upload_failure)
        }
    }

    override fun getMessage(uiErrorType: UiErrorType): String =
        when (uiErrorType) {
            UiErrorType.INVALID_EMAIL -> application.resources.getString(R.string.error_email)
            UiErrorType.INVALID_PASSWORD -> application.resources.getString(R.string.error_password)
            UiErrorType.INVALID_PASSWORD_CONFIRM -> application.resources.getString(R.string.error_password_confirm)
            UiErrorType.INVALID_PASSWORD_MATCH -> application.resources.getString(R.string.error_password_match)
            UiErrorType.INVALID_FIRST_NAME -> application.resources.getString(R.string.error_first_name)
            UiErrorType.INVALID_LAST_NAME -> application.resources.getString(R.string.error_last_name)
            UiErrorType.INVALID_LOGIN_CREDENTIALS -> application.resources.getString(R.string.error_invalid_credentials)
            UiErrorType.REGISTRATION_FAILED -> application.resources.getString(R.string.error_registration_failed)
            UiErrorType.INVALID_APARTMENT_NUMBER -> application.resources.getString(R.string.error_apartment_number)
            UiErrorType.INVALID_AMOUNT -> application.resources.getString(R.string.error_invalid_amount)
            UiErrorType.INVALID_MONTH_DURATION -> application.resources.getString(R.string.error_invalid_month_duration)
            UiErrorType.INVALID_PHONE_NUMBER -> application.resources.getString(R.string.error_invalid_phone_number)
        }
}
