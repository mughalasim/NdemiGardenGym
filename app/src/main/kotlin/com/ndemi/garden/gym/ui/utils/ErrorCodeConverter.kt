package com.ndemi.garden.gym.ui.utils

import android.app.Application
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.UiError
import cv.domain.DomainError

interface ErrorCodeConverter{
    fun getMessage(domainError: DomainError): String
    fun getMessage(uiError: UiError): String
}

class ErrorCodeConverterImp (
    private val application: Application,
): ErrorCodeConverter {
    override fun getMessage(domainError: DomainError): String =
        when (domainError) {
            DomainError.UNKNOWN -> application.resources.getString(R.string.error_unknown)
            DomainError.SERVER -> application.resources.getString(R.string.error_server)
            DomainError.NETWORK -> application.resources.getString(R.string.error_internet_connection)
            DomainError.UNAUTHORISED -> application.resources.getString(R.string.error_unauthorised)
            DomainError.NO_DATA -> application.resources.getString(R.string.error_no_data)
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
