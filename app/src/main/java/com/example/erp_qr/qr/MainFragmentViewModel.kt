package com.example.erp_qr.qr

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.erp_qr.login.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class MainFragmentViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainFragmentUiState())
    val uiState: StateFlow<MainFragmentUiState> = _uiState.asStateFlow()


    init {
        loadEmployeeData()
        loadTodayDate()
    }

    private fun loadEmployeeData() {
        _uiState.update { it.copy(isLoading = true) }
        try {
            val data = loginRepository.getLoginData()
            _uiState.update {
                it.copy(
                    employeeId = data["employeeId"] ?: "No ID Found",
                    companyName = data["companyName"] ?: "No company Found",
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

    private fun loadTodayDate() {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 EEEE", Locale.KOREAN)
        _uiState.update { it.copy(today = today.format(formatter)) }
    }
}