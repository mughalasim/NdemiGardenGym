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
            DomainError.INVALID_ARGUMENT -> "Data supplied is invalid"
            DomainError.EMAIL_ALREADY_EXISTS -> "This email already exists"
            DomainError.PASSWORD_TOO_SHORT -> "Password must be at least 6 characters in length"
            DomainError.INVALID_CREDENTIALS -> "The supplied credentials are incorrect, malformed or have expired."
            DomainError.USER_DISABLED -> "Your account has been disabled, please contact admin"
            DomainError.INVALID_SESSION_TIME -> "Your workout session must be between 5 minutes and 24 hours"
        }
    }

    override fun getMessage(uiError: UiError): String =
        when (uiError) {
            UiError.EMAIL_INVALID -> application.resources.getString(R.string.error_email)
            UiError.PASSWORD_INVALID -> application.resources.getString(R.string.error_password)
            UiError.PASSWORD_CONFIRM_INVALID -> application.resources.getString(R.string.error_password_confirm)
            UiError.PASSWORD_MATCH_INVALID -> application.resources.getString(R.string.error_password_match)
            UiError.FIRST_NAME_INVALID -> application.resources.getString(R.string.error_first_name)
            UiError.LAST_NAME_INVALID -> application.resources.getString(R.string.error_last_name)
            UiError.INVALID_LOGIN_CREDENTIALS -> "Credentials are invalid"
            UiError.REGISTRATION_FAILED -> "Registration failed"
        }
}
