package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.entities.AdminDashboard
import cv.domain.entities.AttendanceMonthEntity
import cv.domain.entities.MemberEntity
import cv.domain.enums.MemberFetchType
import cv.domain.repositories.AttendanceRepository
import cv.domain.repositories.AuthRepository
import cv.domain.repositories.MemberRepository
import cv.domain.repositories.PaymentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import java.util.Date

class AdminDashboardUseCase(
    private val authRepository: AuthRepository,
    private val memberRepository: MemberRepository,
    private val attendanceRepository: AttendanceRepository,
    private val paymentRepository: PaymentRepository,
) {
    fun invoke() =
        callbackFlow {
            val currentYear = 2025

            var totalRegisteredUsers: Int
            var totalExpiredUsers: Int
            var totalRevenueYear = 0.0
            var totalRevenueMonth = 0.0
            val topTenActiveMembers = mutableListOf<Pair<MemberEntity, Int>>()
            val topTenPayingMembers = mutableListOf<Pair<MemberEntity, Double>>()

            authRepository.getLoggedInUser().combine(
                memberRepository.getMembers(MemberFetchType.MEMBERS),
            ) { user: DomainResult<MemberEntity>, membersResponse: DomainResult<List<MemberEntity>> ->

                if (user is DomainResult.Error) {
                    trySend(AdminDashboard())
                    cancel()
                }

                when (membersResponse) {
                    is DomainResult.Error -> trySend(AdminDashboard())

                    is DomainResult.Success -> {
                        totalRegisteredUsers = membersResponse.data.size
                        totalExpiredUsers = membersResponse.data.filter { !it.hasPaidMembership() }.size
                        val memberInfo = mutableListOf<Triple<MemberEntity, Int, Double>>()

                        for (member in membersResponse.data) {
                            val attendances: MutableList<AttendanceMonthEntity> = mutableListOf()
                            var totalSpentByMember = 0.0

                            (JANUARY..DECEMBER).map { month ->
                                val response = attendanceRepository.getAttendances(member.id, currentYear, month).first()
                                if (response is DomainResult.Success && response.data.attendances.isNotEmpty()) {
                                    attendances.removeIf { it.monthNumber == response.data.monthNumber }
                                    attendances.add(
                                        AttendanceMonthEntity(
                                            monthNumber = response.data.monthNumber,
                                            totalMinutes = response.data.totalMinutes,
                                            attendances = response.data.attendances,
                                        ),
                                    )
                                }
                            }
                            val paymentResponse = paymentRepository.getPayments(isMembersPayment = true, member.id, currentYear).first()
                            if (paymentResponse is DomainResult.Success) {
                                totalSpentByMember = paymentResponse.data.totalAmount

                                totalRevenueYear += paymentResponse.data.totalAmount

                                totalRevenueMonth +=
                                    paymentResponse.data.payments.filter {
                                        Date(it.startDateMillis).after(Date())
                                    }.sumOf { it.amount }
                            }
                            memberInfo.add(Triple(member, attendances.size, totalSpentByMember))
                        }

                        topTenActiveMembers.addAll(memberInfo.sortedBy { it.second }.map { Pair(it.first, it.second) }.take(TOP_10))
                        topTenPayingMembers.addAll(memberInfo.sortedBy { it.third }.map { Pair(it.first, it.third) }.take(TOP_10))

                        trySend(
                            AdminDashboard(
                                memberEntity = (user as DomainResult.Success).data,
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
            }.launchIn(CoroutineScope(Dispatchers.IO))

            awaitClose()
        }
}

private const val DECEMBER = 12
private const val JANUARY = 1
private const val TOP_10 = 10
