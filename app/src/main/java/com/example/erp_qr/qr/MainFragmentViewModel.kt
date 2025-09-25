package com.example.erp_qr.qr

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.erp_qr.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {
    private val _employeeID = MutableLiveData<String>()
    val employeeID: LiveData<String> get() = _employeeID


    private val _companyName = MutableLiveData<String>()
    val companyName: LiveData<String> get() = _companyName


    init {
        loadEmployeeData()
    }

    private fun loadEmployeeData() {
        val data = loginRepository.getLoginData()
        _employeeID.value = data["employeeId"] ?: "No ID Found"
        _companyName.value = data["companyName"] ?: "No company Found"
    }
}