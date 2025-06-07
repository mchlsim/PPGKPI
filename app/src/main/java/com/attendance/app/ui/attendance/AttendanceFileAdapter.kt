package com.attendance.app.ui.attendance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.attendance.app.data.model.AttendanceFile
import com.attendance.app.databinding.ItemAttendanceFileBinding
import java.text.SimpleDateFormat
import java.util.*

class AttendanceFileAdapter(
    private val onItemClick: (AttendanceFile) -> Unit,
    private val onExportClick: (AttendanceFile) -> Unit
) : ListAdapter<AttendanceFile, AttendanceFileAdapter.AttendanceFileViewHolder>(AttendanceFileDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceFileViewHolder {
        val binding = ItemAttendanceFileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AttendanceFileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceFileViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AttendanceFileViewHolder(
        private val binding: ItemAttendanceFileBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale("id"))

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }

            binding.btnExport.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onExportClick(getItem(position))
                }
            }
        }

        fun bind(attendanceFile: AttendanceFile) {
            binding.tvFileName.text = attendanceFile.name
            binding.tvDateTime.text = dateFormat.format(Date(attendanceFile.date))
        }
    }

    private class AttendanceFileDiffCallback : DiffUtil.ItemCallback<AttendanceFile>() {
        override fun areItemsTheSame(oldItem: AttendanceFile, newItem: AttendanceFile): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AttendanceFile, newItem: AttendanceFile): Boolean {
            return oldItem == newItem
        }
    }
} 