package com.example.erp_qr.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val list = repository.getUnreadNotifications()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    notifications = list,
                    unreadCount = list.size,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "알림을 불러올 수 없습니다: ${e.message}"
                )
            }
        }
    }

    fun markNotificationAsRead(id: Long) {
        viewModelScope.launch {
            try {
                repository.markAsRead(id)
                val updatedList = _uiState.value.notifications.filter { it.id != id }
                _uiState.value = _uiState.value.copy(
                    notifications = updatedList,
                    unreadCount = updatedList.size
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "읽음 처리 실패: ${e.message}"
                )
            }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            try {
                _uiState.value.notifications.forEach { repository.markAsRead(it.id) }
                _uiState.value = _uiState.value.copy(
                    notifications = emptyList(),
                    unreadCount = 0
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "전체 읽음 처리 실패: ${e.message}"
                )
            }
        }
    }

}
