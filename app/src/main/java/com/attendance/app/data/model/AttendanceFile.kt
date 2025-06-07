package com.attendance.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance_files")
data class AttendanceFile(
    @PrimaryKey
    val id: String,
    val name: String, // e.g., "Kegiatan Sabtu"
    val year: Int,
    val month: Int, // 1-12
    val date: Long, // timestamp of the activity
    val createdAt: Long = System.currentTimeMillis()
) 