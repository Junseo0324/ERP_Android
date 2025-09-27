package com.example.erp_qr.vacation

import com.example.erp_qr.data.VacationDTO

data class VacationUiState(
    val vacations: List<VacationDTO> = emptyList(),
    val filteredVacations: List<VacationDTO> = emptyList(),
    val statusCount: Map<String, Int> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)