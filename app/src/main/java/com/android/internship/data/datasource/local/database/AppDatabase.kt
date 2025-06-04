package com.android.internship.data.datasource.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.internship.data.datasource.local.converter.ListStringConverter
import com.android.internship.data.datasource.local.converter.UserRoomConverter
import com.android.internship.data.datasource.local.dao.UserDao
import com.android.internship.data.datasource.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(ListStringConverter::class, UserRoomConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private const val DATABASE_NAME = "app_database"

        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME,
                ).build()
                Companion.instance = instance
                instance
            }
        }
    }
}
