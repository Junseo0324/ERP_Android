package com.example.erp_qr.attendance

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.erp_qr.MainActivity
import com.example.erp_qr.adapter.AttendanceAdapter
import com.example.erp_qr.data.AttendanceRecordDTO
import com.example.erp_qr.databinding.FragmentAttendanceBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class AttendanceFragment : Fragment() {
    private lateinit var binding: FragmentAttendanceBinding
    private val viewModel: AttendanceViewModel by viewModels()
    private val adapter  = AttendanceAdapter()


    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showToolbar(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as? MainActivity)?.showToolbar(true)
    }


    private var currentYearMonth: YearMonth = YearMonth.now()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        setRecyclerView()
        setupObservers()

        setupMonthNavigation()
        updateMonthAndLoad()

        return binding.root
    }

    private fun setRecyclerView(){
        binding.recyclerViewAttendance.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAttendance.adapter = adapter
    }



    private fun setupObservers() {
        viewModel.attendanceData.observe(viewLifecycleOwner) { attendanceList ->
            adapter.submitList(attendanceList)
            updateSummary(attendanceList)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupMonthNavigation() {
        binding.btnPreviousMonth.setOnClickListener {
            currentYearMonth = currentYearMonth.minusMonths(1)
            updateMonthAndLoad()
        }
        binding.btnNextMonth.setOnClickListener {
            currentYearMonth = currentYearMonth.plusMonths(1)
            updateMonthAndLoad()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateMonthAndLoad() {
        val formatted = "${currentYearMonth.year}년 ${currentYearMonth.monthValue}월"
        binding.textCurrentMonth.text = formatted
        binding.textMonthYear.text = formatted

        viewModel.loadAttendanceForMonth(currentYearMonth.monthValue.toString())
    }

    private fun updateSummary(attendanceList: List<AttendanceRecordDTO>) {
        val totalDays = attendanceList.size
        val totalHours = attendanceList.sumOf { it.totalWorkHours.toDoubleOrNull() ?: 0.0 }
        binding.textTotalWorkDays.text = totalDays.toString()
        binding.textTotalWorkHours.text = "${totalHours}h"
    }

}
