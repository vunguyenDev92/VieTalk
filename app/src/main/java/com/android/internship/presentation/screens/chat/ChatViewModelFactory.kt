package com.android.internship.presentation.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.android.internship.di.AppContainer
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
import com.android.internship.domain.usecase.UpdateBlockUseCase
import com.android.internship.domain.usecase.UpdateMuteUseCase
import com.android.internship.domain.usecase.UpdateTypingTimeUseCase

class ChatViewModelFactory(private val appContainer: AppContainer) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            val savedStateHandle = extras.createSavedStateHandle()
            return ChatViewModel(
                savedStateHandle = savedStateHandle,
                getRoomsUseCase = GetRoomsUseCase(appContainer.roomRepository),
                observeMessagesUseCase = ObserveMessagesUseCase(appContainer.messageRepository),
                observeUserRoomDetailsUseCase = ObserveUserRoomDetailsUseCase(appContainer.userRoomRepository),
                sendMessageUseCase = SendMessagesUseCase(appContainer.authRepository, appContainer.messageRepository, appContainer.roomRepository),
                seenMessageUseCase = SeenMessageUseCase(appContainer.authRepository, appContainer.userRoomRepository),
                addTypingUseCase = UpdateTypingTimeUseCase(appContainer.authRepository, appContainer.userRoomRepository),
                updateActiveUserUseCase = UpdateActiveTimeUseCase(appContainer.authRepository, appContainer.userRepository),
                getAllUsersInRoomUseCase = GetAllUsersInRoomUseCase(appContainer.userRepository),
                getLatestLocalMessageUseCase = GetLatestLocalMessageUseCase(appContainer.messageRepository),
                observeNewMessagesUseCase = ObserveNewMessagesUseCase(appContainer.messageRepository),
                saveLocalMessagesUseCase = SaveLocalMessagesUseCase(appContainer.messageRepository),
                getOlderMessagesUseCase = GetOlderMessagesUseCase(appContainer.messageRepository),
                observeSingleRoomUseCase = ObserveSingleRoomUseCase(appContainer.roomRepository),
                getCurrentUserIdUseCase = GetCurrentUserIdUseCase(appContainer.authRepository),
                updateMuteUseCase = UpdateMuteUseCase(appContainer.authRepository, appContainer.userRoomRepository),
                updateBlockUseCase = UpdateBlockUseCase(appContainer.userRoomRepository),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
