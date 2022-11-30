package com.calibraint.paymenttracking.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.calibraint.paymenttracking.viewmodels.DashboardViewModel
import com.calibraint.paymenttracking.R
import com.calibraint.paymenttracking.databinding.ActivityDashboardBinding
import com.calibraint.paymenttracking.databinding.BottomSheetUserActionBinding
import com.calibraint.paymenttracking.fragment.*
import com.calibraint.paymenttracking.interfaces.UserAction
import com.calibraint.paymenttracking.room.FlatDatabase
import com.calibraint.paymenttracking.room.HouseMember
import com.calibraint.paymenttracking.utils.CommonUtils
import com.calibraint.paymenttracking.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity(), UserAction {

    @Inject
    lateinit var database: FlatDatabase

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel by viewModels<DashboardViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomSheet()
        initialization()
    }

    private fun setBottomSheet() {
        binding.bottomNav.background = null
        binding.bottomNav.menu.getItem(1).isEnabled = false
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_home -> {
                    loadFragment(Constants.managementFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_more -> {
                    loadFragment(Constants.moreFragment)
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener true
                }
            }
        }
    }

    private fun initialization() {
        setOnClickListener()
        loadManagementFragment()
    }

    private fun setOnClickListener() {
        binding.btnAction.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogStyle)
            val bottomSheet = BottomSheetUserActionBinding.inflate(layoutInflater)
            bottomSheet.apply {
                bottomSheetDialog.setContentView(this.root)
                bottomSheetDialog.show()
                tvAddUser.setOnClickListener {
                    onAddUser()
                    bottomSheetDialog.dismiss()
                }
                tvMakePayment.setOnClickListener {
                    onMonthlyTransactionMade()
                    bottomSheetDialog.dismiss()
                }
            }
        }
    }

    private fun loadFragment(fragmentName: String) {
        val setFragment = { fragment: BaseFragment ->
            fragment.setViewmodel(viewModel)
            fragment.setDatabaseToView(database)
            fragment.setUserCallback(this)
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentView.id, fragment).addToBackStack(null)
                .commit()
        }

        val fragment = when (fragmentName) {
            Constants.managementFragment -> ManagementFragment()
            Constants.addUserFragment -> AddUserFragment()
            Constants.monthlyTransactionFragment -> MonthlyTransactionFragment()
            Constants.moreFragment -> MoreFragment()
            else -> null
        }
        setFragment(fragment!!)
        lifecycleScope.launch {
            delay(100)
            when (fragmentName) {
                Constants.managementFragment, Constants.moreFragment -> showBottomBar(true)
                else -> showBottomBar(false)
            }
        }
    }

    private fun showBottomBar(toShow: Boolean) {
        binding.bottomAppBar.visibility = if (toShow) View.VISIBLE else View.GONE
        binding.btnAction.visibility = if (toShow) View.VISIBLE else View.GONE
    }

    private fun loadManagementFragment() {
        viewModel.getData() { loadFragment(Constants.managementFragment) }
    }

    override fun onUserAction() {
        loadFragment(Constants.addUserFragment)
    }

    override fun onAddUser() {
        loadFragment(Constants.addUserFragment)
    }

    override fun onMonthlyTransactionMade() {
        loadFragment(Constants.monthlyTransactionFragment)
    }

    override fun onEditUser(flatInfo: HouseMember) {
        viewModel.userData.value = flatInfo
        viewModel.toUpdateUser.value = true
        onUserAction()
    }

    override fun onDeleteUser(flatInfo: HouseMember) {
        viewModel.deleteUser(flatInfo) { loadManagementFragment() }
    }

    override fun onBackPressed(fragmentName: String) {
        when (fragmentName) {
            Constants.managementFragment -> {
                showBottomBar(true)
                binding.bottomNav.selectedItemId = R.id.bottom_home
            }
            Constants.moreFragment -> {
                showBottomBar(true)
                binding.bottomNav.selectedItemId = R.id.bottom_home
            }
            else -> showBottomBar(false)
        }
        when (fragmentName) {
            Constants.managementFragment -> finishAffinity()
            else -> loadFragment(Constants.managementFragment)
        }
    }

    override fun clearTransactions() {
        viewModel.onClearAllTransactions() {
            CommonUtils.showToast(this, "Transactions have been cleared")
        }
    }

    override fun onClearAll() {
        viewModel.clearAll(
            {
                CommonUtils.showToast(this, "Records have been cleared")
            },
            { CommonUtils.showToast(this, "No records to delete") })
    }

    override fun onPaymentMade(flatNo: String, isPaid: Boolean, paymentMode: String) {
        viewModel.onPaymentMade(flatNo, isPaid, paymentMode) { loadManagementFragment() }
    }

    override fun onSuccessfulMateUserAddition() {
        loadManagementFragment()
    }
}
