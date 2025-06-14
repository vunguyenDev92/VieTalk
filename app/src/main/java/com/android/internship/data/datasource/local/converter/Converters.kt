package com.android.internship.data.datasource.local.converter

import androidx.room.TypeConverter
import com.android.internship.data.datasource.local.entity.MessageEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return value?.let { Gson().fromJson(it, listType) }
    }

    @TypeConverter
    fun fromMessageEntity(value: MessageEntity?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toMessageEntity(value: String?): MessageEntity? {
        return value?.let { Gson().fromJson(it, MessageEntity::class.java) }
    }
}
