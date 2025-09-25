package com.example.erp_qr.adapter

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.erp_qr.databinding.ItemSalaryDetailBinding

class SalaryDetailAdapter(
    private val isAllowance: Boolean
) : ListAdapter<Map.Entry<String, Double>, SalaryDetailAdapter.SalaryDetailViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalaryDetailViewHolder {
        val binding = ItemSalaryDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SalaryDetailViewHolder(binding, isAllowance)
    }

    override fun onBindViewHolder(holder: SalaryDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SalaryDetailViewHolder(
        private val binding: ItemSalaryDetailBinding,
        private val isAllowance: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: Map.Entry<String, Double>) {
            val (name, amount) = entry

            // 항목명
            binding.textItemName.text = name

            // 금액 포맷
            val formatted = "₩%,d".format(amount.toInt())

            if (isAllowance) {
                binding.textItemAmount.text = "+$formatted"
                binding.textItemAmount.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.holo_green_light)
                )
            } else {
                binding.textItemAmount.text = "-$formatted"
                binding.textItemAmount.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.holo_red_light) // 빨강색
                )
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Map.Entry<String, Double>>() {
        override fun areItemsTheSame(oldItem: Map.Entry<String, Double>, newItem: Map.Entry<String, Double>) =
            oldItem.key == newItem.key

        override fun areContentsTheSame(oldItem: Map.Entry<String, Double>, newItem: Map.Entry<String, Double>) =
            oldItem == newItem
    }
}
