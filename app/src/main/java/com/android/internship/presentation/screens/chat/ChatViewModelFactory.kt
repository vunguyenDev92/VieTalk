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
import com.android.internship.domain.usecase.ObserveMessagesUseCase
import com.android.internship.domain.usecase.ObserveUserRoomDetailsUseCase
import com.android.internship.domain.usecase.SeenMessageUseCase
import com.android.internship.domain.usecase.SendMessagesUseCase
import com.android.internship.domain.usecase.UpdateActiveUserUseCase
import com.android.internship.presentation.components.utils.IConnectivityObserver

class ChatViewModelFactory(
    private val authRepository: AuthRepository,
    private val roomRepository: RoomRepository,
    private val connectivityObserver: IConnectivityObserver,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            val savedStateHandle = extras.createSavedStateHandle()

            return ChatViewModel(
                savedStateHandle = savedStateHandle,
                authRepository = authRepository,
                getRoomUseCase = GetRoomUseCase(roomRepository),
                getUserInfoUseCase = GetAllUsersInfoUseCase(authRepository),
                observeMessagesUseCase = ObserveMessagesUseCase(roomRepository),
                observeUserRoomDetailsUseCase = ObserveUserRoomDetailsUseCase(roomRepository),
                sendMessageUseCase = SendMessagesUseCase(authRepository, roomRepository),
                seenMessageUseCase = SeenMessageUseCase(authRepository, roomRepository),
                addTypingUseCase = AddTypingUseCase(authRepository, roomRepository),
                updateActiveUserUseCase = UpdateActiveUserUseCase(authRepository),
                connectivityObserver = connectivityObserver,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
