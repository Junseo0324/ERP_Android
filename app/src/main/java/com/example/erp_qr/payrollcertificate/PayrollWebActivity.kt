package com.example.erp_qr.payrollcertificate

import android.content.ContentValues
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.erp_qr.R
import com.example.erp_qr.databinding.ActivityPayrollWebBinding
import com.example.erp_qr.retrofit.RetrofitProvider

class PayrollWebActivity : AppCompatActivity() {
    lateinit var binding: ActivityPayrollWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPayrollWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.webViewClient = WebViewClient()

        val month = intent.getStringExtra("month") ?: "2025-09"

        binding.webView.loadUrl("${RetrofitProvider.BASE_URL}/certificate/salary?month=$month")

        supportActionBar?.apply {
            title = "급여 명세서"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_payroll_web, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_save_pdf -> {
                saveWebViewAsPdf()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveWebViewAsPdf() {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(binding.webView.width, binding.webView.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        binding.webView.draw(page.canvas)
        pdfDocument.finishPage(page)

        val fileName = "급여명세서_${System.currentTimeMillis()}.pdf"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val resolver = contentResolver
        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

        try {
            uri?.let {
                resolver.openOutputStream(it)?.use { output ->
                    pdfDocument.writeTo(output)
                    Toast.makeText(this, "PDF 저장 완료: Downloads/$fileName", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "PDF 저장 실패: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            pdfDocument.close()
        }
    }
}

