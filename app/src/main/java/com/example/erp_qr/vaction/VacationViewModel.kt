package com.example.erp_qr.vaction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.erp_qr.data.VacationDTO
import com.example.erp_qr.data.repository.LoginRepository
import com.example.erp_qr.retrofit.RetrofitProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class VacationViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {

    private val _vacationData = MutableLiveData<List<VacationDTO>>()
    val vacationData: LiveData<List<VacationDTO>> get() = _vacationData

    init {
        val data = loginRepository.getLoginData()
        val employeeId = data["employeeId"] ?: "No ID Found"
        
        loadVacationData(employeeId)
    }

    private fun loadVacationData(employeeId: String) {
        RetrofitProvider.networkService.getVacationList(employeeId).clone()?.enqueue(object : Callback<List<VacationDTO>>{
            override fun onResponse(call: Call<List<VacationDTO>>, response: Response<List<VacationDTO>>) {
               if(response.isSuccessful){
                   if(response.body() != null){
                       _vacationData.value = response.body()
                   }else{
                       Log.d(TAG, "onResponse: responseBody is null : ${response.body()}")
                   }
               }else{
                   Log.d(TAG, "onResponse: Response Failed: Code = ${response.code()}, Message = ${response.message()}, ErrorBody = ${response.errorBody()?.string()}")
               }
            }

            override fun onFailure(call: Call<List<VacationDTO>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }
    
    companion object{
        private const val TAG = "VacationViewModel"
    }
}
