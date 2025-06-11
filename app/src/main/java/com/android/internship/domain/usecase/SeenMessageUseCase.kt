package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.UserRoomRepository

class SeenMessageUseCase(
    private val authRepository: AuthRepository,
    private val userRoomRepository: UserRoomRepository,
) {
    suspend operator fun invoke(rid: String, lastSeenMessageId: String) {
        val uid = authRepository.getCurrentUserId()

        uid?.let {
            userRoomRepository.updateLastSeenMessages(rid, uid, lastSeenMessageId)
        }
    }
}
//package com.android.internship.domain.usecase
//
//import com.android.internship.domain.repository.AuthRepository
//import com.android.internship.domain.repository.RoomRepository
//
//class SeenMessageUseCase(
//	private val authRepository: AuthRepository,
//	private val roomRepository: RoomRepository,
//						) {
//	suspend operator fun invoke(rid: String, mid: String) {
//		val uid = authRepository.getCurrentUserId()
//
//		uid?.let {
//			roomRepository.seenMessage(rid, uid, mid)
//		}
//	}
//}
