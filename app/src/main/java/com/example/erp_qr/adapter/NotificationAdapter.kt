package com.example.erp_qr.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.erp_qr.data.NotificationDTO
import com.example.erp_qr.databinding.ItemNotificationBinding

class NotificationAdapter(private val onItemClick: (NotificationDTO) -> Unit) : ListAdapter<NotificationDTO, NotificationAdapter.NotificationViewHolder>(DiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        var binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationViewHolder(binding,onItemClick)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NotificationViewHolder(private val binding: ItemNotificationBinding,private val onItemClick: (NotificationDTO) -> Unit) : RecyclerView.ViewHolder(binding.root){
        fun bind(notification: NotificationDTO){
            binding.textNotificationTitle.text = notification.displayType
            binding.tagNotificationStatus.text = notification.displayType

            binding.textNotificationMessage.text = notification.displayMessage

            binding.iconNotificationType.setBackgroundResource(notification.iconBackgroundRes)
            binding.iconNotificationType.setImageResource(notification.iconRes)
            binding.textNotificationTime.text = notification.createdAt


            binding.root.setOnClickListener {
                onItemClick(notification)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<NotificationDTO>() {
        override fun areItemsTheSame(oldItem: NotificationDTO, newItem: NotificationDTO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NotificationDTO, newItem: NotificationDTO): Boolean {
            return oldItem == newItem
        }

    }

}