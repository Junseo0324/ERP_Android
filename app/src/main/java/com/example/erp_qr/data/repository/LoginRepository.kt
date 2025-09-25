package com.example.erp_qr.data.repository

import android.content.SharedPreferences
import javax.inject.Inject

class LoginRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun saveLoginData(employeeId: String,employeeNumber: String, email: String, name: String, department: String, position: String, photo: String,companyName: String) {
        val editor = sharedPreferences.edit()
        editor.putString("employeeId", employeeId)
        editor.putString("employeeNumber", employeeNumber)
        editor.putString("email", email)
        editor.putString("name", name)
        editor.putString("department", department)
        editor.putString("position", position)
        editor.putString("photo", photo)
        editor.putString("companyName", companyName)
        editor.apply()
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