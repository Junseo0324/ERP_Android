package com.example.erp_qr.notification

import com.example.erp_qr.data.NotificationDTO

data class NotificationUiState(
    val isLoading: Boolean = false,
    val notifications: List<NotificationDTO> = emptyList(),
    val unreadCount: Int = 0,
    val errorMessage: String? = null
)
