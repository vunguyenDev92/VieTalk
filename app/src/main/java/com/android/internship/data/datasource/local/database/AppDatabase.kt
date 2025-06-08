package com.android.internship.data.datasource.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.internship.data.datasource.local.dao.MessageDao
import com.android.internship.data.datasource.local.dao.RoomDao
import com.android.internship.data.datasource.local.dao.UserDao
import com.android.internship.data.datasource.local.dao.UserRoomDao
import com.android.internship.data.datasource.local.entity.MessageEntity
import com.android.internship.data.datasource.local.entity.RoomEntity
import com.android.internship.data.datasource.local.entity.UserEntity
import com.android.internship.data.datasource.local.entity.UserRoomEntity

@Database(
    entities = [UserEntity::class, MessageEntity::class, UserRoomEntity::class, RoomEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun roomDao(): RoomDao
    abstract fun userRoomDao(): UserRoomDao

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
