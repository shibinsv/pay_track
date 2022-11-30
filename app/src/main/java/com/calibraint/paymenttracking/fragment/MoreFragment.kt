package com.calibraint.paymenttracking.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.calibraint.paymenttracking.databinding.FragmentMoreBinding
import com.calibraint.paymenttracking.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreFragment : BaseFragment() {

    private lateinit var binding: FragmentMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialization()
        viewModel.flatData.observe(viewLifecycleOwner) { data ->
            data.isNullOrEmpty().let {
                setEnabledState(it, binding.tvClearTransactions)
                setEnabledState(it, binding.tvDeleteAll)
            }
        }
    }

    private fun setEnabledState(isDisabled: Boolean, view: View) {
        when (isDisabled) {
            true -> {
                view.alpha = 0.5f
                view.isClickable = false
            }
            else -> {
                view.alpha = 1f
                view.isClickable = true
            }
        }
    }

    private fun initialization() {
        onBackPressed(Constants.moreFragment)
        binding.tvClearTransactions.setOnClickListener { callback.clearTransactions() }
        binding.tvDeleteAll.setOnClickListener { callback.onClearAll() }
    }
}