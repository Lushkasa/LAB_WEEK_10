package com.example.lab_week_10.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Total::class], version = 2)
abstract class TotalDatabase : RoomDatabase() {

    abstract fun totalDao(): TotalDao

    companion object {
        @Volatile
        private var INSTANCE: TotalDatabase? = null
    }
}
