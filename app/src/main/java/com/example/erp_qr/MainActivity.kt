package com.example.erp_qr

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.erp_qr.attendance.AttendanceFragment
import com.example.erp_qr.databinding.ActivityMainBinding
import com.example.erp_qr.salary.SalaryFragment
import com.example.erp_qr.vacation.VacationFragment
import com.example.erp_qr.login.LoginActivity
import com.example.erp_qr.notification.NotificationActivity
import com.example.erp_qr.qr.MainFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupBottomNavigation()
        setupObservers()

        if (savedInstanceState == null) {
            loadFragment(MainFragment())
        }
        viewModel.refreshUnreadNotifications()

        binding.btnNotification.setOnClickListener {
            viewModel.openNotification()
        }
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    fun showToolbar(show: Boolean) {
        binding.toolbar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, fragment)
            .commit()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.textEmployeeName.text = "${state.employeeName} • ${state.department}"

                binding.badgeNotification.text = state.unreadCount.toString()
                binding.badgeNotification.visibility =
                    if (state.unreadCount > 0) View.VISIBLE else View.GONE

                binding.progressBar.visibility =
                    if (state.isLoading) View.VISIBLE else View.GONE

                state.errorMessage?.let { msg ->
                    Log.e("MainActivity", "Error: $msg")
                }

                if (state.isLoggedOut) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

                if (state.openNotification) {
                    startActivity(Intent(this@MainActivity, NotificationActivity::class.java))
                    viewModel.clearNotificationEvent()
                }
            }
        }
    }


    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> loadFragment(MainFragment())
                R.id.nav_vacation -> loadFragment(VacationFragment())
                R.id.nav_attendance -> loadFragment(AttendanceFragment())
                R.id.nav_salary -> loadFragment(SalaryFragment())
            }
            true
        }
    }

}