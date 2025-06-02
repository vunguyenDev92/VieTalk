package com.android.internship.data.datasource.local

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

class AuthLocalDataSource(context: Context) {
    private val auth = FirebaseAuth.getInstance()
    fun isUserSignedIn(): Boolean {
        return auth.currentUser != null
    }
}
