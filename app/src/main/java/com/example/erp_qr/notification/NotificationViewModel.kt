package com.example.erp_qr.notification

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
class NotificationViewModel @Inject constructor(private val loginRepository: LoginRepository)
    : ViewModel()
{

        private var _notificationData = MutableLiveData<List<NotificationDTO>>()
        val notificationData: MutableLiveData<List<NotificationDTO>> get() = _notificationData


    init {
        getNotificationUnreadData()
    }


    private fun getNotificationUnreadData(){
        val data = loginRepository.getLoginData()
        val employeeId = data["employeeId"] ?: "No ID Found"
        RetrofitProvider.networkService.getUnreadNotificationCount(employeeId).clone()?.enqueue(object : Callback<List<NotificationDTO>>{
           override fun onResponse(call: Call<List<NotificationDTO>>, response: Response<List<NotificationDTO>>) {
               if(response.isSuccessful){
                   _notificationData.value = response.body() ?: emptyList()
               }else{
                   Log.d(TAG, "Response Failed: Code = ${response.code()}, Message = ${response.message()}, ErrorBody = ${response.errorBody()?.string()}")
               }
           }

           override fun onFailure(call: Call<List<NotificationDTO>>, t: Throwable) {
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
                }

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "NotificationViewModel"
    }
}