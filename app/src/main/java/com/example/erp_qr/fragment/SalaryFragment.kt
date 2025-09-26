package com.example.erp_qr.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.erp_qr.MainActivity
import com.example.erp_qr.R
import com.example.erp_qr.adapter.SalaryDetailAdapter
import com.example.erp_qr.databinding.FragmentSalaryBinding
import com.example.erp_qr.payrollcertificate.PayrollWebActivity
import com.example.erp_qr.salary.SalaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class SalaryFragment : Fragment() {
    private var _binding: FragmentSalaryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SalaryViewModel by viewModels()

    private val allowanceAdapter = SalaryDetailAdapter(isAllowance = true)
    private val deductionAdapter = SalaryDetailAdapter(isAllowance = false)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSalaryBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupListeners()
        setObservers()
        binding.btnDownload.setOnClickListener {
            val intent = Intent(requireContext(), PayrollWebActivity::class.java)
            intent.putExtra("month", viewModel.currentMonth.value.toString())
            startActivity(intent)
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showToolbar(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as? MainActivity)?.showToolbar(true)
    }

    private fun setupRecyclerView() {
        binding.allowanceRv.layoutManager = LinearLayoutManager(requireContext())
        binding.allowanceRv.adapter = allowanceAdapter

        binding.deductionRv.layoutManager = LinearLayoutManager(requireContext())
        binding.deductionRv.adapter = deductionAdapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupListeners() {
        binding.btnPreviousMonth.setOnClickListener {
            viewModel.changeMonth(-1)
        }
        binding.btnNextMonth.setOnClickListener {
            viewModel.changeMonth(1)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setObservers() {
        viewModel.currentMonth.observe(viewLifecycleOwner) { month ->
            binding.textCurrentMonth.text = formatMonthText(month)
        }

        viewModel.salaryData.observe(viewLifecycleOwner) { salary ->
            salary?.let {
                binding.textMonthlySalary.text = "₩%,.0f".format(it.monthlySalary)

                binding.textTotalAllowance.text = "+₩%,d".format(it.totalAllowance.toInt())

                binding.textTotalDeduction.text = "-₩%,d".format(it.totalDeductions.toInt())

                val netSalary = it.monthlySalary.toInt() + it.totalAllowance.toInt() - it.totalDeductions.toInt()
                binding.textNetSalary.text = "₩%,d".format(netSalary)
                binding.textMonthlySalary.text = "₩%,d".format(netSalary)

                binding.textEmployeeId.text = it.employeeId

                val payDate = LocalDate.parse(viewModel.currentMonth.value + "-01")
                    .withDayOfMonth(10)
                val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
                binding.textPayDate.text = payDate.format(formatter)

                val today = LocalDate.now()
                if (today.isAfter(payDate)) {
                    binding.textPayStatus.text = "지급 완료"
                    binding.textPayStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.salary_allowance)) // #27AE60
                    binding.textPayStatus.setBackgroundResource(R.drawable.status_completed_background)
                } else {
                    binding.textPayStatus.text = "지급 대기"
                    binding.textPayStatus.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
                    binding.textPayStatus.setBackgroundResource(0)
                }
                allowanceAdapter.submitList(it.allowanceDetails.entries.toList())
                deductionAdapter.submitList(it.deductionDetails.entries.toList())
            }
        }
    }

    private fun formatMonthText(month: String): String {
        val parts = month.split("-")
        return "${parts[0]}년 ${parts[1].toInt()}월"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
