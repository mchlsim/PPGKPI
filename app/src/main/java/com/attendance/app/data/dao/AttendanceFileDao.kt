package com.attendance.app.data.dao

import androidx.room.*
import com.attendance.app.data.model.AttendanceFile
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceFileDao {
    @Query("SELECT * FROM attendance_files WHERE year = :year ORDER BY month ASC, date ASC")
    fun getFilesByYear(year: Int): Flow<List<AttendanceFile>>

    @Query("SELECT * FROM attendance_files WHERE year = :year AND month = :month ORDER BY date ASC")
    fun getFilesByMonth(year: Int, month: Int): Flow<List<AttendanceFile>>

    @Query("SELECT DISTINCT year FROM attendance_files ORDER BY year DESC")
    fun getAllYears(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(file: AttendanceFile)

    @Update
    suspend fun updateFile(file: AttendanceFile)

    @Delete
    suspend fun deleteFile(file: AttendanceFile)

    @Query("SELECT * FROM attendance_files WHERE id = :fileId")
    suspend fun getFileById(fileId: String): AttendanceFile?
} 