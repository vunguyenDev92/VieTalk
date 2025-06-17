@file:OptIn(ExperimentalMaterial3Api::class)

package com.android.internship.presentation.components.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    title: String,
    subtitle: String,
    avatarUrls: List<String>,
    isSubtitleActive: Boolean,
    isMuted: Boolean = false,
    isBlocked: Boolean = false,
    onBackClick: () -> Unit = {},
    onMuteClick: () -> Unit = {},
    onBlockClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                GroupAvatar(
                    avatarUrls = avatarUrls,
                    size = 40.dp,
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = if (isSubtitleActive) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                BlockMuteMenus(
                    isMuted = isMuted,
                    isBlocked = isBlocked,
                    onMuteClick = onMuteClick,
                    onBlockClick = onBlockClick,
                )
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
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun ChatTopBarSingleActivePreview() {
    MaterialTheme {
        ChatTopBar(
            title = "John Doe",
            subtitle = "Active Now",
            avatarUrls = listOf("https://example.com/avatar.jpg"),
            isSubtitleActive = true,
            isMuted = false,
            isBlocked = false,
            onMuteClick = { /* ... */ },
            onBlockClick = { /* ... */ },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatTopBarSingleOfflinePreview() {
    MaterialTheme {
        ChatTopBar(
            title = "Jane Smith",
            subtitle = "Offline",
            avatarUrls = listOf("https://example.com/avatar2.jpg"),
            isSubtitleActive = false,
            isMuted = true,
            isBlocked = false,
            onMuteClick = { /* ...  */ },
            onBlockClick = { /* ... */ },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatTopBarGroupPreview() {
    MaterialTheme {
        ChatTopBar(
            title = "Team Discussion",
            subtitle = "5 members",
            avatarUrls = listOf(
                "https://example.com/avatar1.jpg",
                "https://example.com/avatar2.jpg",
            ),
            isSubtitleActive = false,
            isMuted = false,
            isBlocked = true,
            onMuteClick = { /* ...  */ },
            onBlockClick = { /* ...  */ },
        )
    }
}
