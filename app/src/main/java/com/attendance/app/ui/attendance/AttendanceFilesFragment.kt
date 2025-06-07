package com.attendance.app.ui.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.attendance.app.databinding.FragmentAttendanceFilesBinding
import java.util.*

class AttendanceFilesFragment : Fragment() {
    private var _binding: FragmentAttendanceFilesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AttendanceViewModel by viewModels()
    private lateinit var adapter: AttendanceFileAdapter

    private var selectedYear = Calendar.getInstance().get(Calendar.YEAR)
    private var selectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttendanceFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupYearMonthSpinners()
        setupFab()
        observeViewModel()
        loadInitialData()
    }

    private fun setupRecyclerView() {
        adapter = AttendanceFileAdapter(
            onItemClick = { attendanceFile ->
                // TODO: Navigate to attendance details
            },
            onExportClick = { attendanceFile ->
                // TODO: Export to Excel
            }
        )
        binding.rvAttendanceFiles.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AttendanceFilesFragment.adapter
        }
    }

    private fun setupYearMonthSpinners() {
        // Setup year spinner
        viewModel.years.observe(viewLifecycleOwner) { years ->
            val yearAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                years
            )
            binding.actvYear.setAdapter(yearAdapter)
            binding.actvYear.setText(selectedYear.toString(), false)
        }

        // Setup month spinner
        val months = listOf(
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        )
        val monthAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            months
        )
        binding.actvMonth.setAdapter(monthAdapter)
        binding.actvMonth.setText(months[selectedMonth - 1], false)

        // Setup listeners
        binding.actvYear.setOnItemClickListener { _, _, position, _ ->
            selectedYear = viewModel.years.value?.get(position) ?: selectedYear
            loadAttendanceFiles()
        }

        binding.actvMonth.setOnItemClickListener { _, _, position, _ ->
            selectedMonth = position + 1
            loadAttendanceFiles()
        }
    }

    private fun setupFab() {
        binding.fabAddFile.setOnClickListener {
            CreateAttendanceFileDialog().show(
                childFragmentManager,
                "create_attendance_file"
            )
        }
    }

    private fun observeViewModel() {
        viewModel.attendanceFiles.observe(viewLifecycleOwner) { files ->
            adapter.submitList(files)
        }
    }

    private fun loadInitialData() {
        viewModel.loadYears()
        loadAttendanceFiles()
    }

    private fun loadAttendanceFiles() {
        viewModel.loadAttendanceFiles(selectedYear, selectedMonth)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 