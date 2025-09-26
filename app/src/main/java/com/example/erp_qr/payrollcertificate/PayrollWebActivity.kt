package com.example.erp_qr.payrollcertificate

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.erp_qr.R
import com.example.erp_qr.databinding.ActivityPayrollWebBinding

class PayrollWebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityPayrollWebBinding.inflate(layoutInflater)

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.webViewClient = WebViewClient()

        // 전달받은 월 (예: 2025-09)
        val month = intent.getStringExtra("month") ?: "2025-09"

        binding.webView.loadUrl("http://서버주소:8080/certificate/salary?month=$month")
    }
}