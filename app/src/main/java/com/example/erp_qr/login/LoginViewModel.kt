package com.example.erp_qr.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erp_qr.retrofit.RetrofitProvider.networkService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel(){

    var employeeNumber = MutableStateFlow("")
    var email = MutableStateFlow("")

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState


    fun login() {
        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)

            try {
                val result = networkService.login(employeeNumber.value, email.value)
                val success = result["success"] as? Boolean ?: false

                if (success) {
                    loginRepository.saveLoginData(
                        employeeId = result["employeeId"]?.toString() ?: "",
                        employeeNumber = employeeNumber.value,
                        email = email.value,
                        name = result["employeeName"]?.toString() ?: "",
                        department = result["department"]?.toString() ?: "",
                        position = result["position"]?.toString() ?: "",
                        photo = result["photo"]?.toString() ?: "",
                        companyName = result["companyName"]?.toString() ?: ""
                    )
                    _uiState.value = LoginUiState(isSuccess = true)
                } else {
                    _uiState.value = LoginUiState(errorMessage = "로그인 실패")
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState(errorMessage = "네트워크 오류: ${e.message}")
            }
        }
    }

}