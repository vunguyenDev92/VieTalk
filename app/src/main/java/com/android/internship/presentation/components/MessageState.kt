package com.android.internship.presentation.components

import com.android.internship.data.model.Room
import com.android.internship.data.model.User
data class MessageState(
    val currentUserId: String = "",
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = true,
    val isRefreshing: Boolean = false,
    val isNetworkAvailable: Boolean = true,
    val room: Room? = null,
    val messages: List<MessageItem> = emptyList(),
    val userMap: Map<String, User> = emptyMap(),
    val currentUser: User? = null,
    val topBarTitle: String = "",
    val topBarSubtitle: String = "",
    val topBarAvatarUrls: List<String> = emptyList(),
    val isPeerActive: Boolean = false,
    val expandedMessageId: String? = null,
    val errorMessage: String? = null,
) {
    // FIX: Override equals để tránh unnecessary recomposition
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageState

        if (currentUserId != other.currentUserId) return false
        if (isLoading != other.isLoading) return false
        if (isLoadingMore != other.isLoadingMore) return false
        if (canLoadMore != other.canLoadMore) return false
        if (isRefreshing != other.isRefreshing) return false
        if (isNetworkAvailable != other.isNetworkAvailable) return false
        if (room != other.room) return false
        if (messages != other.messages) return false
        if (userMap != other.userMap) return false
        if (currentUser != other.currentUser) return false
        if (topBarTitle != other.topBarTitle) return false
        if (topBarSubtitle != other.topBarSubtitle) return false
        if (topBarAvatarUrls != other.topBarAvatarUrls) return false
        if (isPeerActive != other.isPeerActive) return false
        if (expandedMessageId != other.expandedMessageId) return false
        if (errorMessage != other.errorMessage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = currentUserId.hashCode()
        result = 31 * result + isLoading.hashCode()
        result = 31 * result + isLoadingMore.hashCode()
        result = 31 * result + canLoadMore.hashCode()
        result = 31 * result + isRefreshing.hashCode()
        result = 31 * result + isNetworkAvailable.hashCode()
        result = 31 * result + (room?.hashCode() ?: 0)
        result = 31 * result + messages.hashCode()
        result = 31 * result + userMap.hashCode()
        result = 31 * result + (currentUser?.hashCode() ?: 0)
        result = 31 * result + topBarTitle.hashCode()
        result = 31 * result + topBarSubtitle.hashCode()
        result = 31 * result + topBarAvatarUrls.hashCode()
        result = 31 * result + isPeerActive.hashCode()
        result = 31 * result + (expandedMessageId?.hashCode() ?: 0)
        result = 31 * result + (errorMessage?.hashCode() ?: 0)
        return result
    }
}
