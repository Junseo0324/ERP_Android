package com.example.erp_qr.salary

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erp_qr.data.SalaryDTO
import com.example.erp_qr.login.LoginRepository
import com.example.erp_qr.retrofit.RetrofitProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class SalaryViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val salaryRepository: SalaryRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(SalaryUiState())
    val uiState: StateFlow<SalaryUiState> = _uiState


    init {
        val data = loginRepository.getLoginData()
        val employeeId = data["employeeId"] ?: "No ID Found"
        val todayMonth = getMonth()
        _uiState.update { it.copy(currentMonth = todayMonth) }
        loadSalaryData(employeeId, todayMonth)
    }

    private fun loadSalaryData(employeeId: String, month: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val salary = salaryRepository.getSalaryList(employeeId, month)
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        salaryData = salary,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading salary: ${e.message}")
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMonth(): String {
        val currentDate = LocalDate.now()
        val year = currentDate.year
        val month = currentDate.monthValue.toString().padStart(2, '0')
        return "$year-$month"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun changeMonth(increment: Int) {
        val currentDate = LocalDate.parse("${_uiState.value.currentMonth}-01")
        val newDate = currentDate.plusMonths(increment.toLong())
        val newMonth = "${newDate.year}-${newDate.monthValue.toString().padStart(2, '0')}"

        val employeeId = loginRepository.getLoginData()["employeeId"] ?: "No ID Found"
        _uiState.update { it.copy(currentMonth = newMonth) }
        loadSalaryData(employeeId, newMonth)
    }

    
    companion object{
        private const val TAG = "SalaryViewModel"
    }

}

