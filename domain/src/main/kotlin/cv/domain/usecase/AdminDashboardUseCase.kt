package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.entities.AdminDashboard
import cv.domain.entities.MemberEntity
import cv.domain.enums.MemberFetchType
import cv.domain.repositories.AttendanceRepository
import cv.domain.repositories.AuthRepository
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.MemberRepository
import cv.domain.repositories.PaymentRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first

class AdminDashboardUseCase(
    private val authRepository: AuthRepository,
    private val memberRepository: MemberRepository,
    private val paymentRepository: PaymentRepository,
    private val attendanceRepository: AttendanceRepository,
    private val dateProviderRepository: DateProviderRepository,
) {
    fun invoke() =
        callbackFlow {
            val currentYear = dateProviderRepository.getYear()
            val currentMonth = dateProviderRepository.getMonth()

            var totalRegisteredUsers: Int
            var totalExpiredUsers: Int
            var totalRevenueYear: Double
            var totalRevenueMonth: Double
            val topTenActiveMembers = mutableListOf<Pair<MemberEntity, Int>>()
            val topTenPayingMembers = mutableListOf<Pair<MemberEntity, Double>>()

            val loggedInUser = authRepository.getLoggedInUser().first()
            val allMembers = memberRepository.getMembers(MemberFetchType.MEMBERS).first()
            val allPayments = paymentRepository.getAllPayments(year = currentYear).first()
            val allAttendances = attendanceRepository.getAllAttendances(year = currentYear, month = currentMonth).first()

            when {
                loggedInUser is DomainResult.Error ||
                    allMembers is DomainResult.Error ||
                    allPayments is DomainResult.Error ||
                    allAttendances is DomainResult.Error -> {
                    trySend(AdminDashboard())
                    cancel()
                }

                loggedInUser is DomainResult.Success &&
                    allMembers is DomainResult.Success &&
                    allPayments is DomainResult.Success &&
                    allAttendances is DomainResult.Success -> {
                    totalRegisteredUsers = allMembers.data.size
                    totalExpiredUsers = allMembers.data.filter { !it.hasPaidMembership() }.size
                    totalRevenueYear = allPayments.data.totalAmount
                    totalRevenueMonth =
                        allPayments.data.payments
                            .filter {
                                dateProviderRepository.isWithinCurrentMonth(startTime = it.startDateMillis, endTime = it.startDateMillis)
                            }.sumOf { it.amount }

                    val memberInfo = mutableListOf<Triple<MemberEntity, Int, Double>>()
                    allMembers.data.forEach {
                        val memberAttendances = allAttendances.data.attendances.filter { attendance -> attendance.memberId == it.id }
                        val memberPayments = allPayments.data.payments.filter { payment -> payment.memberId == it.id }
                        memberInfo.add(
                            Triple(
                                it,
                                memberAttendances.size,
                                memberPayments.sumOf { payment -> payment.amount },
                            ),
                        )
                    }
                    topTenActiveMembers.addAll(memberInfo.sortedByDescending { it.second }.map { Pair(it.first, it.second) }.take(TOP_10))
                    topTenPayingMembers.addAll(memberInfo.sortedByDescending { it.third }.map { Pair(it.first, it.third) }.take(TOP_10))

                    trySend(
                        AdminDashboard(
                            memberEntity = loggedInUser.data,
                            totalRegisteredUsers = totalRegisteredUsers,
                            totalExpiredUsers = totalExpiredUsers,
                            totalRevenueYear = totalRevenueYear,
                            totalRevenueMonth = totalRevenueMonth,
                            topTenActiveMembers = topTenActiveMembers,
                            topTenPayingMembers = topTenPayingMembers,
                        ),
                    )
                }
            }

            awaitClose()
        }
}

private const val TOP_10 = 10
