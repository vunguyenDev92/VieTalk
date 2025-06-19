package com.android.internship

import android.app.Application
import com.cloudinary.android.MediaManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val config = mapOf(
            "cloud_name" to "daymbfzlq",
            "api_key" to "559792734895777",
            "api_secret" to "iFLDhSHt1stRARSAIlt7lhr-ivA",
        )
        MediaManager.init(this, config)
    }
}
