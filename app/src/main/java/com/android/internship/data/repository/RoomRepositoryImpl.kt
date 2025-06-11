package com.android.internship.data.repository

import com.android.internship.data.datasource.local.RoomLocalDataSource
import com.android.internship.data.datasource.remote.RoomRemoteDataSource
import com.android.internship.data.model.Room
import com.android.internship.domain.repository.RoomRepository

class RoomRepositoryImpl(
    private val roomLocalDataSource: RoomLocalDataSource,
    private val roomRemoteDataSource: RoomRemoteDataSource,
) : RoomRepository {

    override fun addRoomRemote(
        rid: String,
        isGroup: Boolean,
        avatar: String?,
        name: String?,
    ) {
        val room = Room(rid = rid, isGroup = isGroup, avatar = avatar, name = name)
        roomRemoteDataSource.addRoom(room)
    }

    override suspend fun getRoomRemote(rid: String): Room? {
        return roomRemoteDataSource.getRoomById(rid)
    }

    override suspend fun getRoomLocal(rid: String): Room? {
        return roomLocalDataSource.getRoomById(rid)
    }

    override suspend fun saveLocalRoom(room: Room) {
        roomLocalDataSource.saveRoomLocal(room)
    }
}
// }
// package com.android.internship.data.repository
//
// import com.android.internship.data.datasource.local.MessageLocalDataSource
// import com.android.internship.data.datasource.local.RoomLocalDataSource
// import com.android.internship.data.datasource.local.UserRoomLocalDataSource
// import com.android.internship.data.datasource.remote.MessageRemoteDataSource
// import com.android.internship.data.datasource.remote.RoomRemoteDataSource
// import com.android.internship.data.datasource.remote.UserRoomRemoteDataSource
// import com.android.internship.data.model.Message
// import com.android.internship.data.model.Room
// import com.android.internship.data.model.UserRoom
// import com.android.internship.domain.repository.RoomRepository
// import kotlinx.coroutines.flow.Flow
//
// class RoomRepositoryImpl(
// 	private val roomLocalDataSource: RoomLocalDataSource,
// 	private val roomRemoteDataSource: RoomRemoteDataSource,
// 	private val messageLocalDataSource: MessageLocalDataSource,
// 	private val messageRemoteDataSource: MessageRemoteDataSource,
// 	private val userRoomRemoteDataSource: UserRoomRemoteDataSource,
// 	private val userRoomLocalDataSource: UserRoomLocalDataSource,
// 						) : RoomRepository {
//
// 	override suspend fun getRoomRemote(rid: String): Room? {
// 		return roomRemoteDataSource.getRoomById(rid)
// 	}
//
// 	override suspend fun getUserRoomRemote(rid: String): List<UserRoom> {
// 		return userRoomRemoteDataSource.getUserRoomsForRoom(rid)
// 	}
//
// 	override fun observeMessages(rid: String): Flow<List<Message>> {
// 		return messageRemoteDataSource.observeMessages(rid)
// 	}
//
// 	override fun observeUserRoomDetails(rid: String): Flow<List<UserRoom>> {
// 		return userRoomRemoteDataSource.observeUserRoomsForRoom(rid)
// 	}
//
// 	override fun addTyping(rid: String, uid: String, time: String) {
// 		userRoomRemoteDataSource.updateTypingTime(rid, uid, time)
// 	}
//
// 	override fun addRemoteMessage(
// 		rid: String,
// 		message: Message,
// 								 ) {
// 		messageRemoteDataSource.addRemoteMessage(rid, message)
// 	}
//
// 	override fun seenMessage(rid: String, uid: String, mid: String) {
// 		userRoomRemoteDataSource.updateLastSeenMessages(rid, uid, mid)
// 	}
//
// 	override suspend fun getRemoteMessages(
// 		rid: String,
// 		startMessageId: String?,
// 		limit: Int,
// 										  ): List<Message>? {
// 		return messageRemoteDataSource.getRemoteMessages(rid, startMessageId, limit)
// 	}
//
// 	override suspend fun getLocalMessages(rid: String): List<Message>? {
// 		return messageLocalDataSource.getMessage(rid)
// 	}
//
// 	override suspend fun saveLocalMessages(messages: List<Message>) {
// 		return messageLocalDataSource.saveLocalMessage(messages)
// 	}
//
// 	override suspend fun getRoomLocal(rid: String): Room? {
// 		return roomLocalDataSource.getRoomById(rid)
// 	}
//
// 	override suspend fun saveLocalRoom(room: Room) {
// 		roomLocalDataSource.saveRoomLocal(room)
// 	}
//
// 	override suspend fun getUserRoomLocal(rid: String): List<UserRoom>? {
// 		return userRoomLocalDataSource.getUserRoomsForRoom(rid)
// 	}
//
// 	override suspend fun saveLocalUserRoom(userRooms: List<UserRoom>) {
// 		userRoomLocalDataSource.insertUserRooms(userRooms)
// 	}
// }
