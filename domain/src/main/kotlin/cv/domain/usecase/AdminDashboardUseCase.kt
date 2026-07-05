package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.dispatchers.ScopeProvider
import cv.domain.enums.MemberFetchType
import cv.domain.presentationModels.AdminDashboardPresentationModel
import cv.domain.presentationModels.TopTenMemberPresentationModel
import cv.domain.repositories.AttendanceRepository
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.MemberRepository
import cv.domain.repositories.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import java.util.Date

class AdminDashboardUseCase(
    private val scope: ScopeProvider,
    memberRepository: MemberRepository,
    private val paymentRepository: PaymentRepository,
    private val attendanceRepository: AttendanceRepository,
    private val dateProviderRepository: DateProviderRepository,
    private val numberFormatUseCase: NumberFormatUseCase,
) {
    private val allMembersFlow = memberRepository.getMembers(MemberFetchType.MEMBERS)

    fun invoke(currentDate: Date): Flow<AdminDashboardPresentationModel> {
        val currentYear = dateProviderRepository.getYear(currentDate)
        val currentMonth = dateProviderRepository.getMonth(currentDate)
        val monthName = dateProviderRepository.getMonthName(currentMonth)

        return combine(
            allMembersFlow,
            paymentRepository.getAllPayments(year = currentYear),
            attendanceRepository.getAllAttendances(year = currentYear, month = currentMonth),
        ) { allMembers, allPayments, allMonthlyAttendances ->
            when {
                allMembers is DomainResult.Success &&
                    allPayments is DomainResult.Success &&
                    allMonthlyAttendances is DomainResult.Success -> {
                    val members = allMembers.data
                    val paymentsData = allPayments.data
                    val attendancesData = allMonthlyAttendances.data

                    val totalRegisteredUsers = members.size
                    val totalExpiredUsers = members.count { it.renewalFutureDateMillis == null }
                    val totalRevenueYear = paymentsData.totalAmount
                    val totalRevenueMonth =
                        paymentsData.payments
                            .filter {
                                dateProviderRepository.isWithinCurrentMonth(
                                    startTime = it.startDateMillis,
                                    currentMonth = currentMonth,
                                )
                            }.sumOf { it.amount }

                    val attendanceCountMap = attendancesData.attendances.groupBy { it.memberId }
                    val paymentsByMemberMap = paymentsData.payments.filter { it.amount != 0.0 }.groupBy { it.memberId }

                    val topTenActiveMembers =
                        members
                            .mapNotNull { member ->
                                val visits = attendanceCountMap[member.id]?.size ?: 0
                                if (visits > 0) {
                                    TopTenMemberPresentationModel(
                                        id = member.id,
                                        image = member.profileImageUrl,
                                        fullName = "${member.firstName} ${member.lastName}",
                                        visits = visits,
                                    )
                                } else {
                                    null
                                }
                            }.sortedByDescending { it.visits }
                            .take(TOP_10)

                    val topTenPayingMembers =
                        members
                            .mapNotNull { member ->
                                val memberPayments = paymentsByMemberMap[member.id] ?: emptyList()
                                val paymentTotal = memberPayments.sumOf { it.amount }
                                if (paymentTotal != 0.0) {
                                    TopTenMemberPresentationModel(
                                        id = member.id,
                                        image = member.profileImageUrl,
                                        fullName = "${member.firstName} ${member.lastName}",
                                        amountValue = paymentTotal,
                                        amountFormatted = numberFormatUseCase.getCurrencyFormatted(paymentTotal),
                                    )
                                } else {
                                    null
                                }
                            }.sortedByDescending { it.amountValue }
                            .take(TOP_10)

                    AdminDashboardPresentationModel(
                        selectedYear = currentYear,
                        selectedMonth = monthName,
                        totalRegisteredUsers = totalRegisteredUsers,
                        totalExpiredUsers = totalExpiredUsers,
                        totalRevenueYear = numberFormatUseCase.getCurrencyFormatted(totalRevenueYear),
                        totalRevenueMonth = numberFormatUseCase.getCurrencyFormatted(totalRevenueMonth),
                        topTenActiveMembers = topTenActiveMembers,
                        topTenPayingMembers = topTenPayingMembers,
                    )
                }

                else -> {
                    AdminDashboardPresentationModel(
                        selectedYear = currentYear,
                        selectedMonth = monthName,
                    )
                }
            }
        }.flowOn(scope.ioDispatcher())
    }
}

private const val TOP_10 = 10
