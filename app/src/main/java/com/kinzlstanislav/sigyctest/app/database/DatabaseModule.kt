package com.kinzlstanislav.sigyctest.app.database

import android.content.Context
import androidx.room.Room
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DatabaseModule {

    companion object {
        const val APP_DATABASE_NAME = "APP_DATABASE"
    }

    @Single
    fun appDatabase(
        context: Context
    ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, APP_DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()

    @Single
    fun catsDao(appDatabase: AppDatabase): CatsDao = appDatabase.catsDao()
}