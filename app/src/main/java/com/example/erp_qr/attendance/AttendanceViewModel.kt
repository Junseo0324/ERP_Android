package com.example.erp_qr.attendance

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erp_qr.data.AttendanceRecordDTO
import com.example.erp_qr.data.repository.LoginRepository
import com.example.erp_qr.retrofit.RetrofitProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val repository: AttendanceRepository
) : ViewModel(){

    private val _attendanceData = MutableLiveData<List<AttendanceRecordDTO>>()
    val attendanceData: LiveData<List<AttendanceRecordDTO>> get() = _attendanceData

    private var currentMonth: String = ""


    init {
        val data = loginRepository.getLoginData()
        val employeeId = data["employeeId"] ?: "No ID Found"
        val currentMonth = LocalDate.now().monthValue.toString()
        Log.d(TAG, "currentMonth: $currentMonth")
        loadAttendanceData(employeeId, currentMonth)
    }

    fun loadAttendanceData(employeeId: String, month: String) {
        viewModelScope.launch {
            try {
                val result = repository.getAttendanceList(employeeId, month)
                _attendanceData.value = result
            } catch (e: Exception) {
                Log.e(TAG, "Error loading attendance: ${e.message}", e)
                _attendanceData.value = emptyList()
            }
        }
    }

    fun loadAttendanceForMonth(month: String) {
        val data = loginRepository.getLoginData()
        val employeeId = data["employeeId"] ?: "No ID Found"
        currentMonth = month
        loadAttendanceData(employeeId, currentMonth)
    }

    companion object{
        private const val TAG = "AttendanceViewModel"
    }



}