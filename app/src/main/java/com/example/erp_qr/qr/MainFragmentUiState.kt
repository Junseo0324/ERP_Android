package com.example.erp_qr.qr

data class MainFragmentUiState(
    val employeeId: String = "",
    val companyName: String = "",
    val today: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)