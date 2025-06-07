package com.attendance.app.data.dao

import androidx.room.*
import com.attendance.app.data.model.Attendance
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM attendance WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAttendanceHistory(userId: String): Flow<List<Attendance>>

    @Query("SELECT * FROM attendance WHERE userId = :userId AND date(timestamp/1000, 'unixepoch') = date(:date/1000, 'unixepoch')")
    fun getAttendanceByDate(userId: String, date: Long): Flow<List<Attendance>>

    @Insert
    suspend fun insertAttendance(attendance: Attendance)

    @Update
    suspend fun updateAttendance(attendance: Attendance)

    @Delete
    suspend fun deleteAttendance(attendance: Attendance)

    @Query("SELECT * FROM attendance WHERE userId = :userId AND type = :type AND date(timestamp/1000, 'unixepoch') = date(:date/1000, 'unixepoch') LIMIT 1")
    suspend fun getAttendanceByTypeAndDate(userId: String, type: String, date: Long): Attendance?
} 