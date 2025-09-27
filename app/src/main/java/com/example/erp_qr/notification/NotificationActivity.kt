package com.example.erp_qr.notification

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.erp_qr.MainViewModel
import com.example.erp_qr.R
import com.example.erp_qr.adapter.NotificationAdapter
import com.example.erp_qr.databinding.ActivityNotificationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        observeState()
        setupClickListeners()

        viewModel.loadNotifications()


    }

    private fun setupRecyclerView() {
        binding.notificaitonRv.adapter = adapter
        binding.notificaitonRv.layoutManager = LinearLayoutManager(this)
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnMarkAllRead.setOnClickListener {
            viewModel.markAllAsRead()
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.progressBar.visibility =
                    if (state.isLoading) View.VISIBLE else View.GONE

                when {
                    state.notifications.isEmpty() && state.errorMessage == null -> {
                        binding.notificaitonRv.visibility = View.GONE
                        binding.emptyView.visibility = View.VISIBLE
                        binding.textEmptyMessage.text = "현재 알림이 없습니다."
                        binding.btnMarkAllRead.visibility = View.GONE
                    }
                    state.errorMessage != null -> {
                        binding.notificaitonRv.visibility = View.GONE
                        binding.emptyView.visibility = View.VISIBLE
                        binding.textEmptyMessage.text = "알림을 불러올 수 없습니다."
                        binding.btnMarkAllRead.visibility = View.GONE
                    }
                    else -> {
                        binding.notificaitonRv.visibility = View.VISIBLE
                        binding.emptyView.visibility = View.GONE
                        binding.btnMarkAllRead.visibility = View.VISIBLE
                        adapter.submitList(state.notifications)
                    }
                }

                binding.textNotificationCount.text = "읽지 않은 알림 ${state.unreadCount}개"
            }
        }
    }
}