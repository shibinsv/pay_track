package com.calibraint.paymenttracking.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HouseMember::class], version = 1)
abstract class FlatDatabase : RoomDatabase() {

    abstract fun flatDao(): FlatDao

    companion object {
        @Volatile
        private var INSTANCE: FlatDatabase? = null

        fun getDatabase(context: Context): FlatDatabase {

            val tempInstance = INSTANCE
            tempInstance?.let {
                return it
            }
            synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        FlatDatabase::class.java,
                        "app_database"
                    ).build()
                INSTANCE = instance
                return instance
            }

        }
    }
}