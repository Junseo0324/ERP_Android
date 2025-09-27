package com.example.erp_qr

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.erp_qr.data.NotificationDTO
import com.example.erp_qr.login.LoginRepository
import com.example.erp_qr.retrofit.RetrofitProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginRepository: LoginRepository
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


    // 로그아웃 상태 관리
    val isLoggedOut = MutableLiveData<Boolean>(false)

    val notification = MutableLiveData<Boolean>(false)
    
    private val _unreadCount = MutableLiveData<Int>()
    val unreadCount: MutableLiveData<Int> get() = _unreadCount

    init {
        isLoggedOut.value = false
        notification.value = false
        loadUserData()
        getUnreadNotificationCountAndData()

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
        isLoggedOut.value = true // 로그아웃 상태로 변경
    }

    fun notification(){
        notification.value = true
    }
    
    fun getUnreadNotificationCountAndData(){
        val employeeId = loginRepository.getLoginData()["employeeId"] ?: "No ID Found"
        RetrofitProvider.networkService.getUnreadNotificationCount(employeeId).clone()?.enqueue(object : Callback<List<NotificationDTO>>{
            override fun onResponse(call: Call<List<NotificationDTO>>, response: Response<List<NotificationDTO>>) {
                if (response.isSuccessful){
                    _notificationData.value = response.body() ?: emptyList()
                    _unreadCount.value = response.body()?.size ?: 0
                    Log.d(TAG, "onResponse: unReadCount 값 ${_unreadCount.value}")
                }
            }

            override fun onFailure(call: Call<List<NotificationDTO>>, t: Throwable) {
                _unreadCount.value = 0
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun markNotificationAsRead(notificationId: Long){
        Log.d(TAG, "markNotificationAsRead: call id : $notificationId")
        RetrofitProvider.networkService.markNotificationAsRead(notificationId).clone()?.enqueue(object :Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful) {
                    Log.d(TAG, "onResponse: $notificationId")

                    val updatedList = _notificationData.value?.filter { it.id != notificationId } ?: emptyList()
                    Log.d("ViewModel", "Updated list: $updatedList")
                    _notificationData.postValue(updatedList)// 변경된 데이터로 LiveData 갱신
                    _unreadCount.value = updatedList?.size
                    Log.d(TAG, "onResponse: unreadCountValue${_unreadCount.value}")

                }

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }



    companion object{
        private const val TAG = "MainViewModel"
    }

}