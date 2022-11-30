package com.calibraint.paymenttracking.utils.enums

enum class PaymentModes(val mode: String) {

    SELECT_MODE("Select payment mode"),
    UPI("UPI"),
    CASH("Cash"),
    BANK("Bank");

    companion object {
        fun getModes(): ArrayList<String> {
            val paymentArray = arrayListOf<String>()
            values().forEach {
                paymentArray.add(it.mode)
            }
            return paymentArray
        }
    }
}