package cv.domain.usecase

import cv.domain.DomainResult
import cv.domain.enums.MemberFetchType
import cv.domain.presentationModels.AdminDashboardPresentationModel
import cv.domain.presentationModels.TopTenMemberPresentationModel
import cv.domain.repositories.AttendanceRepository
import cv.domain.repositories.AuthRepository
import cv.domain.repositories.DateProviderRepository
import cv.domain.repositories.MemberRepository
import cv.domain.repositories.PaymentRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import java.util.Date

class AdminDashboardUseCase(
    private val authRepository: AuthRepository,
    private val memberRepository: MemberRepository,
    private val paymentRepository: PaymentRepository,
    private val attendanceRepository: AttendanceRepository,
    private val dateProviderRepository: DateProviderRepository,
    private val numberFormatUseCase: NumberFormatUseCase,
) {
    fun invoke(currentDate: Date) =
        callbackFlow {
            val currentYear = dateProviderRepository.getYear(currentDate)
            val currentMonth = dateProviderRepository.getMonth(currentDate)

            val loggedInUser = authRepository.getLoggedInUser().first()
            val allMembers = memberRepository.getMembers(MemberFetchType.MEMBERS).first()
            val allPayments = paymentRepository.getAllPayments(year = currentYear).first()
            val allMonthlyAttendances = attendanceRepository.getAllAttendances(year = currentYear, month = currentMonth).first()

            when {
                loggedInUser is DomainResult.Error ||
                    allMembers is DomainResult.Error ||
                    allPayments is DomainResult.Error ||
                    allMonthlyAttendances is DomainResult.Error -> {
                    trySend(
                        AdminDashboardPresentationModel(
                            selectedYear = currentYear,
                            selectedMonth = dateProviderRepository.getMonthName(currentMonth),
                        ),
                    )
                    cancel()
                }

                loggedInUser is DomainResult.Success &&
                    allMembers is DomainResult.Success &&
                    allPayments is DomainResult.Success &&
                    allMonthlyAttendances is DomainResult.Success -> {
                    val topTenActiveMembers = mutableListOf<TopTenMemberPresentationModel>()
                    val topTenPayingMembers = mutableListOf<TopTenMemberPresentationModel>()
                    val memberAttendanceInfo = mutableListOf<TopTenMemberPresentationModel>()
                    val memberPaymentInfo = mutableListOf<TopTenMemberPresentationModel>()
                    val totalRegisteredUsers = allMembers.data.size
                    val totalExpiredUsers = allMembers.data.filter { it.renewalFutureDateMillis == null }.size
                    val totalRevenueYear = allPayments.data.totalAmount
                    val totalRevenueMonth =
                        allPayments.data.payments
                            .filter {
                                dateProviderRepository.isWithinCurrentMonth(
                                    startTime = it.startDateMillis,
                                    currentMonth = currentMonth,
                                )
                            }.sumOf { it.amount }

                    memberAttendanceInfo.clear()
                    memberPaymentInfo.clear()
                    allMembers.data.forEach {
                        val memberAttendances =
                            allMonthlyAttendances.data.attendances
                                .filter { attendance -> attendance.memberId == it.id }
                                .size
                        val memberPayments =
                            allPayments.data.payments.filter { payment -> payment.memberId == it.id && payment.amount != 0.0 }
                        val paymentTotal = memberPayments.sumOf { payment -> payment.amount }
                        if (memberAttendances > 0) {
                            memberAttendanceInfo.add(
                                TopTenMemberPresentationModel(
                                    id = it.id,
                                    fullName = "${it.firstName} ${it.lastName}",
                                    visits = memberAttendances,
                                ),
                            )
                        }
                        if (paymentTotal != 0.0) {
                            memberPaymentInfo.add(
                                TopTenMemberPresentationModel(
                                    id = it.id,
                                    fullName = "${it.firstName} ${it.lastName}",
                                    amountValue = paymentTotal,
                                    amountFormatted = numberFormatUseCase.getCurrencyFormatted(paymentTotal),
                                ),
                            )
                        }
                    }
                    topTenActiveMembers.clear()
                    topTenPayingMembers.clear()
                    topTenActiveMembers.addAll(
                        memberAttendanceInfo
                            .sortedByDescending { it.visits }
                            .take(TOP_10),
                    )
                    topTenPayingMembers.addAll(
                        memberPaymentInfo
                            .sortedByDescending { it.amountValue }
                            .take(TOP_10),
                    )

                    trySend(
                        AdminDashboardPresentationModel(
                            selectedYear = currentYear,
                            selectedMonth = dateProviderRepository.getMonthName(currentMonth),
                            totalRegisteredUsers = totalRegisteredUsers,
                            totalExpiredUsers = totalExpiredUsers,
                            totalRevenueYear = numberFormatUseCase.getCurrencyFormatted(totalRevenueYear),
                            totalRevenueMonth = numberFormatUseCase.getCurrencyFormatted(totalRevenueMonth),
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
