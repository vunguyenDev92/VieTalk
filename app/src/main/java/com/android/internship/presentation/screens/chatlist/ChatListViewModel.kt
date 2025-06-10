package com.android.internship.presentation.screens.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.internship.data.model.User
import com.android.internship.domain.usecase.GetAllUsersInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class ChatListViewModel(
    private val getAllUsersInfoUseCase: GetAllUsersInfoUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ChatListState())
    open val state = _state.asStateFlow()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val users = getAllUsersInfoUseCase()
                val chatItems = users.map { user ->
                    ChatItemState(
                        id = user.uid,
                        name = user.username,
                        avatarUrl = user.avatar ?: "",
                        lastMessage = "",
                        timestamp = user.lastActiveTime,
                        isOnline = true,
                        isGroupChat = false,
                        memberAvatars = null,
                        lastSenderName = null,
                    )
                }
                _state.update {
                    it.copy(
                        chatItems = chatItems,
                        isLoading = false,
                        error = null,
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load users",
                    )
                }
            }
        }
    }

    fun refreshUsers() {
        loadUsers()
    }
}
