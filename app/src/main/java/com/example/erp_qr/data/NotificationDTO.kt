package com.example.erp_qr.data

import com.example.erp_qr.R

data class NotificationDTO(
    var id: Long,
    var isRead: Boolean,
    var type: String,
    var typeId: String,
    var createdAt: String,
    var employeeId: Long
){
    val displayType: String
        get() = when (type) {
            "vacation_pending" -> "휴가 신청"
            "vacation_approve" -> "휴가 승인"
            "attendance_checkIn" -> "출근"
            "attendance_checkout" -> "퇴근"
            else -> "알림"
        }

    val displayMessage: String
        get() = when(type){
            "vacation_pending" -> "휴가 신청이 완료되었습니다."
            "vacation_approve" -> "휴가 승인이 완료되었습니다."
            "attendance_checkIn" -> "출근이 완료되었습니다."
            "attendance_checkout" -> "퇴근이 완료되었습니다."
            else -> "새로운 알림 도착"
        }

    val iconBackgroundRes: Int
        get() = when (type) {
            "attendance_checkIn" -> R.drawable.tag_attendance_background
            "attendance_checkout" -> R.drawable.tag_leave_background
            "vacation_approve" -> R.drawable.tag_vacation_background
            "vacation_pending" -> R.drawable.tag_vacation_application_background
            else -> R.drawable.tag_attendance_background
        }

    val iconRes: Int
        get() = when (type) {
            "attendance_checkIn" -> R.drawable.ic_login
            "attendance_checkout" -> R.drawable.ic_exit
            "vacation_approve" -> R.drawable.ic_vacation
            "vacation_pending" -> R.drawable.ic_vacation
            else -> R.drawable.ic_notification
        }
}