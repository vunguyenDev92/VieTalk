package com.android.internship.presentation.screens.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.internship.di.AppContainer

class ChatListViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatListViewModel::class.java)) {
            val currentUserId = appContainer.authRepository.getCurrentUserId()
                ?: throw IllegalStateException("User must be logged in to view chat list")
                
            @Suppress("UNCHECKED_CAST")
            return ChatListViewModel(
                roomRepository = appContainer.roomRepository,
                userRoomRepository = appContainer.userRoomRepository,
                userRepository = appContainer.userRepository,
                currentUserId = currentUserId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
