package com.android.internship.presentation.screens.groupeditor

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.internship.di.AppContainer
import com.android.internship.domain.usecase.CreateGroupUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GroupEditorViewModel(
    private val createGroupUseCase: CreateGroupUseCase,
    private val groupName: String,
    private val members: List<String>,
    private val currentUserId: String?,
) : ViewModel() {
    private val _state = MutableStateFlow(
        GroupEditorState(
            groupName = groupName,
            members = members.filter { it != currentUserId },
        ),
    )
    val state = _state.asStateFlow()

    init {
        if (members.isEmpty()) {
            _state.value = _state.value.copy(
                members = emptyList(),
                canSubmit = false,
            )
        }
    }

    fun onEvent(event: GroupEditorEvent) {
        when (event) {
            is GroupEditorEvent.OnNameInputChange -> {
                _state.value = _state.value.copy(
                    groupName = event.name,
                    canSubmit =
                    event.name.isNotBlank() &&
                        _state.value.members.isNotEmpty() &&
                        (_state.value.groupName != groupName || _state.value.members != members),
                )
            }
            is GroupEditorEvent.OnMemberInputChange -> {
                _state.value = _state.value.copy(memberInput = event.input)
            }
            is GroupEditorEvent.OnMembersChange -> {
                val filteredMembers = event.members.filter { it != currentUserId }
                _state.value = _state.value.copy(
                    members = filteredMembers,
                    canSubmit = filteredMembers.isNotEmpty() &&
                        _state.value.groupName.isNotBlank() &&
                        (_state.value.groupName != groupName || _state.value.members != members),
                )
            }
        }
    }

    fun createGroup() {
        val groupName = _state.value.groupName
        val members = _state.value.members

        if (groupName.isBlank() || members.isEmpty()) {
            _state.value = _state.value.copy(
                isSuccess = false,
                message = ERROR_MESSAGE_GROUP_NAME_AND_MEMBERS_EMPTY,
            )
            return
        }

        try {
            val groupId = createGroupUseCase.invoke(
                roomName = groupName,
                userIds = members,
                currentUserId = currentUserId ?: throw IllegalStateException("Current user ID is missing"),
            )

            _state.value = _state.value.copy(
                groupId = groupId,
                isSuccess = true,
                message = GROUP_CREATED_SUCCESSFULLY,
            )
        } catch (e: IllegalArgumentException) {
            _state.value = _state.value.copy(
                isSuccess = false,
                message = e.message ?: ERROR_MESSAGE_GROUP_NAME_AND_MEMBERS_EMPTY,
            )
        }
    }

    data class GroupEditorState(
        val groupName: String = "",
        val memberInput: String = "",
        val members: List<String> = emptyList(),
        val canSubmit: Boolean = false,
        val groupId: String? = null,
        val isSuccess: Boolean = false,
        val message: String? = null,
    )

    sealed class GroupEditorEvent {
        data class OnNameInputChange(val name: String) : GroupEditorEvent()
        data class OnMemberInputChange(val input: String) : GroupEditorEvent()
        data class OnMembersChange(val members: List<String>) : GroupEditorEvent()
    }

    companion object {
        private const val ERROR_MESSAGE_GROUP_NAME_AND_MEMBERS_EMPTY = "Group name and at least one other member are required."
        private const val GROUP_CREATED_SUCCESSFULLY = "Group created successfully."

        fun factory(context: Context, groupName: String, members: List<String>) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(GroupEditorViewModel::class.java)) {
                    val appContainer = AppContainer(context)

                    val createGroupUseCase = CreateGroupUseCase(
                        roomRepository = appContainer.roomRepository,
                        userRoomRepository = appContainer.userRoomRepository,
                    )

                    val currentUserId = appContainer.authRepository.getCurrentUserId()

                    return GroupEditorViewModel(
                        createGroupUseCase = createGroupUseCase,
                        groupName = groupName,
                        members = members,
                        currentUserId = currentUserId,
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
