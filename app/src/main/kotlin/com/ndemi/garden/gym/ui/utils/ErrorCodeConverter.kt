package com.ndemi.garden.gym.ui.utils

import android.app.Application
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.UiError
import cv.domain.DomainError
import cv.domain.Variables.EVENT_ERROR
import cv.domain.Variables.PARAM_DOMAIN
import cv.domain.repositories.AnalyticsRepository

interface ErrorCodeConverter{
    fun getMessage(domainError: DomainError): String
    fun getMessage(uiError: UiError): String
}

class ErrorCodeConverterImp (
    private val application: Application,
    private val analyticsRepository: AnalyticsRepository,
): ErrorCodeConverter {
    override fun getMessage(domainError: DomainError): String {
        analyticsRepository.logEvent(EVENT_ERROR,
            listOf( Pair (PARAM_DOMAIN, domainError.name))
        )
        return when (domainError) {
            DomainError.UNKNOWN -> application.resources.getString(R.string.error_unknown)
            DomainError.SERVER -> application.resources.getString(R.string.error_server)
            DomainError.NETWORK -> application.resources.getString(R.string.error_internet_connection)
            DomainError.UNAUTHORISED -> application.resources.getString(R.string.error_unauthorised)
            DomainError.NO_DATA -> application.resources.getString(R.string.error_no_data)
            DomainError.INVALID_ARGUMENT -> application.resources.getString(R.string.error_invalid_info)
            DomainError.EMAIL_ALREADY_EXISTS -> application.resources.getString(R.string.error_email_already_exists)
            DomainError.INVALID_PASSWORD_LENGTH -> application.resources.getString(R.string.error_password_length)
            DomainError.INVALID_LOGIN_CREDENTIALS -> application.resources.getString(R.string.error_invalid_credentials)
            DomainError.USER_DISABLED -> application.resources.getString(R.string.error_account_disabled)
            DomainError.INVALID_SESSION_TIME -> application.resources.getString(R.string.error_workout_session_length)
        }
    }

    override fun getMessage(uiError: UiError): String =
        when (uiError) {
            UiError.INVALID_EMAIL -> application.resources.getString(R.string.error_email)
            UiError.INVALID_PASSWORD -> application.resources.getString(R.string.error_password)
            UiError.INVALID_PASSWORD_CONFIRM -> application.resources.getString(R.string.error_password_confirm)
            UiError.INVALID_PASSWORD_MATCH -> application.resources.getString(R.string.error_password_match)
            UiError.INVALID_FIRST_NAME -> application.resources.getString(R.string.error_first_name)
            UiError.INVALID_LAST_NAME -> application.resources.getString(R.string.error_last_name)
            UiError.INVALID_LOGIN_CREDENTIALS -> application.resources.getString(R.string.error_invalid_credentials)
            UiError.REGISTRATION_FAILED -> application.resources.getString(R.string.error_registration_failed)
        }
}
