package com.android.internship.domain.usecase

import com.android.internship.data.model.Message
import com.android.internship.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

class ObserveMessagesUseCase(private val repository: MessageRepository) {
    operator fun invoke(roomId: String): Flow<List<Message>> {
        return repository.observeMessages(roomId)
    }
    fun observeNewMessages(roomId: String, afterTimestamp: Long): Flow<List<Message>> {
        return repository.observeNewMessages(roomId, afterTimestamp)
    }
}
//
// package com.android.internship.domain.usecase
//
// import com.android.internship.data.model.Message
// import com.android.internship.domain.repository.RoomRepository
// import kotlinx.coroutines.flow.Flow
//
// class ObserveMessagesUseCase(private val roomRepository: RoomRepository) {
// 	operator fun invoke(roomId: String): Flow<List<Message>> {
// 		return roomRepository.observeMessages(roomId)
// 	}
// }
