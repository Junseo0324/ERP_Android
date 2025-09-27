package com.example.erp_qr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erp_qr.login.LoginRepository
import com.example.erp_qr.notification.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()


    init {
        loadUserData()
        refreshUnreadNotifications()

    }

    private fun loadUserData(){
        val employee = loginRepository.getLoginData()
        _uiState.update { state ->
            state.copy(
                employeeName = employee["name"] ?: "",
                department = employee["department"] ?: "",
            )
        }
    }

    fun logout(){
        loginRepository.deleteData()
        _uiState.update { it.copy(isLoggedOut = true) }
    }

    fun openNotification() {
        _uiState.update { it.copy(openNotification = true) }
    }

    fun refreshUnreadNotifications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val list = notificationRepository.getUnreadNotifications()
                _uiState.update { it.copy(
                    notifications = list,
                    unreadCount = list.size,
                    isLoading = false
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    notifications = emptyList(),
                    unreadCount = 0,
                    isLoading = false,
                    errorMessage = e.message
                ) }
            }
        }
    }

    fun clearNotificationEvent() {
        _uiState.update { it.copy(openNotification = false) }
    }


}