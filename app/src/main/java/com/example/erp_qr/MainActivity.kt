package com.example.erp_qr

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.erp_qr.attendance.AttendanceFragment
import com.example.erp_qr.databinding.ActivityMainBinding
import com.example.erp_qr.salary.SalaryFragment
import com.example.erp_qr.vacation.VacationFragment
import com.example.erp_qr.login.LoginActivity
import com.example.erp_qr.notification.NotificationActivity
import com.example.erp_qr.qr.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        setSupportActionBar(binding.toolbar)

        setupBottomNavigation()
        setupObservers()

        if (savedInstanceState == null) {
            loadFragment(MainFragment())
        }
        viewModel.refreshUnreadNotifications()

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
        viewModel.isLoggedOut.observe(this) {
            if (it) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
        viewModel.notification.observe(this) {
            if (it) {
                startActivity(Intent(this, NotificationActivity::class.java))
            }
        }
        viewModel.unreadCount.observe(this) { unreadCount ->
            Log.d("MainViewModel", "Unread count updated: $unreadCount")
            binding.badgeNotification.text = unreadCount.toString()
            binding.badgeNotification.visibility =
                if (unreadCount > 0) View.VISIBLE else View.GONE
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