package com.calibraint.paymenttracking.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.calibraint.paymenttracking.R
import com.calibraint.paymenttracking.adapters.UserManagementAdapter
import com.calibraint.paymenttracking.databinding.BottomSheetPaidActionBinding
import com.calibraint.paymenttracking.databinding.ChipPaidBinding
import com.calibraint.paymenttracking.databinding.FragmentManagementBinding
import com.calibraint.paymenttracking.room.HouseMember
import com.calibraint.paymenttracking.utils.CommonUtils
import com.calibraint.paymenttracking.utils.Constants
import com.calibraint.paymenttracking.utils.pieCharts.PieChartData
import com.calibraint.paymenttracking.utils.pieCharts.PieChartRepresentation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagementFragment: BaseFragment() {

    private lateinit var binding: FragmentManagementBinding
    private lateinit var unpaidUserAdapter: UserManagementAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManagementBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialization()
        onBackPressed(Constants.managementFragment)
    }

    private fun setPieChart(data: List<HouseMember>) {
        val pieData = PieChartData(pieCentreText = "Payment info")
        pieData.pieEntries = PieChartRepresentation.getPieEntries(data)
        PieChartRepresentation.initializePieChart(requireContext(), binding.pcDataStats, pieData)
    }

    private fun initialization() {
        viewModel.flatData.observe(viewLifecycleOwner) { data ->
            unpaidUserAdapter.notifyDataSetChanged()
            binding.tvPaidUserTitle.visibility = if (data.any { it.isPaid }) View.VISIBLE else View.GONE
            binding.tvUnpaidUserTitle.visibility = if (data.any { !it.isPaid }) View.VISIBLE else View.GONE
            setPieChart(data)
        }
        viewModel.isDataPresent.observe(viewLifecycleOwner) {
            it?.let {
                binding.apply {
                    if (!it){
                        tvHeaderTitle.text = getString(R.string.no_records_found)
                        pcDataStats.visibility = View.GONE
                    } else {
                        tvHeaderTitle.text = getString(R.string.Payment_stats)
                        pcDataStats.visibility = View.VISIBLE
                    }
                }
            }
        }
        setPaidUserUI()
        setUnpaidUserUI()
    }

    private fun setPaidUserUI() {
        viewModel.flatData.value?.filter { it.isPaid }?.sortedBy { it.flatNumber }?.forEach { data ->
            val chip = createChip(data)
            binding.cgPaid.addView(chip)
            chip.setOnClickListener {
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                val bottomSheet = BottomSheetPaidActionBinding.inflate(layoutInflater)
                bottomSheet.apply {
                    bottomSheetDialog.setContentView(this.root)
                    bottomSheetDialog.show()

                    tvViewDetails.setOnClickListener {
                        bottomSheetDialog.dismiss()
                        CommonUtils.showDialog(requireContext(), data)
                    }

                    tvPhoneUser.setOnClickListener {
                        bottomSheetDialog.dismiss()
                        CommonUtils.callUser(requireContext(), data.primaryNumber)
                    }

                    tvRevokePayment.setOnClickListener {
                        bottomSheetDialog.dismiss()
                        CommonUtils.startAnimation(requireContext(), R.anim.slide_up, chip)
                        Handler(Looper.getMainLooper()).postDelayed({
                            chip.visibility = View.GONE
                            callback.onPaymentMade(data.flatNumber, false, "")
                        }, 500)
                    }
                }
            }
        }
    }

    private fun setUnpaidUserUI() {
        unpaidUserAdapter = UserManagementAdapter(requireContext(), callback, viewModel.flatData.value!!.filter { !it.isPaid })
        binding.rvDefaulters.adapter = unpaidUserAdapter
    }

    private fun createChip(houseMember: HouseMember): Chip {
        val chip = ChipPaidBinding.inflate(layoutInflater).root
        chip.text = houseMember.flatNumber
        chip.tag = id
        return chip
    }
}