package com.example.erp_qr.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erp_qr.login.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val attendanceRepository: AttendanceRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState: StateFlow<AttendanceUiState> = _uiState

    init {
        val data = loginRepository.getLoginData()
        val employeeId = data["employeeId"] ?: "No ID Found"
        val currentMonth = LocalDate.now().monthValue.toString()
        loadAttendanceData(employeeId, currentMonth)
    }

    fun loadAttendanceData(employeeId: String, month: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val result = attendanceRepository.getAttendanceList(employeeId, month)
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        attendanceData = result,
                        currentMonth = month,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        attendanceData = emptyList(),
                        errorMessage = e.message ?: "알 수 없는 에러"
                    )
                }
            }
        }
    }

    fun loadAttendanceForMonth(month: String) {
        val data = loginRepository.getLoginData()
        val employeeId = data["employeeId"] ?: "No ID Found"
        loadAttendanceData(employeeId, month)
    }

}