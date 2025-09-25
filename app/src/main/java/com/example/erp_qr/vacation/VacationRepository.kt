package com.example.erp_qr.vacation

import com.example.erp_qr.data.VacationDTO
import com.example.erp_qr.retrofit.NetworkService
import javax.inject.Inject

class VacationRepository @Inject constructor(
    private val networkService: NetworkService
) {
    suspend fun getVacationList(employeeId: String): List<VacationDTO> {
        return networkService.getVacationList(employeeId)
    }
}