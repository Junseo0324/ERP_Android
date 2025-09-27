package com.example.erp_qr.vacation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erp_qr.data.VacationDTO
import com.example.erp_qr.login.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VacationViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val vacationRepository: VacationRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(VacationUiState())
    val uiState: StateFlow<VacationUiState> = _uiState.asStateFlow()


    init {
        val data = loginRepository.getLoginData()
        val employeeId = data["employeeId"] ?: "No ID Found"
        viewModelScope.launch {
            try {
                val list = vacationRepository.getVacationList(employeeId)
                _vacationData.value = list
                _filteredData.value = list
                updateStatusCount(list)
            } catch (e: Exception) {
                Log.d("VacationViewModel", "Error: ${e.message}")
            }
        }
    }

    fun updateStatusCount(list: List<VacationDTO>) {
        val counts = mapOf(
            "ALL" to list.size,
            "APPROVED" to list.count { it.status == "APPROVED" },
            "PENDING" to list.count { it.status == "PENDING" },
            "REJECTED" to list.count { it.status == "REJECTED" }
        )
        _statusCount.value = counts
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

    fun filterByStatus(status: String?) {
        val list = _vacationData.value ?: return
        _filteredData.value = when (status) {
            null, "ALL" -> list
            "APPROVED" -> list.filter { it.status == "APPROVED" }
            "PENDING" -> list.filter { it.status == "PENDING" }
            "REJECTED" -> list.filter { it.status == "REJECTED" }
            else -> list
        }
    }


}