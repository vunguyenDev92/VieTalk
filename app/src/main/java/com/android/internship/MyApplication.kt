package com.android.internship

import android.app.Application
import com.cloudinary.android.MediaManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = loadCloudinaryConfig()
        MediaManager.init(this, config)
    }

    private fun loadCloudinaryConfig(): Map<String, String> {
        val inputStream = assets.open("cloudinary-key.json")
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(reader, type)
    }
}
