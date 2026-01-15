package com.example.simkader_236.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Kader::class], version = 1, exportSchema = false)
abstract class DatabaseKader : RoomDatabase() {
    abstract fun kaderDao(): KaderDao

    companion object {
        @Volatile
        private var Instance: DatabaseKader? = null

        fun getDatabase(context: Context): DatabaseKader {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    DatabaseKader::class.java,
                    "kader_database"
                ).build().also { Instance = it }
            }
        }
    }
}