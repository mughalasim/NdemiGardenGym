package com.ndemi.garden.gym.ui.utils

import com.ndemi.garden.gym.BuildConfig
import java.text.DecimalFormat

fun Double.toAmountString(): String = DecimalFormat("${BuildConfig.CURRENCY_CODE} #,###").format(this)
