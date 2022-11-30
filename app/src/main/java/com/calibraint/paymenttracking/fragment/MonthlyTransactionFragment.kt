package com.calibraint.paymenttracking.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.calibraint.paymenttracking.adapters.FlatBottomSheetAdapter
import com.calibraint.paymenttracking.databinding.BottomSheetFlatNameBinding
import com.calibraint.paymenttracking.databinding.FragmentMonthlyTransactionBinding
import com.calibraint.paymenttracking.interfaces.FlatAction
import com.calibraint.paymenttracking.utils.CommonUtils
import com.calibraint.paymenttracking.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog

class MonthlyTransactionFragment : BaseFragment(), FlatAction {

    private lateinit var binding: FragmentMonthlyTransactionBinding
    private var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var flatBottomSheet: BottomSheetFlatNameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMonthlyTransactionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CommonUtils.setPayButtonState(requireContext(), binding.btnPay, false)
        onBackPressed(Constants.monthlyTransactionFragment)
        setSpinner()
        setOnClickListener()
    }

    private fun setSpinner() {
        CommonUtils.setPaymentSpinner(
            requireContext(),
            binding.spinnerPaymentMode
        ) { viewModel.flatPaymentMode.value = it }
    }

    private fun setOnClickListener() {
        binding.tvFlatSelection.setOnClickListener {
            flatBottomSheet = BottomSheetFlatNameBinding.inflate(layoutInflater)
            bottomSheetDialog = BottomSheetDialog(requireContext())
            bottomSheetDialog?.let {
                it.setContentView(flatBottomSheet.root)
                it.behavior.peekHeight = 1500
                flatBottomSheet.root.layoutParams.height = 1500
                it.show()
            }
            flatBottomSheet.rvFlatNumber.adapter = FlatBottomSheetAdapter(requireContext(), viewModel.flatData.value!!.filter { !it.isPaid }, this)
        }
        binding.btnPay.setOnClickListener {
            callback.onPaymentMade(viewModel.flatPayment.value!!, true, viewModel.flatPaymentMode.value!!)
        }
    }

    override fun onFlatSelection(houseMember: String) {
        bottomSheetDialog?.dismiss()
        CommonUtils.setPayButtonState(requireContext(), binding.btnPay, true)
        houseMember.let {
            binding.tvFlatSelection.text = it
            viewModel.flatPayment.value = it
        }
    }


}
