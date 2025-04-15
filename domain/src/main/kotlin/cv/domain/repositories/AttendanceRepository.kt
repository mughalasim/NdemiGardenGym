package cv.domain.repositories

import cv.domain.DomainResult
import cv.domain.entities.AttendanceEntity
import java.util.Date

interface AttendanceRepository {
    suspend fun getAttendances(
        isMembersAttendances: Boolean,
        memberId: String,
        year: Int,
        month: Int,
    ): DomainResult<Pair<List<AttendanceEntity>, Int>>

    suspend fun addAttendance(
        memberId: String,
        startDate: Date,
        endDate: Date,
    ): DomainResult<Unit>

    suspend fun deleteAttendance(attendanceEntity: AttendanceEntity): DomainResult<Unit>
}
