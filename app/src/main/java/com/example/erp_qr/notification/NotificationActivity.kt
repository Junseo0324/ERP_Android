package com.example.erp_qr.notification

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.erp_qr.MainViewModel
import com.example.erp_qr.R
import com.example.erp_qr.adapter.NotificationAdapter
import com.example.erp_qr.databinding.ActivityNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()
    private val adapter = NotificationAdapter { notification ->
        viewModel.markNotificationAsRead(notification.id)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        viewModel.loadNotifications()


    }

    private fun setupRecyclerView() {
        binding.notificaitonRv.adapter = adapter
        binding.notificaitonRv.layoutManager = LinearLayoutManager(this)
    }

    private fun setupObservers() {
        viewModel.notifications.observe(this) { notificationList ->
            adapter.submitList(notificationList)
        }

        viewModel.unreadCount.observe(this) { count ->
            binding.textNotificationCount.text = "읽지 않은 알림 ${count}개"
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnMarkAllRead.setOnClickListener {
            val list = viewModel.notifications.value ?: return@setOnClickListener
            list.forEach { notification ->
                viewModel.markNotificationAsRead(notification.id)
            }
        }
    }
}