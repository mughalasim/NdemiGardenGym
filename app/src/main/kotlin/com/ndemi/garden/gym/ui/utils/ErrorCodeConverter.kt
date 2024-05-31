package com.ndemi.garden.gym.ui.utils

import android.app.Application
import cv.domain.DomainError
import com.ndemi.garden.gym.R

interface ErrorCodeConverter{
    fun getMessage(domainError: DomainError): String
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
        }
}
