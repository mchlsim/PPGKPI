package com.attendance.app.ui.scan

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.attendance.app.data.AppDatabase
import com.attendance.app.data.model.Attendance
import com.attendance.app.databinding.DialogManualAttendanceBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ManualAttendanceDialog : DialogFragment() {
    private var _binding: DialogManualAttendanceBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: AppDatabase
    private var selectedDate: Calendar = Calendar.getInstance()
    private var selectedTime: Calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogManualAttendanceBinding.inflate(LayoutInflater.from(context))
        database = AppDatabase.getDatabase(requireContext())

        setupDatePicker()
        setupTimePicker()
        setupTypeSpinner()
        loadUsers()

        return AlertDialog.Builder(requireContext())
            .setTitle("Manual Attendance")
            .setView(binding.root)
            .setPositiveButton("Save") { _, _ ->
                saveAttendance()
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    private fun setupDatePicker() {
        binding.etDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    selectedDate.set(year, month, day)
                    updateDateField()
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupTimePicker() {
        binding.etTime.setOnClickListener {
            TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    selectedTime.set(Calendar.HOUR_OF_DAY, hour)
                    selectedTime.set(Calendar.MINUTE, minute)
                    updateTimeField()
                },
                selectedTime.get(Calendar.HOUR_OF_DAY),
                selectedTime.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun setupTypeSpinner() {
        val types = arrayOf("Check In", "Check Out")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.etType.setAdapter(adapter)
    }

    private fun loadUsers() {
        viewLifecycleOwner.lifecycleScope.launch {
            val users = database.userDao().getAllUsers().first()
            val userNames = users.map { it.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, userNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerUser.setAdapter(adapter)
        }
    }

    private fun updateDateField() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.etDate.setText(dateFormat.format(selectedDate.time))
    }

    private fun updateTimeField() {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        binding.etTime.setText(timeFormat.format(selectedTime.time))
    }

    private fun saveAttendance() {
        viewLifecycleOwner.lifecycleScope.launch {
            val selectedUserName = binding.spinnerUser.text.toString()
            val users = database.userDao().getAllUsers().first()
            val selectedUser = users.find { it.name == selectedUserName }

            selectedUser?.let { user ->
                val calendar = Calendar.getInstance()
                calendar.set(
                    selectedDate.get(Calendar.YEAR),
                    selectedDate.get(Calendar.MONTH),
                    selectedDate.get(Calendar.DAY_OF_MONTH),
                    selectedTime.get(Calendar.HOUR_OF_DAY),
                    selectedTime.get(Calendar.MINUTE)
                )

                val attendance = Attendance(
                    userId = user.id,
                    type = binding.etType.text.toString().lowercase(),
                    timestamp = calendar.timeInMillis,
                    notes = binding.etNotes.text.toString()
                )

                database.attendanceDao().insertAttendance(attendance)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 