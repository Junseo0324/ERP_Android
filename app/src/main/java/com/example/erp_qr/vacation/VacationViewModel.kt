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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VacationViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val vacationRepository: VacationRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(VacationUiState())
    val uiState: StateFlow<VacationUiState> = _uiState.asStateFlow()


    init {
        loadVacationData()
    }

    private fun loadVacationData() {
        val data = loginRepository.getLoginData()
        val employeeId = data["employeeId"] ?: "No ID Found"

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val list = vacationRepository.getVacationList(employeeId)
                _uiState.update {
                    it.copy(
                        vacations = list,
                        filteredVacations = list,
                        statusCount = buildStatusCount(list),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    private fun buildStatusCount(list: List<VacationDTO>): Map<String, Int> {
        return mapOf(
            "ALL" to list.size,
            "APPROVED" to list.count { it.status == "APPROVED" },
            "PENDING" to list.count { it.status == "PENDING" },
            "REJECTED" to list.count { it.status == "REJECTED" }
        )
    }

    fun filterVacations(query: String) {
        val list = _uiState.value.vacations
        val filtered = if (query.isBlank()) {
            list
        } else {
            list.filter { vacation ->
                vacation.leaveItemName.contains(query, ignoreCase = true) ||
                        vacation.reason.contains(query, ignoreCase = true) ||
                        vacation.displayStatus.contains(query, ignoreCase = true)
            }
        }
        _uiState.update { it.copy(filteredVacations = filtered) }
    }

    fun filterByStatus(status: String?) {
        val list = _uiState.value.vacations
        val filtered = when (status) {
            null, "ALL" -> list
            "APPROVED" -> list.filter { it.status == "APPROVED" }
            "PENDING" -> list.filter { it.status == "PENDING" }
            "REJECTED" -> list.filter { it.status == "REJECTED" }
            else -> list
        }
        _uiState.update { it.copy(filteredVacations = filtered) }
    }


}