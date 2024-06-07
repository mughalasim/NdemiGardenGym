package cv.data

import android.util.Log
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.Date

object Variables {

    fun String.toDate(): Date {
        return try {
            DateTime.parse(this, FORMAT_DATE_ISO).toDate()
        } catch (e: IllegalArgumentException){
            Log.e(javaClass.simpleName, e.toString())
            Date()
        }
    }

    private val FORMAT_DATE_ISO = DateTimeFormat.forPattern("yyyy-MM-ddTHH:mm:ss.SSSZZ")
    const val PATH_USER = "User"
    const val PATH_ATTENDANCE = "Attendance"
}