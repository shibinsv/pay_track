package com.calibraint.paymenttracking.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.children
import com.calibraint.paymenttracking.R
import com.calibraint.paymenttracking.databinding.ChipPaidBinding
import com.calibraint.paymenttracking.databinding.DialogMakePaymentBinding
import com.calibraint.paymenttracking.databinding.DialogUserDetailsBinding
import com.calibraint.paymenttracking.room.HouseMember
import com.calibraint.paymenttracking.utils.enums.DefaultPaymentAmount
import com.calibraint.paymenttracking.utils.enums.PaymentModes


object CommonUtils {

    fun showToast(context: Context, message: String) {
        Looper.prepare()
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        Looper.loop()
    }

    fun showToast(message: String, context: Context) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()

    fun setPaymentSpinner(
        context: Context,
        spinner: AppCompatSpinner,
        onSelection: (String) -> Unit
    ) {
        val paymentArray = PaymentModes.getModes()
        val adapter = ArrayAdapter(
            context,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            paymentArray
        )
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                onSelection(if (position > 0) paymentArray[position] else "")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun showPaymentDialog(
        context: Context,
        paymentMode: (String) -> Unit,
        onPaid: (String) -> Unit
    ) {
        val alert = AlertDialog.Builder(context, R.style.CustomDialogTheme).create()
        val binding = DialogMakePaymentBinding.inflate(LayoutInflater.from(context))
        alert.apply {
            setCancelable(false)
            binding.apply {
                setView(root)
                var paidAmount = ""
                setPaymentSpinner(context, spinnerPaymentMode) { paymentMode(it) }
                setPayButtonState(context, btnPay, false)
                etPaymentAmount.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {}

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {}

                    override fun afterTextChanged(text: Editable?) {
                        paidAmount = text.toString()
                        setPayButtonState(context, btnPay, (paidAmount.isNotEmpty() && paidAmount.length >= 2))
                    }
                })
                DefaultPaymentAmount.getDefaultPaymentAmount().forEachIndexed { position, it ->
                    ChipPaidBinding.inflate(LayoutInflater.from(context)).root.apply {
                        text = it
                        tag = it
                        cgAmount.addView(this)
                        cgAmount.children.forEachIndexed { index, view ->
                            if (index == DefaultPaymentAmount.getDefaultPaymentAmount().lastIndex) view.alpha = 0.7f
                        }
                        setOnClickListener {
                            cgAmount.children.forEachIndexed { index, view ->
                                val chipPosition = cgAmount.indexOfChild(it)
                                view.alpha = if (index == position) 0.7f else 1f
                                etPaymentAmount.setText("")
                                tvPaymentAmount.text = ""
                                if (chipPosition == DefaultPaymentAmount.getDefaultPaymentAmount().lastIndex) {
                                    etPaymentAmount.visibility = View.VISIBLE
                                    tvPaymentAmount.visibility = View.GONE
                                } else {
                                    tvPaymentAmount.text = text
                                    paidAmount = text.toString()
                                    setPayButtonState(context, btnPay, true)
                                    tvPaymentAmount.visibility = View.VISIBLE
                                    etPaymentAmount.visibility = View.GONE
                                }
                            }
                        }
                    }
                }
                ivClose.setOnClickListener { dismiss() }
                btnPay.setOnClickListener {
                    dismiss()
                    onPaid(paidAmount)
                }
            }
            show()
        }
    }

    fun setPayButtonState(context: Context, button: AppCompatButton, isEnabled: Boolean) {
        button.apply {
            if (isEnabled) {
                this.isEnabled = true
                background = AppCompatResources.getDrawable(context, R.drawable.bg_button_enabled)
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done, 0, 0, 0);

            } else {
                this.isEnabled = false
                background = AppCompatResources.getDrawable(context, R.drawable.bg_button_disabled)
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_red, 0, 0, 0);
            }
        }
    }

    fun showDialog(context: Context, data: HouseMember) {
        val alert = AlertDialog.Builder(context, R.style.CustomDialogTheme).create()
        val binding = DialogUserDetailsBinding.inflate(LayoutInflater.from(context))
        alert.apply {
            binding.apply {
                setView(root)
                tvOwnerName.text = data.name
                tvMobileNumber.text = data.primaryNumber
                ivClose.setOnClickListener { dismiss() }
                viewCall.setOnClickListener {
                    callUser(context, data.primaryNumber)
                }
                show()
            }
        }
    }

    fun callUser(context: Context, number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        context.startActivity(intent)
    }

    fun openWhatsApp(context: Context, smsNumber: String) {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = "text/plain"
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
        sendIntent.putExtra("jid", "$smsNumber@s.whatsapp.net")
        sendIntent.setPackage("com.whatsapp")
        context.startActivity(sendIntent)
    }

    fun showPopupMenu(
        context: Context,
        view: View,
        menu: Int,
        onEdit: () -> Unit,
        onDelete: () -> Unit,
    ) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_edit -> {
                    onEdit()
                    return@setOnMenuItemClickListener true
                }
                R.id.menu_delete -> {
                    onDelete()
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener true
            }
        }
        popupMenu.show()
    }

    fun startAnimation(context: Context, animationView: Int, view: View) {
        val animation = AnimationUtils.loadAnimation(context, animationView)
        view.startAnimation(animation)
    }
}