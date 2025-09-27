package com.example.erp_qr.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erp_qr.data.NotificationDTO
import com.example.erp_qr.login.LoginRepository
import com.example.erp_qr.retrofit.RetrofitProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
) : ViewModel() {

    private val _notifications = MutableLiveData<List<NotificationDTO>>()
    val notifications: LiveData<List<NotificationDTO>> = _notifications

    private val _unreadCount = MutableLiveData<Int>()
    val unreadCount: LiveData<Int> = _unreadCount

    fun loadNotifications() {
        viewModelScope.launch {
            val list = repository.getUnreadNotifications()
            _notifications.value = list
            _unreadCount.value = list.size
        }
    }

    fun markNotificationAsRead(id: Long) {
        viewModelScope.launch {
            repository.markAsRead(id)

            val updatedList = _notifications.value?.filter { it.id != id } ?: emptyList()
            _notifications.value = updatedList
            _unreadCount.value = updatedList.size
        }
    }
}
