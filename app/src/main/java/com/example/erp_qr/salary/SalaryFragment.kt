package com.example.erp_qr.salary

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.erp_qr.MainActivity
import com.example.erp_qr.R
import com.example.erp_qr.adapter.SalaryDetailAdapter
import com.example.erp_qr.databinding.FragmentSalaryBinding
import com.example.erp_qr.payrollcertificate.PayrollWebActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class SalaryFragment : Fragment() {
    private var _binding: FragmentSalaryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SalaryViewModel by viewModels()

    private val allowanceAdapter = SalaryDetailAdapter(isAllowance = true)
    private val deductionAdapter = SalaryDetailAdapter(isAllowance = false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSalaryBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupListeners()
        setObservers()
        binding.btnDownload.setOnClickListener {
            val intent = Intent(requireContext(), PayrollWebActivity::class.java)
            intent.putExtra("month", viewModel.uiState.value.currentMonth)
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->

                binding.progressBar.visibility =
                    if (state.isLoading) View.VISIBLE else View.GONE

                if (state.isLoading) {
                    binding.scrollContent.visibility = View.GONE
                    binding.emptyView.visibility = View.GONE
                    return@collect
                }

                binding.textCurrentMonth.text = formatMonthText(state.currentMonth)

                if (state.salaryData?.monthlySalary == null || state.salaryData.allowanceDetails.isEmpty() || state.salaryData.deductionDetails.isEmpty()) {
                    binding.scrollContent.visibility = View.GONE
                    binding.emptyView.visibility = View.VISIBLE

                    val parts = state.currentMonth.split("-")
                    val year = parts[0]
                    val month = parts[1].toInt()
                    binding.textEmptySubtitle.text =
                        "${year}년 ${month}월 급여 명세서가 아직 업로드되지 않았습니다."
                } else {
                    binding.scrollContent.visibility = View.VISIBLE
                    binding.emptyView.visibility = View.GONE

                    // 급여 데이터 처리
                    state.salaryData.let { salary ->
                        binding.textNetSalary.text = "₩%,d".format(
                            salary.monthlySalary.toInt() +
                                    salary.totalAllowance.toInt() -
                                    salary.totalDeductions.toInt()
                        )
                        binding.textMonthlySalary.text = "₩%,d".format(
                            salary.monthlySalary.toInt() +
                                    salary.totalAllowance.toInt() -
                                    salary.totalDeductions.toInt()
                        )

                        binding.textBaseSalary.text = "₩%,d".format(salary.monthlySalary.toInt())
                        binding.textTotalAllowance.text = "+₩%,d".format(salary.totalAllowance.toInt())
                        binding.textTotalDeduction.text = "-₩%,d".format(salary.totalDeductions.toInt())
                        binding.textEmployeeId.text = salary.employeeId

                        val payDate = LocalDate.parse(state.currentMonth + "-01").withDayOfMonth(10)
                        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
                        binding.textPayDate.text = payDate.format(formatter)

                        val today = LocalDate.now()
                        if (today.isAfter(payDate)) {
                            binding.textPayStatus.text = "지급 완료"
                            binding.textPayStatus.setTextColor(
                                ContextCompat.getColor(requireContext(), R.color.salary_allowance)
                            )
                            binding.textPayStatus.setBackgroundResource(R.drawable.status_completed_background)
                        } else {
                            binding.textPayStatus.text = "지급 대기"
                            binding.textPayStatus.setTextColor(
                                ContextCompat.getColor(requireContext(), android.R.color.darker_gray)
                            )
                            binding.textPayStatus.setBackgroundResource(0)
                        }

                        allowanceAdapter.submitList(salary.allowanceDetails.entries.toList())
                        deductionAdapter.submitList(salary.deductionDetails.entries.toList())
                    }
                }


                state.errorMessage?.let { msg ->
                    binding.progressBar.visibility = View.GONE
                    binding.scrollContent.visibility = View.GONE
                    binding.emptyView.visibility = View.VISIBLE

                    binding.textEmptyTitle.text = "급여 정보를 불러올 수 없습니다."
                    binding.textEmptySubtitle.text = "네트워크 상태를 확인하시거나 다시 시도해주세요."
                }
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