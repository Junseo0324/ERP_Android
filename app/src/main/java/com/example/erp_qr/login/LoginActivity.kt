package com.example.erp_qr.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.erp_qr.MainActivity
import com.example.erp_qr.R
import com.example.erp_qr.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeState()
    }

    private fun setupUI() {
        binding.buttonLogin.setOnClickListener {
            loginViewModel.employeeNumber.value = binding.editTextEmployeeId.text.toString()
            loginViewModel.email.value = binding.editTextEmail.text.toString()
            loginViewModel.login()
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            loginViewModel.uiState.collect { state ->
                when {
                    state.isLoading -> {

                    }
                    state.isSuccess -> {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        })
                    }
                    state.errorMessage != null -> {

                    }
                }
            }
        }
    }
}