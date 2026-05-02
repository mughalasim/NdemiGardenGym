package com.ndemi.garden.gym.ui.appSnackbar

data class AppSnackbarData(
    val type: AppSnackbarType,
    val title: String = "",
    val message: String,
    val actionLabel: String? = null,
    val withDismissAction: Boolean = false,
)

fun buildErrorSnackbar(message: String) = AppSnackbarData(type = AppSnackbarType.ERROR, message = message)

fun buildSuccessSnackbar(message: String) = AppSnackbarData(type = AppSnackbarType.SUCCESS, message = message)

fun buildInfoSnackbar(message: String) = AppSnackbarData(type = AppSnackbarType.INFO, message = message)
