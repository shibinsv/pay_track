package com.calibraint.paymenttracking.utils

object Constants {
    const val addUserFragment: String = "AddUserFragment"
    const val moreFragment: String = "MoreFragment"
    const val managementFragment: String = "ManagementFragment"
    const val monthlyTransactionFragment: String = "MonthlyTransactionFragment"

    //    regex
    //will allow only one letter and one number
    val flatRegex = "([A-Za-z]+[0-9]|[0-9]+[A-Za-z])[A-Za-z0-9]*".toRegex()
}