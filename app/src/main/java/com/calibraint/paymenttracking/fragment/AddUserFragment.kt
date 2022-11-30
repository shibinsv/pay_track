package com.calibraint.paymenttracking.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.calibraint.paymenttracking.R
import com.calibraint.paymenttracking.databinding.FragmentAddUserBinding
import com.calibraint.paymenttracking.room.HouseMember
import com.calibraint.paymenttracking.utils.CommonUtils
import com.calibraint.paymenttracking.utils.CommonUtils.showToast
import com.calibraint.paymenttracking.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddUserFragment: BaseFragment(), TextWatcher {

    private lateinit var binding: FragmentAddUserBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddUserBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed(Constants.addUserFragment)
        initialization()
        setOnClickListener()
        setTextWatchers()
    }

    private fun initialization() {
        viewModel.apply {
            userData.observe(viewLifecycleOwner) {
                if (toUpdateUser.value == true) {
                    binding.etFlatNumber.setText(it.flatNumber)
                    binding.etOwnerName.setText(it.name)
                    binding.etPrimaryNumber.setText(it.primaryNumber)
                }
            }
        }
    }

    private fun setTextWatchers() {
        binding.etFlatNumber.addTextChangedListener(this)
        binding.etOwnerName.addTextChangedListener(this)
        binding.etPrimaryNumber.addTextChangedListener(this)
    }

    private fun setOnClickListener() {
        binding.tvBack.setOnClickListener { callback.onBackPressed(Constants.addUserFragment) }
        binding.btnSave.setOnClickListener { saveAction() }
    }

    private fun saveAction(){
        val flatNumber = binding.etFlatNumber.text.toString().replaceFirstChar { it.uppercase() }
        val ownerName = binding.etOwnerName.text.toString()
        val primaryNumber = binding.etPrimaryNumber.text.toString()

        if (flatNumber.isNotEmpty() && ownerName.isNotEmpty() && primaryNumber.isNotEmpty()) {
            val flatInfo = HouseMember(
                viewModel.userData.value?.id,
                flatNumber,
                ownerName,
                primaryNumber,
                binding.etAdditionalNumberOne.text.toString(),
                binding.etAdditionalNumberTwo.text.toString(),
                viewModel.userData.value?.isPaid ?: false,
                ""
            )
            when(viewModel.toUpdateUser.value){
                true -> {
                    viewModel.toUpdateUser.value = false
                    lifecycleScope.launch(Dispatchers.IO) {
                        database.flatDao().updateUser(flatInfo)
                        callback.onSuccessfulMateUserAddition()
                        showToast(requireContext(), "Flat updated successfully")
                    }
                }
                else -> {
                    CommonUtils.startAnimation(requireContext(), R.anim.fade_out, binding.clView)
                    viewModel.addUser(
                        flatNumber,
                        primaryNumber,
                        flatInfo,
                        { showToast(requireContext(), "Flat already present") },
                        { showToast(requireContext(), "Number present") },
                        {
                            callback.onSuccessfulMateUserAddition()
                            showToast(requireContext(), "Flat added successfully")
                        }
                    )
                }
            }
        } else {
            //show data required snackbar
            showToast("Required fields can't be empty", requireContext())
        }
    }

    private fun setPayButtonState(context: Context, button: AppCompatButton, isEnabled: Boolean) {
        button.apply {
            if (isEnabled) {
                this.isEnabled = true
                background = AppCompatResources.getDrawable(context, R.drawable.bg_button_enabled)

            } else {
                this.isEnabled = false
                background = AppCompatResources.getDrawable(context, R.drawable.bg_button_disabled)
            }
        }
    }


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(text: Editable?) {
        if ((binding.etFlatNumber.text?.matches(Constants.flatRegex) == true) &&
            (binding.etPrimaryNumber.text?.isNotEmpty() == true) &&
            (binding.etOwnerName.text?.isNotEmpty() == true)
        ) {
            viewModel.userData.value?.flatNumber?.replaceFirstChar { it.uppercase() }
            setPayButtonState(requireContext(), binding.btnSave, true)
        } else {
            setPayButtonState(requireContext(), binding.btnSave, false)

        }
    }
}