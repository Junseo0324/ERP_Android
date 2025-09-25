package com.example.erp_qr.retrofit

import com.example.erp_qr.data.AttendanceRecordDTO
import com.example.erp_qr.data.NotificationDTO
import com.example.erp_qr.data.SalaryDTO
import com.example.erp_qr.data.VacationDTO
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NetworkService {
    @FormUrlEncoded
    @POST("/android/login")
    fun login(@Field("employeeNumber") employeeNumber: String,
              @Field("email") email: String): Call<Map<String,Any>>

    //직원의 근태 조회  EX) localhost:8080/record/android/list/11/10
    @GET("/record/android/list/{employeeId}/{month}")
    fun getAttendanceList(@Path("employeeId") employeeId: String,@Path("month") month: String): Call<List<AttendanceRecordDTO>>

    //직원의 휴가 조회
    @GET("/vacation/android/{employeeId}")
    suspend fun getVacationList(@Path("employeeId") employeeId: String): List<VacationDTO>

    //급여 정보 가져오기
    @GET("/salary/android/{employeeId}/{month}")
    fun getSalaryList(@Path("employeeId") employeeId: String,@Path("month") month: String): Call<SalaryDTO>


    //안읽은 알람
    @GET("/notification/unread/{employeeId}")
    fun getUnreadNotificationCount(@Path("employeeId") employeeId: String): Call<List<NotificationDTO>>

    @POST("/notification/read/{notificationId}")
    fun markNotificationAsRead(@Path("notificationId") notificationId: Long): Call<Void>

}