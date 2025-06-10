package com.android.internship.presentation.screens.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.internship.di.AppContainer
import com.android.internship.domain.usecase.GetAllUsersInfoUseCase

class ChatListViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatListViewModel(
                getAllUsersInfoUseCase = GetAllUsersInfoUseCase(appContainer.userRepository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 