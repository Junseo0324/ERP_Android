package com.example.erp_qr.qr

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.erp_qr.login.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class MainFragmentViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {
    private val _employeeID = MutableLiveData<String>()
    val employeeID: LiveData<String> get() = _employeeID


    private val _companyName = MutableLiveData<String>()
    val companyName: LiveData<String> get() = _companyName

    private val _today = MutableLiveData<String>()
    val today: LiveData<String> get() = _today


    init {
        loadEmployeeData()
        loadTodayDate()
    }

    private fun loadEmployeeData() {
        val data = loginRepository.getLoginData()
        _employeeID.value = data["employeeId"] ?: "No ID Found"
        _companyName.value = data["companyName"] ?: "No company Found"
    }

    private fun loadTodayDate() {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 EEEE", Locale.KOREAN)
        _today.value = today.format(formatter)
    }
}