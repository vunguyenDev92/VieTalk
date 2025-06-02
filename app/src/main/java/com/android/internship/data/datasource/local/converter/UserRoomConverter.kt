package com.android.internship.data.datasource.local.converter

import android.util.Log
import androidx.room.TypeConverter
import com.android.internship.data.model.UserRoom
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserRoomConverter {
    @TypeConverter
    fun fromUserRoomList(value: List<UserRoom>?): String? {
        return if (value == null) null else Gson().toJson(value)
    }

    @TypeConverter
    fun toUserRoomList(value: String?): List<UserRoom>? {
        return if (value == null) {
            null
        } else {
            try {
                val listType = object : TypeToken<List<UserRoom>>() {}.type
                Gson().fromJson(value, listType)
            } catch (e: Exception) {
                Log.e("TypeConverter", "Failed to parse UserRoom list from JSON", e)
                null
            }
        }
    }
}
