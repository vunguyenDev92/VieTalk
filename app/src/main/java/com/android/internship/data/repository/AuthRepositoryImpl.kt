package com.android.internship.data.repository

import com.android.internship.data.datasource.local.AuthLocalDataSource
import com.android.internship.data.datasource.remote.AuthRemoteDataSource
import com.android.internship.data.model.SignInResponse
import com.android.internship.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuthException

class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
) : AuthRepository {

    override suspend fun signIn(username: String, password: String): SignInResponse {
        return try {
            val user = authRemoteDataSource.signInWithEmailAndPassword(username, password)

            if (user != null) {
                authLocalDataSource.saveUserSession(user)

                SignInResponse(
                    success = true,
                    message = "Đăng nhập thành công",
                    user = user,
                )
            } else {
                SignInResponse(
                    success = false,
                    message = "Không tìm thấy thông tin người dùng",
                    user = null,
                )
            }
        } catch (e: FirebaseAuthException) {
            val errorMessage = when (e.errorCode) {
                "ERROR_INVALID_EMAIL" -> "Email không hợp lệ"
                "ERROR_WRONG_PASSWORD" -> "Mật khẩu không đúng"
                "ERROR_USER_NOT_FOUND" -> "Không tìm thấy tài khoản"
                "ERROR_USER_DISABLED" -> "Tài khoản đã bị vô hiệu hóa"
                "ERROR_TOO_MANY_REQUESTS" -> "Quá nhiều lần đăng nhập sai. Vui lòng thử lại sau"
                "ERROR_NETWORK_REQUEST_FAILED" -> "Lỗi kết nối mạng"
                else -> "Đăng nhập thất bại: ${e.message}"
            }

            SignInResponse(
                success = false,
                message = errorMessage,
                user = null,
            )
        } catch (e: Exception) {
            SignInResponse(
                success = false,
                message = "Đã xảy ra lỗi: ${e.message}",
                user = null,
            )
        }
    }

    override suspend fun getCurrentUser(): String? {
        return authLocalDataSource.getCurrentUserId()
    }

    suspend fun signOut() {
        authRemoteDataSource.signOut()
        authLocalDataSource.clearUserSession()
    }

    suspend fun isLoggedIn(): Boolean {
        return authLocalDataSource.isLoggedIn() && authRemoteDataSource.isUserSignedIn()
    }

    suspend fun getCurrentUsername(): String? {
        return authLocalDataSource.getCurrentUsername()
    }

    suspend fun getCurrentUserEmail(): String? {
        return authLocalDataSource.getCurrentUserEmail()
    }
}
