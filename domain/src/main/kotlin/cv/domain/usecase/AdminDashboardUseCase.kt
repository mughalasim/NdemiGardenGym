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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import java.util.Calendar
import java.util.Date

class AdminDashboardUseCase(
    private val authRepository: AuthRepository,
    private val memberRepository: MemberRepository,
    private val attendanceRepository: AttendanceRepository,
    private val paymentRepository: PaymentRepository,
) {
    fun invoke() =
        callbackFlow {
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)

            var totalRegisteredUsers: Int
            var totalExpiredUsers: Int
            var totalRevenueYear = 0.0
            var totalRevenueMonth = 0.0
            val topTenActiveMembers = mutableListOf<Pair<MemberEntity, Int>>()
            val topTenPayingMembers = mutableListOf<Pair<MemberEntity, Double>>()

            val loggedInUser = authRepository.getLoggedInUser().first()
            val membersResponse = memberRepository.getMembers(MemberFetchType.MEMBERS).first()

            if (loggedInUser is DomainResult.Error || membersResponse is DomainResult.Error) {
                trySend(AdminDashboard())
                cancel()
            } else if (membersResponse is DomainResult.Success) {
                totalRegisteredUsers = membersResponse.data.size
                totalExpiredUsers = membersResponse.data.filter { !it.hasPaidMembership() }.size
                val memberInfo = mutableListOf<Triple<MemberEntity, Int, Double>>()

                for (member in membersResponse.data) {
                    val attendances: MutableList<AttendanceMonthEntity> = mutableListOf()
                    var totalSpentByMember = 0.0

                            for (month in JANUARY..DECEMBER) {
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
                            paymentResponse.data.payments
                                .filter {
                                    Date(it.startDateMillis).after(Date())
                                }.sumOf { it.amount }
                    }
                    memberInfo.add(Triple(member, attendances.size, totalSpentByMember))
                }

                topTenActiveMembers.addAll(memberInfo.sortedBy { it.second }.map { Pair(it.first, it.second) }.take(TOP_10))
                topTenPayingMembers.addAll(memberInfo.sortedBy { it.third }.map { Pair(it.first, it.third) }.take(TOP_10))

                trySend(
                    AdminDashboard(
                        memberEntity = (loggedInUser as DomainResult.Success).data,
                        totalRegisteredUsers = totalRegisteredUsers,
                        totalExpiredUsers = totalExpiredUsers,
                        totalRevenueYear = totalRevenueYear,
                        totalRevenueMonth = totalRevenueMonth,
                        topTenActiveMembers = topTenActiveMembers,
                        topTenPayingMembers = topTenPayingMembers,
                    ),
                )
            }

            awaitClose()
        }
}

private const val DECEMBER = 12
private const val JANUARY = 1
private const val TOP_10 = 10
