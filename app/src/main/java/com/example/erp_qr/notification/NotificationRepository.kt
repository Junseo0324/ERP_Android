package com.example.erp_qr.notification

import com.example.erp_qr.data.NotificationDTO
import com.example.erp_qr.login.LoginRepository
import com.example.erp_qr.retrofit.NetworkService
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val networkService: NetworkService,
    private val loginRepository: LoginRepository
) {
    suspend fun getUnreadNotifications(): List<NotificationDTO> {
        val employeeId = loginRepository.getLoginData()["employeeId"] ?: ""
        if (employeeId.isEmpty()) return emptyList()
        return networkService.getUnreadNotificationList(employeeId)
    }

    suspend fun markAsRead(notificationId: Long) {
        networkService.markNotificationAsRead(notificationId)
    }
}