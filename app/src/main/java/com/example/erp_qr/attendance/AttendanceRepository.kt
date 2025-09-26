package com.example.erp_qr.attendance

import com.example.erp_qr.data.AttendanceRecordDTO
import com.example.erp_qr.retrofit.NetworkService
import javax.inject.Inject


class AttendanceRepository @Inject constructor(
    private val api: NetworkService
) {
    suspend fun getAttendanceList(employeeId: String, month: String): List<AttendanceRecordDTO> {
        return api.getAttendanceList(employeeId, month)
    }
}