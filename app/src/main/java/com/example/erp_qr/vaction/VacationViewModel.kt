package com.example.erp_qr.vaction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erp_qr.data.VacationDTO
import com.example.erp_qr.data.repository.LoginRepository
import com.example.erp_qr.retrofit.RetrofitProvider
import com.example.erp_qr.vacation.VacationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class VacationViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val vacationRepository: VacationRepository) : ViewModel() {

    private val _vacationData = MutableLiveData<List<VacationDTO>>()
    val vacationData: LiveData<List<VacationDTO>> get() = _vacationData

    init {
        val data = loginRepository.getLoginData()
        val employeeId = data["employeeId"] ?: "No ID Found"
        viewModelScope.launch {
            try {
                _vacationData.value = vacationRepository.getVacationList(employeeId)
            } catch (e: Exception) {
                Log.d("VacationViewModel", "Error: ${e.message}")
            }
        }
    }
    
}
