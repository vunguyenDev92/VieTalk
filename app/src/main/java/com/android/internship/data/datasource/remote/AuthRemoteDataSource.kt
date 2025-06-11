package com.android.internship.data.datasource.remote

import android.util.Log
import com.android.internship.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRemoteDataSource {
    private val auth = FirebaseAuth.getInstance()
    private val fireStore = FirebaseFirestore.getInstance()

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String> {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            return Result.success(result.user?.uid ?: "")
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun getUserFromFireStore(uid: String): User? {
        return try {
            val userDoc = fireStore.collection("users")
                .document(uid)
                .get()
                .await()

            if (userDoc.exists()) {
                userDoc.toObject(User::class.java)
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getActiveUser(uid: String): String {
        return try {
            val userDoc = fireStore.collection("users")
                .document(uid)
                .get()
                .await()

            userDoc.getString("lastActiveTime") ?: ""
        } catch (_: Exception) {
            ""
        }
    }

//    fun updateActiveUser(uid: String, lastActiveTime: String) {
//        try {
//            fireStore.collection("users").document(uid).update("lastActiveTime", lastActiveTime)
//        } catch (_: Exception) {}
//    }

    fun updateActiveUser(uid: String, lastActiveTime: String) {
        // BƯỚC 3: Thay thế toàn bộ nội dung hàm cũ bằng code này

        // Kiểm tra an toàn để đảm bảo không gọi với uid rỗng
        if (uid.isBlank()) {
            Log.w("aaa", "updateActiveUser được gọi với UID rỗng. Bỏ qua.")
            return
        }

        Log.d("aaa", "Đang chuẩn bị cập nhật lastActiveTime cho UID: $uid với giá trị: $lastActiveTime")

        fireStore.collection("users").document(uid)
            .update("lastActiveTime", lastActiveTime)
            .addOnSuccessListener {
                // Log này sẽ chạy nếu cập nhật lên Firestore thành công
                Log.i("aaa", "CẬP NHẬT THÀNH CÔNG lastActiveTime cho UID: $uid")
            }
            .addOnFailureListener { e ->
                // Log này sẽ chạy NẾU CÓ LỖI (ví dụ: không có quyền ghi, sai đường dẫn,...)
                Log.e("aaa", "LỖI khi cập nhật lastActiveTime cho UID: $uid", e)
            }
    }

    fun updateMuteUser(rid: String, uid: String, time: String?) {
        try {
            fireStore.collection("rooms").document(rid).collection("users").document(uid)
                .update("mute", time != null, "turnOnTime", time)
        } catch (_: Exception) {}
    }

    suspend fun getUsersFromFirestore(uids: List<String>): List<User> {
        if (uids.isEmpty()) {
            return emptyList()
        }
        return try {
            val userDocs = fireStore.collection("users")
                .whereIn("uid", uids)
                .get()
                .await()

            userDocs.toObjects(User::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
