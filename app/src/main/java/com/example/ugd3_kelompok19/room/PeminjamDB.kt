package com.example.ugd3_kelompok19.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Peminjam::class],
    version = 1
)
abstract class PeminjamDB : RoomDatabase() {
    abstract fun peminjamDao(): PeminjamDao

    companion object {
        @Volatile
        private var instance: PeminjamDB? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PeminjamDB::class.java,
                "peminjam12345.db"
            ).build()
    }

}