package com.calibraint.paymenttracking.adapters

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.calibraint.paymenttracking.R
import com.calibraint.paymenttracking.databinding.AdapterUserManagementBinding
import com.calibraint.paymenttracking.databinding.BottomSheetUnpaidActionBinding
import com.calibraint.paymenttracking.interfaces.UserAction
import com.calibraint.paymenttracking.room.HouseMember
import com.calibraint.paymenttracking.utils.CommonUtils
import com.google.android.material.bottomsheet.BottomSheetDialog

class UserManagementAdapter(
    private val context: Context,
    private val callback: UserAction,
    private val userList: List<HouseMember>
) :
    RecyclerView.Adapter<UserManagementAdapter.ViewHolder>() {

    private lateinit var binding: AdapterUserManagementBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            AdapterUserManagementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.dataBindToView(userList[position])

    override fun getItemCount(): Int = userList.size

    inner class ViewHolder(binding: View) : RecyclerView.ViewHolder(binding) {

        fun dataBindToView(data: HouseMember) {
            binding.apply {
                data.apply {
                    tvName.text = name
                    tvFlat.text = flatNumber
                    tvMobileNumber.text = primaryNumber
                }

                clView.setOnClickListener {
                    val bottomSheetDialog = BottomSheetDialog(context)
                    val bottomSheet = BottomSheetUnpaidActionBinding.inflate(LayoutInflater.from(context))
                    bottomSheet.apply {
                        bottomSheetDialog.setContentView(this.root)
                        bottomSheetDialog.show()

                        tvPhoneUser.setOnClickListener {
                            bottomSheetDialog.dismiss()
                            CommonUtils.callUser(context, data.primaryNumber)
                        }

//                        tvWtsappCall.setOnClickListener {
//                            bottomSheetDialog.dismiss()
//                            CommonUtils.openWhatsApp(context, data.primaryNumber)
//                        }

                        tvMakePayment.setOnClickListener {
                            bottomSheetDialog.dismiss()
                            CommonUtils.showPaymentDialog(context,
                                { data.paymentMode = it },
                                { amountPaid ->
                                    // on payment button click
                                    CommonUtils.startAnimation(context, R.anim.slide_down, clView)
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        clView.visibility = View.GONE
                                        callback.onPaymentMade(
                                            data.flatNumber,
                                            true,
                                            data.paymentMode
                                        )
                                    }, 500)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}