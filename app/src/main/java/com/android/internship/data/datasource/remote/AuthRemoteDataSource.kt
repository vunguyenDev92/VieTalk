package com.android.internship.data.datasource.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRemoteDataSource(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
)
