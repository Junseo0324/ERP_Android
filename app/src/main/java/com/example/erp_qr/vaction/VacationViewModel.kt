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

    private val _filteredData = MutableLiveData<List<VacationDTO>>() // 검색된 데이터
    val filteredData: LiveData<List<VacationDTO>> get() = _filteredData

    init {
        val data = loginRepository.getLoginData()
        val employeeId = data["employeeId"] ?: "No ID Found"
        viewModelScope.launch {
            try {
                val list = vacationRepository.getVacationList(employeeId)
                _vacationData.value = list
                _filteredData.value = list
            } catch (e: Exception) {
                Log.d("VacationViewModel", "Error: ${e.message}")
            }
        }
    }

    fun filterVacations(query: String) {
        val list = _vacationData.value ?: return
        if (query.isBlank()) {
            _filteredData.value = list
        } else {
            _filteredData.value = list.filter { vacation ->
                vacation.leaveItemName.contains(query, ignoreCase = true) ||
                        vacation.reason.contains(query, ignoreCase = true) ||
                        vacation.displayStatus.contains(query, ignoreCase = true)
            }
        }
    }

    
}
