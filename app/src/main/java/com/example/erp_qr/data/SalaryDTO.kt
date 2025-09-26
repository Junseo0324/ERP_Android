package com.example.erp_qr.data

data class SalaryDTO(
    var employeeId: String,
    var monthlySalary: Double,
    var allowanceDetails: Map<String,Double>,
    var totalAllowance: Double,
    var deductionDetails: Map<String,Double>,
    var totalDeductions: Double
)
