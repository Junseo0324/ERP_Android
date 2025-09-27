package com.example.erp_qr.vacation

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.erp_qr.vacation.VacationViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.widget.addTextChangedListener
import com.example.erp_qr.R
import androidx.core.graphics.toColorInt
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

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

        binding.editTextSearch.addTextChangedListener { text ->
            viewModel.filterVacations(text.toString())
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->

                binding.progressBar.visibility =
                    if (state.isLoading) View.VISIBLE else View.GONE

                val isEmpty = state.filteredVacations.isEmpty() || state.errorMessage != null
                binding.emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
                binding.vacationRv.visibility = if (isEmpty) View.GONE else View.VISIBLE

                if (!isEmpty) {
                    vacationAdapter.submitList(state.filteredVacations)
                }

                binding.tabAll.text = "전체 (${state.statusCount["ALL"] ?: 0})"
                binding.tabApproved.text = "승인 (${state.statusCount["APPROVED"] ?: 0})"
                binding.tabPending.text = "보류 (${state.statusCount["PENDING"] ?: 0})"
                binding.tabRejected.text = "거절 (${state.statusCount["REJECTED"] ?: 0})"

                state.errorMessage?.let { msg ->
                    Log.e("VacationFragment", "Error: $msg")
                }
            }
        }

    }

    private fun setupRecyclerView() {
        binding.vacationRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = vacationAdapter
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