package com.example.erp_qr.vacation

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.erp_qr.MainActivity
import com.example.erp_qr.adapter.VacationAdapter
import com.example.erp_qr.databinding.FragmentVacationBinding
import com.example.erp_qr.vaction.VacationViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.widget.addTextChangedListener
import com.example.erp_qr.R
import androidx.core.graphics.toColorInt

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class VacationFragment : Fragment() {
    private var _binding: FragmentVacationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VacationViewModel by viewModels()
    private val vacationAdapter by lazy { VacationAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVacationBinding.inflate(inflater, container, false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        binding.editTextSearch.addTextChangedListener { it ->
            viewModel.filterVacations(it.toString())
        }

        binding.tabAll.setOnClickListener {
            highlightTab(binding.tabAll)
            viewModel.filterByStatus("ALL")
        }
        binding.tabApproved.setOnClickListener {
            highlightTab(binding.tabApproved)
            viewModel.filterByStatus("APPROVED")
        }
        binding.tabPending.setOnClickListener {
            highlightTab(binding.tabPending)
            viewModel.filterByStatus("PENDING")
        }
        binding.tabRejected.setOnClickListener {
            highlightTab(binding.tabRejected)
            viewModel.filterByStatus("REJECTED")
        }

        viewModel.statusCount.observe(viewLifecycleOwner) { counts ->
            binding.tabAll.text = "전체 (${counts["ALL"] ?: 0})"
            binding.tabApproved.text = "승인 (${counts["APPROVED"] ?: 0})"
            binding.tabPending.text = "보류 (${counts["PENDING"] ?: 0})"
            binding.tabRejected.text = "거절 (${counts["REJECTED"] ?: 0})"
        }

    }

    private fun setupRecyclerView() {
        binding.vacationRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = vacationAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.filteredData.observe(viewLifecycleOwner) { vacationList ->
            vacationAdapter.submitList(vacationList)
        }
    }

    private fun highlightTab(selected: TextView) {
        val tabs = listOf(binding.tabAll, binding.tabApproved, binding.tabPending, binding.tabRejected)
        tabs.forEach { tab ->
            if (tab == selected) {
                tab.setBackgroundResource(R.drawable.tab_selected_background)
                tab.setTextColor("#2C3E50".toColorInt())
                tab.setTypeface(null, Typeface.BOLD)
            } else {
                tab.setBackgroundResource(R.drawable.tab_unselected_background)
                tab.setTextColor("#7F8C8D".toColorInt())
                tab.setTypeface(null, Typeface.NORMAL)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}