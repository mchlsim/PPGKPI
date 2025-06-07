package com.attendance.app.ui.attendance

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.attendance.app.data.AppDatabase
import com.attendance.app.data.model.AttendanceFile
import kotlinx.coroutines.launch

class AttendanceViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val attendanceFileDao = database.attendanceFileDao()

    private val _attendanceFiles = MutableLiveData<List<AttendanceFile>>()
    val attendanceFiles: LiveData<List<AttendanceFile>> = _attendanceFiles

    private val _years = MutableLiveData<List<Int>>()
    val years: LiveData<List<Int>> = _years

    fun loadAttendanceFiles(year: Int, month: Int) {
        viewModelScope.launch {
            val files = attendanceFileDao.getAttendanceFilesByYearAndMonth(year, month)
            _attendanceFiles.value = files
        }
    }

    fun loadYears() {
        viewModelScope.launch {
            val yearsList = attendanceFileDao.getAllYears()
            _years.value = yearsList
        }
    }

    fun createAttendanceFile(attendanceFile: AttendanceFile) {
        viewModelScope.launch {
            attendanceFileDao.insert(attendanceFile)
            loadAttendanceFiles(attendanceFile.year, attendanceFile.month)
        }
    }

    fun updateAttendanceFile(attendanceFile: AttendanceFile) {
        viewModelScope.launch {
            attendanceFileDao.update(attendanceFile)
            loadAttendanceFiles(attendanceFile.year, attendanceFile.month)
        }
    }

    fun deleteAttendanceFile(attendanceFile: AttendanceFile) {
        viewModelScope.launch {
            attendanceFileDao.delete(attendanceFile)
            loadAttendanceFiles(attendanceFile.year, attendanceFile.month)
        }
    }
} 