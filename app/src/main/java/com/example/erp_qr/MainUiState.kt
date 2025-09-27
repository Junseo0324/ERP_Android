package com.example.erp_qr

import com.example.erp_qr.data.NotificationDTO

data class MainUiState(
    val employeeName: String = "",
    val department: String = "",
    val notifications: List<NotificationDTO> = emptyList(),
    val unreadCount: Int = 0,
    val isLoggedOut: Boolean = false,
    val openNotification: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
