package com.example.erp_qr.qr

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.erp_qr.databinding.FragmentMainBinding
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel: MainFragmentViewModel by viewModels()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.qrSection.visibility =
                    if (state.isLoading) View.GONE else View.VISIBLE
                binding.qrSectionLoading?.visibility =
                    if (state.isLoading) View.VISIBLE else View.GONE

                if (!state.isLoading) {
                    binding.companyName.text = state.companyName
                    binding.textToday.text = state.today
                    generateQRCode(state.employeeId)
                }

                state.errorMessage?.let { msg ->
                    Log.e("MainFragment", "Error: $msg")
                }
            }
        }
    }

    private fun generateQRCode(employeeID: String) {
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.encodeBitmap(
                employeeID, com.google.zxing.BarcodeFormat.QR_CODE, 400, 400
            )
            binding.qrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}