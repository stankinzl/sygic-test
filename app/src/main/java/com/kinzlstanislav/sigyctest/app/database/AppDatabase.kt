package com.kinzlstanislav.sigyctest.app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kinzlstanislav.sigyctest.app.network.data.CatResponse

@Database(
    entities = [CatResponse::class],
    version = 2
)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun catsDao(): CatsDao
}