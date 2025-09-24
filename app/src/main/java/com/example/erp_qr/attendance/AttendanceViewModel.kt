package com.example.erp_qr.attendance

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.erp_qr.data.AttendanceRecordDTO
import com.example.erp_qr.data.repository.LoginRepository
import com.example.erp_qr.retrofit.RetrofitProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import org.threeten.bp.LocalDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel(){

    private val _attendanceData = MutableLiveData<List<AttendanceRecordDTO>>()
    val attendanceData: LiveData<List<AttendanceRecordDTO>> get() = _attendanceData

    private val _filteredAttendance = MutableLiveData<List<AttendanceRecordDTO>>() // 필터링된 데이터
    val filteredAttendance: MutableLiveData<List<AttendanceRecordDTO>> get() = _filteredAttendance

    private var currentMonth: String = ""


    // **새로운 월 데이터를 가져오는 메서드**
    fun loadAttendanceForMonth(month: String) {
        val data = loginRepository.getLoginData()
        val employeeId = data["employeeId"] ?: "No ID Found"
        currentMonth = month
        loadAttendanceData(employeeId, currentMonth)
    }

    init {
        val data = loginRepository.getLoginData()
        val employeeId = data["employeeId"] ?: "No ID Found"
        val currentMonth = LocalDate.now().monthValue.toString()
        Log.d(TAG, "currentMonth: $currentMonth")
        loadAttendanceData(employeeId,currentMonth)
    }

    private fun loadAttendanceData(employeeId: String,month: String){
        RetrofitProvider.networkService.getAttendanceList(employeeId,month).clone()?.enqueue(object : Callback<List<AttendanceRecordDTO>>{
            override fun onResponse(call: Call<List<AttendanceRecordDTO>>, response: Response<List<AttendanceRecordDTO>>) {
                if (response.isSuccessful) {
                    _attendanceData.value = response.body() ?: emptyList()
                    _filteredAttendance.value = response.body() ?: emptyList() // 기본값 설정
                } else {
                    Log.d(TAG, "Response Failed: Code = ${response.code()}, Message = ${response.message()}, ErrorBody = ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<AttendanceRecordDTO>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterAttendanceByDateRange(startDate:LocalDate, endDate: LocalDate) {
        val filtered = _attendanceData.value?.filter {
            val parts = it.date.split("-")
            val month = parts[0].toInt()
            val day = parts[1].toInt()
            val recordDate = LocalDate.of(LocalDate.now().year, month, day)
            recordDate in startDate..endDate
        }
        _filteredAttendance.value = filtered!!
    }

    fun filterAttendanceByDate(selectedDate: LocalDate) {
        val filtered = _attendanceData.value?.filter {
            val parts = it.date.split("-")
            val month = parts[0].toInt()
            val day = parts[1].toInt()

            // 서버 데이터의 mm-dd를 LocalDate로 변환
            val recordDate = LocalDate.of(LocalDate.now().year, month, day)

            // 선택한 날짜와 동일한지 확인
            recordDate == selectedDate
        }
        _filteredAttendance.value = filtered!!
    }


    companion object{
        private const val TAG = "AttendanceViewModel"
    }



}