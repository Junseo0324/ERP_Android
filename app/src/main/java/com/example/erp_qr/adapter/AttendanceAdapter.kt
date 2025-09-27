package com.example.erp_qr.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.erp_qr.data.AttendanceRecordDTO
import com.example.erp_qr.databinding.ItemAttendanceBinding
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class AttendanceAdapter : ListAdapter<AttendanceRecordDTO, AttendanceAdapter.AttendanceViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val binding = ItemAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AttendanceViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AttendanceViewHolder(private val binding: ItemAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(attendance: AttendanceRecordDTO) {
            val parts = attendance.date.split("-")
            if (parts.size == 2) {
                val month = parts[0].toInt()
                val day = parts[1].toInt()
                val currentYear = LocalDate.now().year

                val recordDate = LocalDate.of(currentYear, month, day)

                binding.textDate.text = "${month}/${day}"
                binding.textDayOfWeek.text = recordDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
            }


            binding.textCheckInTime.text = attendance.checkInTime
            binding.textCheckOutTime.text = attendance.checkOutTime

            binding.textWorkHours.text = attendance.totalWorkHours + "h"

            binding.textAttendanceStatus.text = attendance.attendanceType
            when (attendance.attendanceType) {
                "정상" -> binding.textAttendanceStatus.setTextColor(0xFF27AE60.toInt())
                "지각" -> binding.textAttendanceStatus.setTextColor(0xFFF39C12.toInt())
                "조퇴" -> binding.textAttendanceStatus.setTextColor(0xFF3498DB.toInt())
                "결근" -> binding.textAttendanceStatus.setTextColor(0xFFE74C3C.toInt())
                else -> binding.textAttendanceStatus.setTextColor(0xFF7F8C8D.toInt())
            }

            val noteText = attendance.notes ?: ""
            if (noteText.isNotBlank()) {
                binding.layoutNote.visibility = View.VISIBLE
                binding.textNote.text = noteText
            } else {
                binding.layoutNote.visibility = View.GONE
            }

        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AttendanceRecordDTO>() {
        override fun areItemsTheSame(oldItem: AttendanceRecordDTO, newItem: AttendanceRecordDTO): Boolean {
            return oldItem.recordId == newItem.recordId
        }

        override fun areContentsTheSame(oldItem: AttendanceRecordDTO, newItem: AttendanceRecordDTO): Boolean {
            return oldItem == newItem
        }
    }
}
