package com.example.erp_qr.login

import android.content.SharedPreferences
import com.example.erp_qr.retrofit.NetworkService
import com.example.erp_qr.retrofit.RetrofitProvider.networkService
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val networkService: NetworkService
) {

    suspend fun login(employeeNumber: String, email: String): Map<String, Any> {
        return networkService.login(employeeNumber, email)
    }

    fun saveLoginData(
        employeeId: String, employeeNumber: String, email: String,
        name: String, department: String, position: String,
        photo: String, companyName: String
    ) {
        sharedPreferences.edit()
            .putString("employeeId", employeeId)
            .putString("employeeNumber", employeeNumber)
            .putString("email", email)
            .putString("name", name)
            .putString("department", department)
            .putString("position", position)
            .putString("photo", photo)
            .putString("companyName", companyName)
            .apply()
    }

    fun getLoginData(): Map<String,String> {
        val employeeId = sharedPreferences.getString("employeeId", null)
        val employeeNumber = sharedPreferences.getString("employeeNumber", null)
        val email = sharedPreferences.getString("email", null)
        val name = sharedPreferences.getString("name", null)
        val department = sharedPreferences.getString("department", null)
        val position = sharedPreferences.getString("position", null)
        val photo = sharedPreferences.getString("photo", null)
        val companyName = sharedPreferences.getString("companyName", null)
        return mapOf(
            "employeeId" to (employeeId ?: ""),
            "employeeNumber" to (employeeNumber ?: ""),
            "email" to (email ?: ""),
            "name" to (name ?: ""),
            "department" to (department ?: ""),
            "position" to (position ?: ""),
            "photo" to (photo ?: ""),
            "companyName" to (companyName ?: "")
        )
    }

    fun deleteData() {
        sharedPreferences.edit().clear().apply()
    }


}