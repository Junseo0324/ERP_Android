package com.example.erp_qr.attendance

import com.example.erp_qr.data.AttendanceRecordDTO

data class AttendanceUiState(
    val isLoading: Boolean = false,
    val attendanceData: List<AttendanceRecordDTO>? = null,
    val currentMonth: String = "",
    val errorMessage: String? = null
)
