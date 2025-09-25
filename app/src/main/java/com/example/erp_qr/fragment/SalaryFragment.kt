package com.example.erp_qr.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.erp_qr.MainActivity
import com.example.erp_qr.adapter.AllowanceAdapter
import com.example.erp_qr.adapter.DeductionAdapter
import com.example.erp_qr.adapter.SalaryDetailAdapter
import com.example.erp_qr.databinding.FragmentSalaryBinding
import com.example.erp_qr.salary.SalaryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SalaryFragment : Fragment() {
    private lateinit var binding: FragmentSalaryBinding
    private val viewModel: SalaryViewModel by viewModels()

    private val allowanceAdapter = SalaryDetailAdapter(isAllowance = true)
    private val deductionAdapter = SalaryDetailAdapter(isAllowance = false)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSalaryBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setObserve()

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
    private fun setObserve() {
        viewModel.salaryData.observe(viewLifecycleOwner) { salaryData ->
            salaryData?.let {
                allowanceAdapter.submitList(it.allowanceDetails.entries.toList())
                deductionAdapter.submitList(it.deductionDetails.entries.toList())
            }
        }
    }
}
