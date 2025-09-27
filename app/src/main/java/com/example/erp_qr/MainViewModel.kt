package com.example.erp_qr

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erp_qr.data.NotificationDTO
import com.example.erp_qr.login.LoginRepository
import com.example.erp_qr.notification.NotificationRepository
import com.example.erp_qr.retrofit.RetrofitProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _employeeName = MutableLiveData<String>()
    val employeeName: MutableLiveData<String> get() = _employeeName
    private val _department = MutableLiveData<String>()
    val department: MutableLiveData<String> get() = _department
    private val _position = MutableLiveData<String>()
    val position: MutableLiveData<String> get() = _position
    private val _photo = MutableLiveData<String>()
    val photo: MutableLiveData<String> get() = _photo

    private var _notificationData = MutableLiveData<List<NotificationDTO>>()
    val notificationData: MutableLiveData<List<NotificationDTO>> get() = _notificationData

    private val _unreadCount = MutableLiveData<Int>()
    val unreadCount: MutableLiveData<Int> get() = _unreadCount

    // 로그아웃 상태 관리
    val isLoggedOut = MutableLiveData<Boolean>(false)
    val notification = MutableLiveData<Boolean>(false)

    init {
        loadUserData()
        refreshUnreadNotifications()

    }

    private fun loadUserData(){
        val employee = loginRepository.getLoginData()
        _employeeName.value = employee["name"] ?: ""
        _department.value = employee["department"] ?: ""
        _position.value = employee["position"] ?: ""
        _photo.value = employee["photo"] ?: ""
    }

    fun logout(){
        loginRepository.deleteData()
        isLoggedOut.value = true
    }

    fun notification(){
        notification.value = true
    }

    fun refreshUnreadNotifications() {
        viewModelScope.launch {
            try {
                val list = notificationRepository.getUnreadNotifications()
                _notificationData.value = list
                _unreadCount.value = list.size
            } catch (e: Exception) {
                _notificationData.value = emptyList()
                _unreadCount.value = 0
                Log.d(TAG, "refreshUnreadNotifications error: ${e.message}")
            }
        }
    }



    companion object{
        private const val TAG = "MainViewModel"
    }

}