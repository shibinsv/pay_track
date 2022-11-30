package com.calibraint.paymenttracking.repository

import androidx.activity.ComponentActivity
import com.calibraint.paymenttracking.room.FlatDao
import com.calibraint.paymenttracking.room.FlatDatabase
import com.calibraint.paymenttracking.room.HouseMember
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DatabaseRepository @Inject constructor(val database: FlatDatabase, val flatDao: FlatDao) : ComponentActivity() {

    fun getAllInfo(): List<HouseMember> {
        return flatDao.getAll()
    }

    suspend fun addUser(flatInfo: HouseMember) {
        flatDao.insertFlatMate(flatInfo)
    }

    suspend fun deleteUser(flatInfo: HouseMember) {
        flatDao.deleteFlatMate(flatInfo)
    }

    suspend fun updatePayment(flatNo: String, isPaid: Boolean, paymentMode: String) {
        flatDao.updatePayment(flatNo, isPaid, paymentMode)
    }

    suspend fun clearAllTransactions() {
        flatDao.updateAllPayments(false)
    }

    suspend fun deleteAllUsers() {
        flatDao.deleteAllMembers()
    }

}