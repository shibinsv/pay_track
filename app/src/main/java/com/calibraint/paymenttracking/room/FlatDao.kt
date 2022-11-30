package com.calibraint.paymenttracking.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FlatDao {
    // @Insert(onConflict = OnConflictStrategy.IGNORE)
    @Insert
    suspend fun insertFlatMate(houseMember: HouseMember)

    //to get all the flatmates from the database
    @Query("SELECT*FROM flat_table")
    fun getAll(): List<HouseMember>

    //find the flat details by passing flatno
    @Query("SELECT*FROM flat_table WHERE flat_no LIKE :flatNo LIMIT 1")
    suspend fun findByFlatNo(flatNo: String): HouseMember

    //Update user info
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(flat: HouseMember)

    //update payment action
    @Query("UPDATE flat_table SET is_paid= :isPaid")
    suspend fun updateAllPayments(isPaid: Boolean)

    //update payment action
    @Query("UPDATE flat_table SET is_paid= :isPaid WHERE flat_no = :flatNo")
    suspend fun updatePaymentStatus(flatNo: String, isPaid: Boolean)

    //update payment action
    @Query("UPDATE flat_table SET is_paid= :isPaid,payment_mode= :paymentMode WHERE flat_no = :flatNo")
    suspend fun updatePayment(flatNo: String, isPaid: Boolean, paymentMode: String)

    @Delete
    suspend fun deleteFlatMate(houseMember: HouseMember)

    @Query("DELETE FROM flat_table")
    suspend fun deleteAllMembers()
}