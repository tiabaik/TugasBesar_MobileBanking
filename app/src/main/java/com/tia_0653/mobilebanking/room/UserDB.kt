package com.tia_0653.mobilebanking.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [User::class, Transaksi::class],
    version = 1,
)
abstract class UserDB: RoomDatabase() {
    abstract fun UserDao(): UserDao
    abstract fun TransaksiDao(): TransaksiDao

    companion object {
        @Volatile
        private var instance: UserDB? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }


        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UserDB::class.java,
            "note12345.db"
        ).allowMainThreadQueries().build()
    }
}