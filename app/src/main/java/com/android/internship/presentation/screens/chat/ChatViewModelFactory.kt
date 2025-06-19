package com.android.internship.presentation.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.MessageRepository
import com.android.internship.domain.repository.RoomRepository
import com.android.internship.domain.repository.UserRepository
import com.android.internship.domain.repository.UserRoomRepository
import com.android.internship.domain.usecase.GetAllUsersInRoomUseCase
import com.android.internship.domain.usecase.GetCurrentUserIdUseCase
import com.android.internship.domain.usecase.GetLatestLocalMessageUseCase
import com.android.internship.domain.usecase.GetOlderMessagesUseCase
import com.android.internship.domain.usecase.GetRoomsUseCase
import com.android.internship.domain.usecase.ObserveMessagesUseCase
import com.android.internship.domain.usecase.ObserveNewMessagesUseCase
import com.android.internship.domain.usecase.ObserveSingleRoomUseCase
import com.android.internship.domain.usecase.ObserveUserRoomDetailsUseCase
import com.android.internship.domain.usecase.SaveLocalMessagesUseCase
import com.android.internship.domain.usecase.SeenMessageUseCase
import com.android.internship.domain.usecase.SendMessagesUseCase
import com.android.internship.domain.usecase.UpdateActiveTimeUseCase
import com.android.internship.domain.usecase.UpdateMuteUseCase
import com.android.internship.domain.usecase.UpdateTypingTimeUseCase
import com.android.internship.presentation.components.utils.IConnectivityObserver

class ChatViewModelFactory(
    private val authRepository: AuthRepository,
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val connectivityObserver: IConnectivityObserver,
    private val userRoomRepository: UserRoomRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            val savedStateHandle = extras.createSavedStateHandle()
            return ChatViewModel(
                savedStateHandle = savedStateHandle,
                getRoomsUseCase = GetRoomsUseCase(roomRepository),
                observeMessagesUseCase = ObserveMessagesUseCase(messageRepository),
                observeUserRoomDetailsUseCase = ObserveUserRoomDetailsUseCase(userRoomRepository),
                sendMessageUseCase = SendMessagesUseCase(
                    authRepository,
                    messageRepository,
                    roomRepository,
                ),
                seenMessageUseCase = SeenMessageUseCase(authRepository, userRoomRepository),
                addTypingUseCase = UpdateTypingTimeUseCase(authRepository, userRoomRepository),
                updateActiveUserUseCase = UpdateActiveTimeUseCase(authRepository, userRepository),
                connectivityObserver = connectivityObserver,
                getAllUsersInRoomUseCase = GetAllUsersInRoomUseCase(userRepository),
                getLatestLocalMessageUseCase = GetLatestLocalMessageUseCase(messageRepository),
                observeNewMessagesUseCase = ObserveNewMessagesUseCase(messageRepository),
                saveLocalMessagesUseCase = SaveLocalMessagesUseCase(messageRepository),
                getOlderMessagesUseCase = GetOlderMessagesUseCase(messageRepository),
                observeSingleRoomUseCase = ObserveSingleRoomUseCase(roomRepository),
                getCurrentUserIdUseCase = GetCurrentUserIdUseCase(authRepository),
                updateMuteUseCase = UpdateMuteUseCase(authRepository, userRoomRepository),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
