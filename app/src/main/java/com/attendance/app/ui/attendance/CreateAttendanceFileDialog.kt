package com.attendance.app.ui.attendance

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.attendance.app.data.model.AttendanceFile
import com.attendance.app.databinding.DialogCreateAttendanceFileBinding
import java.text.SimpleDateFormat
import java.util.*

class CreateAttendanceFileDialog : DialogFragment() {
    private var _binding: DialogCreateAttendanceFileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AttendanceViewModel by viewModels()
    
    private var selectedDate: Calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogCreateAttendanceFileBinding.inflate(layoutInflater)
        
        setupDatePicker()
        setupTimePicker()
        
        return AlertDialog.Builder(requireContext())
            .setTitle("Buat File Absensi Baru")
            .setView(binding.root)
            .setPositiveButton("Buat") { _, _ ->
                createAttendanceFile()
            }
            .setNegativeButton("Batal", null)
            .create()
    }

    private fun setupDatePicker() {
        binding.etDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, day)
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
                    selectedDate.set(Calendar.HOUR_OF_DAY, hour)
                    selectedDate.set(Calendar.MINUTE, minute)
                    updateTimeField()
                },
                selectedDate.get(Calendar.HOUR_OF_DAY),
                selectedDate.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun updateDateField() {
        binding.etDate.setText(dateFormat.format(selectedDate.time))
    }

    private fun updateTimeField() {
        binding.etTime.setText(timeFormat.format(selectedDate.time))
    }

    private fun createAttendanceFile() {
        val fileName = binding.etFileName.text.toString()
        if (fileName.isBlank()) {
            binding.tilFileName.error = "Nama kegiatan harus diisi"
            return
        }

        val attendanceFile = AttendanceFile(
            id = UUID.randomUUID().toString(),
            name = fileName,
            year = selectedDate.get(Calendar.YEAR),
            month = selectedDate.get(Calendar.MONTH) + 1,
            date = selectedDate.timeInMillis
        )

        viewModel.createAttendanceFile(attendanceFile)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 