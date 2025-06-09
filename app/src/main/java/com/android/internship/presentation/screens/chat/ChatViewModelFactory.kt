package com.android.internship.presentation.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.RoomRepository
import com.android.internship.domain.usecase.AddTypingUseCase
import com.android.internship.domain.usecase.GetAllUsersInfoUseCase
import com.android.internship.domain.usecase.GetRoomUseCase
import com.android.internship.domain.usecase.GetUserRoomUseCase
import com.android.internship.domain.usecase.ObserveMessagesUseCase
import com.android.internship.domain.usecase.ObserveUserRoomDetailsUseCase
import com.android.internship.domain.usecase.SeenMessageUseCase
import com.android.internship.domain.usecase.SendMessagesUseCase

class ChatViewModelFactory(
    private val authRepository: AuthRepository,
    private val roomRepository: RoomRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            val savedStateHandle = extras.createSavedStateHandle()

            val getRoomUseCase = GetRoomUseCase(roomRepository)
            val getUserRoomUseCase = GetUserRoomUseCase(roomRepository)
            val getUserInfoUseCase = GetAllUsersInfoUseCase(authRepository)
            val observeMessagesUseCase = ObserveMessagesUseCase(roomRepository)
            val observeUserRoomDetailsUseCase = ObserveUserRoomDetailsUseCase(roomRepository)
            val sendMessageUseCase = SendMessagesUseCase(authRepository, roomRepository)
            val seenMessageUseCase = SeenMessageUseCase(authRepository, roomRepository)
            val addTypingUseCase = AddTypingUseCase(authRepository, roomRepository)

            return ChatViewModel(
                savedStateHandle = savedStateHandle,
                authRepository = authRepository,
                getRoomUseCase = getRoomUseCase,
                getUserRoomUseCase = getUserRoomUseCase,
                getUserInfoUseCase = getUserInfoUseCase,
                observeMessagesUseCase = observeMessagesUseCase,
                observeUserRoomDetailsUseCase = observeUserRoomDetailsUseCase,
                sendMessageUseCase = sendMessageUseCase,
                seenMessageUseCase = seenMessageUseCase,
                addTypingUseCase = addTypingUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
