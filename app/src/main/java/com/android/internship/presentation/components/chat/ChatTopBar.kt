@file:OptIn(ExperimentalMaterial3Api::class)

package com.android.internship.presentation.components.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.android.internship.data.model.Room

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    room: Room,
    memberAvatars: List<String> = emptyList(),
    onBackClick: () -> Unit = {},
    onCallClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (room.isGroup) {
                    GroupAvatar(
                        avatarUrls = memberAvatars,
                        modifier = Modifier,
                        size = 40.dp,
                    )
                } else {
                    AsyncImage(
                        model = room.avatar,
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = room.name ?: "Unknown",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = if (room.isGroup) "Group Chat" else "Active Now",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Row {
                    IconButton(
                        onClick = onCallClick,
                        modifier = Modifier.size(48.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = Color(0xFF2196F3),
                            modifier = Modifier.size(24.dp),
                        )
                    }

                    IconButton(
                        onClick = onMoreClick,
                        modifier = Modifier.size(48.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp),
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    )
}

// Preview cho Single Chat
@Preview(showBackground = true)
@Composable
fun ChatTopBarSinglePreview() {
    MaterialTheme {
        ChatTopBar(
            room = Room(
                rid = "1",
                isGroup = false,
                avatar = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop&crop=face",
                name = "John Doe",
            ),
        )
    }
}

// Preview cho Group Chat
@Preview(showBackground = true)
@Composable
fun ChatTopBarGroupPreview() {
    MaterialTheme {
        ChatTopBar(
            room = Room(
                rid = "2",
                isGroup = true,
                avatar = null,
                name = "Team Discussion",
            ),
            memberAvatars = listOf(
                "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop&crop=face",
                "https://images.unsplash.com/photo-1494790108755-2616b612b786?w=150&h=150&fit=crop&crop=face",
            ),
        )
    }
}

// Preview cho Group Chat không có avatar
@Preview(showBackground = true)
@Composable
fun ChatTopBarGroupEmptyAvatarPreview() {
    MaterialTheme {
        ChatTopBar(
            room = Room(
                rid = "3",
                isGroup = true,
                avatar = null,
                name = "Empty Group",
            ),
            memberAvatars = emptyList(),
        )
    }
}
