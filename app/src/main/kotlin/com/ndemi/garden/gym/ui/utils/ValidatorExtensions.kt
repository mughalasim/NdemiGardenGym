package com.ndemi.garden.gym.ui.utils

import android.telephony.PhoneNumberUtils
import androidx.core.net.toUri

fun String.isValidApartmentNumber(): Boolean = this.matches(Regex("^[A-Da-d](?:[1-9][0-4][0-4][0-4]?|1404)\$"))

fun String.isValidPhoneNumber(): Boolean =
    this.matches(Regex("^[+]?[0-9]{10,13}\$")) &&
        PhoneNumberUtils.isGlobalPhoneNumber(this)

fun String.isValidUri(): Boolean = runCatching { this.toUri() }.getOrNull() != null

fun String.isValidWeight(): Boolean = this.isNotEmpty() && this.matches(Regex("^(?:0|[1-9]\\d*)(?:\\.\\d{1,2})?\$"))

fun String.isValidHeight(): Boolean = this.isNotEmpty() && this.matches(Regex("^[4-9]{1,2}(\\.[0-9]+)?\$"))
