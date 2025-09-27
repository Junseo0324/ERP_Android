package com.example.erp_qr.salary

import com.example.erp_qr.data.SalaryDTO

data class SalaryUiState(
    val isLoading: Boolean = false,
    val salaryData: SalaryDTO? = null,
    val currentMonth: String = "",
    val errorMessage: String? = null
)
