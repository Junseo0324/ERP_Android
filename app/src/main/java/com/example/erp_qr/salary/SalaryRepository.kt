package com.example.erp_qr.salary

import com.example.erp_qr.data.SalaryDTO
import com.example.erp_qr.retrofit.RetrofitProvider
import javax.inject.Inject


class SalaryRepository @Inject constructor() {
    private val service = RetrofitProvider.networkService

    suspend fun getSalaryList(employeeId: String, month: String): SalaryDTO {
        return service.getSalaryList(employeeId, month)
    }
}