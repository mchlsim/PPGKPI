package com.attendance.app.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.attendance.app.R
import com.attendance.app.data.AppDatabase
import com.attendance.app.data.model.Attendance
import com.attendance.app.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: AppDatabase
    private var currentUserId: String = "" // This should be set from login session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        setupAttendanceButtons()
        setupBottomNavigation()
    }

    private fun setupAttendanceButtons() {
        binding.btnCheckIn.setOnClickListener {
            recordAttendance("check_in")
        }

        binding.btnCheckOut.setOnClickListener {
            recordAttendance("check_out")
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_history -> {
                    // Show history fragment
                    true
                }
                R.id.navigation_profile -> {
                    // Show profile fragment
                    true
                }
                else -> false
            }
        }
    }

    private fun recordAttendance(type: String) {
        lifecycleScope.launch {
            val currentTime = System.currentTimeMillis()
            
            // Check if attendance already exists for today
            val existingAttendance = database.attendanceDao()
                .getAttendanceByTypeAndDate(currentUserId, type, currentTime)

            if (existingAttendance != null) {
                val message = if (type == "check_in") {
                    "You have already checked in today"
                } else {
                    "You have already checked out today"
                }
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                return@launch
            }

            val attendance = Attendance(
                userId = currentUserId,
                type = type,
                timestamp = currentTime
            )

            database.attendanceDao().insertAttendance(attendance)

            val message = if (type == "check_in") {
                R.string.msg_check_in_success
            } else {
                R.string.msg_check_out_success
            }
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
} 