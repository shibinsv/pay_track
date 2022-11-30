package com.calibraint.paymenttracking.utils.enums

enum class DefaultPaymentAmount(val amount: String, var isSelected : Boolean) {
    A_100("100", false),
    A_300("300",false),
    A_500("500",false),
    A_600("600",false),
    A_800("800",false),
    A_1000("1000",false),
    A_CUSTOM("Set custom amount", false);

    companion object {
        fun getDefaultPaymentAmount(): ArrayList<String> {
            val defaultAmount = arrayListOf<String>()
            values().forEach {
                defaultAmount.add(it.amount)
            }
            return defaultAmount
        }

        fun setSelected(position: Int) {
            values().forEach { it.isSelected = false }
            values()[position].isSelected = true
        }
    }
}