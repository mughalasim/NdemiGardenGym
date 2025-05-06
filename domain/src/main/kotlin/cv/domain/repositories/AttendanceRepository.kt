package cv.domain.repositories

import cv.domain.DomainResult
import cv.domain.entities.AttendanceEntity
import cv.domain.entities.AttendanceMonthEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface AttendanceRepository {
    suspend fun getAttendances(
        memberId: String,
        year: Int,
        month: Int,
    ): Flow<DomainResult<AttendanceMonthEntity>>

    suspend fun addAttendance(
        memberId: String,
        startDate: Date,
        endDate: Date,
    ): DomainResult<Unit>

    suspend fun deleteAttendance(attendanceEntity: AttendanceEntity): DomainResult<Unit>
}
