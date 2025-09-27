package com.example.erp_qr.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.erp_qr.data.VacationDTO
import com.example.erp_qr.databinding.ItemVacationBinding

@RequiresApi(Build.VERSION_CODES.O)
class VacationAdapter : ListAdapter<VacationDTO, VacationAdapter.VacationViewHolder>(DiffCallback()) {

    class VacationViewHolder(private val binding: ItemVacationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vacation: VacationDTO) {
            binding.textVacationType.text = vacation.leaveItemName

            binding.textVacationStatus.text = vacation.displayStatus
            binding.textVacationStatus.setTextColor(
                when (vacation.status) {
                    "APPROVED" -> itemView.context.getColor(android.R.color.holo_green_dark)
                    "PENDING" -> itemView.context.getColor(android.R.color.holo_orange_dark)
                    "REJECTED" -> itemView.context.getColor(android.R.color.holo_red_dark)
                    else -> itemView.context.getColor(android.R.color.darker_gray)
                }
            )

            binding.textVacationDate.text = "${vacation.startDate} ~ ${vacation.endDate}"
            binding.textVacationDuration.text = calculateDuration(vacation.startDate, vacation.endDate)

            binding.textApprover.text = "${vacation.name} • ${vacation.position} • ${vacation.department}"

            binding.textVacationReason.text = vacation.reason

            binding.textApplicationNumber.text = "신청번호: #${vacation.id}"
        }


        private fun calculateDuration(start: String, end: String): String {
            return try {
                val startDate = start.split("-")
                val endDate = end.split("-")

                val days = endDate[1].toInt() - startDate[1].toInt()
                "${days}일"
            } catch (e: Exception) {
                "기간 계산 불가"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacationViewHolder {
        val binding = ItemVacationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VacationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<VacationDTO>() {
        override fun areItemsTheSame(oldItem: VacationDTO, newItem: VacationDTO) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: VacationDTO, newItem: VacationDTO) = oldItem == newItem
    }
}