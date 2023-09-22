package com.aitgacem.budgeter.di

import android.app.Application
import androidx.room.Room
import com.aitgacem.budgeter.data.TransactionDatabase
import com.aitgacem.budgeter.data.TransactionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideDatabase(application: Application): TransactionDatabase {
        return Room.databaseBuilder(
            application,
            TransactionDatabase::class.java, "main_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideTransactionsRepository(application: Application): TransactionsRepository {
        return TransactionsRepository(provideDatabase(application))
    }
}