package com.calibraint.paymenttracking.fragment

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.calibraint.paymenttracking.viewmodels.DashboardViewModel
import com.calibraint.paymenttracking.interfaces.UserAction
import com.calibraint.paymenttracking.room.FlatDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment : Fragment() {

    protected lateinit var viewModel: DashboardViewModel
    protected lateinit var callback: UserAction
    protected lateinit var database: FlatDatabase

    fun setDatabaseToView(database: FlatDatabase) {
        this.database = database
    }

    fun setViewmodel(viewModel: DashboardViewModel) {
        this.viewModel = viewModel
    }

    fun setUserCallback(callback: UserAction) {
        this.callback = callback
    }

    open fun onBackPressed(fragmentName: String) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    callback.onBackPressed(fragmentName)
                }
            }
        )
    }
}