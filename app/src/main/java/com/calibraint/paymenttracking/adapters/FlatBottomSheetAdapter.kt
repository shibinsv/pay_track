package com.calibraint.paymenttracking.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.calibraint.paymenttracking.databinding.ItemFlatNumberBinding
import com.calibraint.paymenttracking.interfaces.FlatAction
import com.calibraint.paymenttracking.room.HouseMember

class FlatBottomSheetAdapter(
    private val context: Context,
    private val data: List<HouseMember>,
    val callback: FlatAction
) : RecyclerView.Adapter<FlatBottomSheetAdapter.ViewHolder>() {

    private lateinit var binding: ItemFlatNumberBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemFlatNumberBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindDataToView(data[position])

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(itemView: ItemFlatNumberBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val textView: TextView = itemView.tvFlat

        fun bindDataToView(houseMember: HouseMember) {
            textView.text = houseMember.flatNumber
            textView.setOnClickListener {
                callback.onFlatSelection(houseMember.flatNumber)
            }
        }
    }
}
