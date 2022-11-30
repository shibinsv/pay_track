package com.calibraint.paymenttracking.di

import android.content.Context
import com.calibraint.paymenttracking.BaseApplication
import com.calibraint.paymenttracking.repository.DatabaseRepository
import com.calibraint.paymenttracking.room.FlatDao
import com.calibraint.paymenttracking.room.FlatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
//Singleton component means the instance lives as long as the application lives
//Activity component means the instance lives as activity that they are injected into
//ViewModel component means the instance lives as viewModel
//ActivityRetained component means the instance lives even when screen is rotated
//Service component for service calls

object AppModule {


    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext context: Context): BaseApplication {
        return context as BaseApplication
    }

    @Singleton
    @Provides
    fun provideRandomString(): String {
        return "Totally random"
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): FlatDatabase {
        return FlatDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideFlatDao(database: FlatDatabase): FlatDao {
        return database.flatDao()
    }

    @Singleton
    @Provides
    fun provideRepository(database: FlatDatabase, flatDao: FlatDao): DatabaseRepository {
        return DatabaseRepository(database, flatDao)
    }

}