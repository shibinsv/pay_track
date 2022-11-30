package com.calibraint.paymenttracking.interfaces

import com.calibraint.paymenttracking.room.HouseMember

interface UserAction {
    fun onBackPressed(fragmentName: String)
    fun clearTransactions()
    fun onClearAll()
    fun onUserAction()
    fun onAddUser()
    fun onMonthlyTransactionMade()
    fun onEditUser(flatInfo: HouseMember)
    fun onDeleteUser(flatInfo: HouseMember)
    fun onPaymentMade(flatNo: String, isPaid: Boolean, paymentMode: String)
    fun onSuccessfulMateUserAddition()
}