package com.calibraint.paymenttracking.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flat_table")
data class HouseMember(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "flat_no") val flatNumber: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "primary_contact_number") val primaryNumber: String,
    @ColumnInfo(name = "additional_number_1") val additionalNumberOne: String,
    @ColumnInfo(name = "additional_number_2") val additionalNumberTwo: String,
    @ColumnInfo(name = "is_paid") var isPaid: Boolean = false,
    @ColumnInfo(name = "payment_mode") var paymentMode: String
)