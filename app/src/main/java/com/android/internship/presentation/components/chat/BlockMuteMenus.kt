package com.android.internship.presentation.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.internship.R

@Composable
fun BlockMuteMenus(
    isMuted: Boolean = false,
    isBlocked: Boolean = false,
    onMuteClick: () -> Unit = {},
    onBlockClick: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp),
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options",
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White),

        ) {
            DropdownMenuItem(
                text = {
                    Row {
                        Icon(
                            painter = if (isMuted) {
                                painterResource(R.drawable.ic_mute)
                            } else {
                                painterResource(
                                    R.drawable.ic_unmute,
                                )
                            },
                            contentDescription = "Mute notification",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(24.dp),
                        )
                        Text(
                            text = if (isMuted) "Unmute Notifications" else "Mute Notifications",
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(start = 8.dp),
                            fontWeight = FontWeight.W300,
                            fontSize = 14.sp,
                        )
                    }
                },
                onClick = {
                    onMuteClick()
                    expanded = false
                },
            )
            DropdownMenuItem(
                text = {
                    Row {
                        Icon(
                            painter = painterResource(R.drawable.ic_block),
                            contentDescription = "Block",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(24.dp),
                        )
                        Text(
                            text = if (isBlocked) "Unblock" else "Block",
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 8.dp),
                            fontWeight = FontWeight.W300,
                            fontSize = 14.sp,
                        )
                    }
                },
                onClick = {
                    onBlockClick()
                    expanded = false
                },
            )
        }
    }
}
