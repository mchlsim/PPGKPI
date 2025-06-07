package com.attendance.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String,
    val name: String,
    val position: String,
    val qrCode: String, // Unique QR code for each user
    val createdAt: Long = System.currentTimeMillis()
) 