package cv.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import cv.data.mappers.toAttendanceEntity
import cv.data.mappers.toAttendanceModel
import cv.data.mappers.toMemberEntity
import cv.data.mappers.toMemberModel
import cv.data.mappers.toPaymentEntity
import cv.data.mappers.toPaymentModel
import cv.data.models.AttendanceModel
import cv.data.models.MemberModel
import cv.data.models.PaymentModel
import cv.data.retrofit.toDomainError
import cv.domain.DomainError
import cv.domain.DomainResult
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.MemberEntity
import cv.domain.entities.PaymentEntity
import cv.domain.repositories.AppLogLevel
import cv.domain.repositories.AppLoggerRepository
import cv.domain.repositories.MemberRepository
import kotlinx.coroutines.CompletableDeferred
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Minutes
import java.util.Date

class MemberRepositoryImp(
    private val pathUser: String,
    private val pathAttendance: String,
    private val pathPayment: String,
    private val pathPaymentPlan: String,
    private val logger: AppLoggerRepository,
) : MemberRepository {
    private val database = Firebase.firestore

    override suspend fun getMember(): DomainResult<MemberEntity> {
        val id = Firebase.auth.currentUser?.uid ?: run {
            logger.log("Not Authorised", AppLogLevel.ERROR)
            return DomainResult.Error(DomainError.UNAUTHORISED)
        }

        val completable: CompletableDeferred<DomainResult<MemberEntity>> = CompletableDeferred()
        database.collection(pathUser).document(id).get()
            .addOnSuccessListener { document ->
                logger.log("Data received: $document")
                val response = document.toObject<MemberModel>()
                response?.let {
                    completable.complete(DomainResult.Success(it.toMemberEntity()))
                } ?: run {
                    completable.complete(DomainResult.Error(DomainError.NO_DATA))
                }

            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun getMemberById(memberId: String): DomainResult<MemberEntity> {
        val completable: CompletableDeferred<DomainResult<MemberEntity>> = CompletableDeferred()
        database.collection(pathUser).document(memberId).get()
            .addOnSuccessListener { document ->
                logger.log("Data received: $document")
                val response = document.toObject<MemberModel>()
                response?.let {
                    completable.complete(DomainResult.Success(it.toMemberEntity()))
                } ?: run {
                    completable.complete(DomainResult.Error(DomainError.NO_DATA))
                }

            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun getAllMembers(isLive: Boolean): DomainResult<List<MemberEntity>> {
        val completable: CompletableDeferred<DomainResult<List<MemberEntity>>> =
            CompletableDeferred()
        val collection = database.collection(pathUser)

        collection.get()
            .addOnSuccessListener { document ->
                logger.log("Data received: $document")
                val response = document.toObjects<MemberModel>()
                if (isLive) {
                    completable.complete(DomainResult.Success(
                        response.filter {
                            it.activeNowDate != null
                        }.map {
                            it.toMemberEntity()
                        }.sortedByDescending {
                            it.registrationDateMillis
                        })
                    )
                } else {
                    completable.complete(DomainResult.Success(
                        response.filter {
                            it.id != Firebase.auth.currentUser?.uid
                        }.map {
                            it.toMemberEntity()
                        }.sortedByDescending {
                            it.registrationDateMillis
                        })
                    )
                }

            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun updateMember(memberEntity: MemberEntity): DomainResult<Boolean> {
        val completable: CompletableDeferred<DomainResult<Boolean>> = CompletableDeferred()
        val memberModel = memberEntity.toMemberModel()

        database.collection(pathUser).document(memberModel.id).set(memberModel)
            .addOnSuccessListener {
                completable.complete(DomainResult.Success(true))
            }

            .addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun getAttendances(
        isMembersAttendances: Boolean,
        memberId: String,
        year: Int,
        month: Int
    ): DomainResult<List<AttendanceEntity>> {
        val setMemberId = memberId.ifEmpty {
            Firebase.auth.currentUser?.uid ?: run {
                logger.log("Not Authorised", AppLogLevel.ERROR)
                return DomainResult.Error(DomainError.UNAUTHORISED)
            }
        }
        val reference = database
            .collection(pathAttendance)
            .document(year.toString())
            .collection(month.toString())

        val completable: CompletableDeferred<DomainResult<List<AttendanceEntity>>> =
            CompletableDeferred()
        reference.get()
            .addOnSuccessListener { document ->
                logger.log("Data received: $document")
                val response = document.toObjects<AttendanceModel>()
                if (isMembersAttendances) {
                    completable.complete(DomainResult.Success(
                        response.filter {
                            it.memberId == setMemberId
                        }.map {
                            it.toAttendanceEntity()
                        }.sortedByDescending {
                            it.startDateMillis
                        })
                    )
                } else {
                    completable.complete(DomainResult.Success(
                        response.map {
                            it.toAttendanceEntity()
                        }.sortedByDescending {
                            it.startDateMillis
                        })
                    )
                }

            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun addAttendance(
        memberId: String,
        startDate: Date,
        endDate: Date
    ): DomainResult<Unit> {
        val setMemberId = memberId.ifEmpty {
            Firebase.auth.currentUser?.uid ?: run {
                logger.log("Not Authorised", AppLogLevel.ERROR)
                return DomainResult.Error(DomainError.UNAUTHORISED)
            }
        }

        val startDateTime = DateTime(startDate)
        val endDateTime = DateTime(endDate)

        if (endDateTime.isBefore(startDateTime) ||
            Days.daysBetween(startDateTime, endDateTime).days > 1 ||
            Minutes.minutesBetween(startDateTime, endDateTime).minutes < 1
        ) {
            return DomainResult.Error(DomainError.INVALID_SESSION_TIME)
        }

        val attendanceModel = AttendanceModel(
            memberId = setMemberId,
            startDate = Timestamp(startDate),
            endDate = Timestamp(endDate)
        )
        val collection = database
            .collection(pathAttendance)
            .document(startDateTime.year.toString())
            .collection(startDateTime.monthOfYear.toString())
            .document(attendanceModel.getAttendanceId())

        val completable: CompletableDeferred<DomainResult<Unit>> = CompletableDeferred()
        collection.set(attendanceModel)
            .addOnSuccessListener {
                logger.log("Attendance Added")
                completable.complete(DomainResult.Success(Unit))

            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun deleteAttendance(
        attendanceEntity: AttendanceEntity
    ): DomainResult<Unit> {
        val attendanceModel = attendanceEntity.toAttendanceModel()
        val startDateTime = DateTime(attendanceEntity.startDateMillis)

        val collection = database
            .collection(pathAttendance)
            .document(startDateTime.year.toString())
            .collection(startDateTime.monthOfYear.toString())
            .document(attendanceModel.getAttendanceId())

        val completable: CompletableDeferred<DomainResult<Unit>> = CompletableDeferred()
        collection.delete()
            .addOnSuccessListener {
                logger.log("Attendance Deleted")
                completable.complete(DomainResult.Success(Unit))

            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun deleteMember(
        memberEntity: MemberEntity
    ): DomainResult<Unit> {
        val memberModel = memberEntity.toMemberModel()

        val collection = database
            .collection(pathUser)
            .document(memberModel.id)

        val completable: CompletableDeferred<DomainResult<Unit>> = CompletableDeferred()
        collection.delete()
            .addOnSuccessListener {
                logger.log("Member Deleted")
                completable.complete(DomainResult.Success(Unit))

            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun addPaymentPlan(paymentEntity: PaymentEntity): DomainResult<Unit> {
        val paymentModel = paymentEntity.toPaymentModel()

        val collection = database
            .collection(pathPayment)
            .document(pathPaymentPlan)
            .collection(DateTime(paymentEntity.startDateMillis).year.toString())
            .document(paymentModel.paymentId)

        val completable: CompletableDeferred<DomainResult<Unit>> = CompletableDeferred()
        collection.set(paymentModel)
            .addOnSuccessListener {
                logger.log("Payment Plan Added")
                completable.complete(DomainResult.Success(Unit))

            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun getPaymentPlans(
        isMembersPayment: Boolean,
        memberId: String,
        year: Int,
    ): DomainResult<Pair<List<PaymentEntity>, Boolean>> {
        val setMemberId = memberId.ifEmpty {
            Firebase.auth.currentUser?.uid ?: run {
                logger.log("Not Authorised", AppLogLevel.ERROR)
                return DomainResult.Error(DomainError.UNAUTHORISED)
            }
        }
        val reference = database
            .collection(pathPayment)
            .document(pathPaymentPlan)
            .collection(year.toString())

        val completable: CompletableDeferred<DomainResult<Pair<List<PaymentEntity>, Boolean>>> =
            CompletableDeferred()
        reference.get()
            .addOnSuccessListener { document ->
                logger.log("Data received: $document")
                val response = document.toObjects<PaymentModel>()
                val list = response
                    .map { it.toPaymentEntity() }
                    .sortedByDescending { it.startDateMillis }
                    .filter { it.memberId == setMemberId && isMembersPayment }

                var canAddPayment = true
                list.forEach {
                    if (DateTime(it.endDateMillis).isAfterNow){
                        canAddPayment = false
                        return@forEach
                    }
                }
                completable.complete(DomainResult.Success(Pair(list, canAddPayment)))

            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }

    override suspend fun deletePaymentPlan(
        paymentEntity: PaymentEntity
    ): DomainResult<Unit> {
        val collection = database
            .collection(pathPayment)
            .document(pathPaymentPlan)
            .collection(DateTime(paymentEntity.startDateMillis).year.toString())
            .document(paymentEntity.paymentId)

        val completable: CompletableDeferred<DomainResult<Unit>> = CompletableDeferred()
        collection.delete()
            .addOnSuccessListener {
                logger.log("Payment Plan Deleted")
                completable.complete(DomainResult.Success(Unit))

            }.addOnFailureListener {
                logger.log("Exception: $it", AppLogLevel.ERROR)
                completable.complete(DomainResult.Error(it.toDomainError()))
            }

        return completable.await()
    }
}
