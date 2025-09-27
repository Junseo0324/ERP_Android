package com.example.erp_qr.retrofit

import com.example.erp_qr.data.AttendanceRecordDTO
import com.example.erp_qr.data.NotificationDTO
import com.example.erp_qr.data.SalaryDTO
import com.example.erp_qr.data.VacationDTO
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NetworkService {
    @FormUrlEncoded
    @POST("/android/login")
    suspend fun login(@Field("employeeNumber") employeeNumber: String,
              @Field("email") email: String): Map<String,Any>

    @GET("/record/android/list/{employeeId}/{month}")
    suspend fun getAttendanceList(@Path("employeeId") employeeId: String,@Path("month") month: String): List<AttendanceRecordDTO>

    @GET("/vacation/android/{employeeId}")
    suspend fun getVacationList(@Path("employeeId") employeeId: String): List<VacationDTO>

    @GET("/salary/android/{employeeId}/{month}")
    suspend fun getSalaryList(@Path("employeeId") employeeId: String,@Path("month") month: String): SalaryDTO

    @GET("/notification/unread/{employeeId}")
    suspend fun getUnreadNotificationList(@Path("employeeId") employeeId: String): List<NotificationDTO>

    @POST("/notification/read/{notificationId}")
    suspend fun markNotificationAsRead(@Path("notificationId") notificationId: Long): Unit

}