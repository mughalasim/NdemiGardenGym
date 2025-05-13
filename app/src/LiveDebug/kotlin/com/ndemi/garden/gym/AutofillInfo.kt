package com.ndemi.garden.gym

import cv.domain.enums.MemberType

fun autoFillInformation(memberType: MemberType): Pair<String, String> {
    val username =
        when (memberType) {
            MemberType.SUPER_ADMIN -> BuildConfig.EMAIL_SUPER_ADMIN
            MemberType.ADMIN -> BuildConfig.EMAIL_ADMIN
            MemberType.SUPERVISOR -> BuildConfig.EMAIL_SUPERVISOR
            MemberType.MEMBER -> BuildConfig.EMAIL_MEMBER
        }
    return Pair(username, BuildConfig.TEST_PASS)
}
