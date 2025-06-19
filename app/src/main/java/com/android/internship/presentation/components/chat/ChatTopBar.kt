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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.internship.R
import com.android.internship.presentation.components.CommonAvatar
import com.android.internship.presentation.components.CommonGroupAvatar
import com.android.internship.presentation.components.CommonDialog
import com.android.internship.presentation.components.TextButtonDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    title: String,
    subtitle: String,
    isGroup: Boolean,
    avatarUrls: List<String>,
    isMuted: Boolean,
    isBlocked: Boolean,
    isOtherBlocked: Boolean,
    onBackClick: () -> Unit = {},
    onMuteClick: (MuteOption) -> Unit = {},
    onBlockClick: () -> Unit = {},
) {
    var showBlockDialog by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (isGroup) {
                    CommonGroupAvatar(
                        avatars = avatarUrls,
                        size = 40,
                    )
                } else {
                    CommonAvatar(
                        avatar = avatarUrls.firstOrNull(),
                        modifier = Modifier.size(40.dp),
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
						maxLines = 1,
						overflow = TextOverflow.Ellipsis,
                    )
                    if (!isBlocked && !isOtherBlocked) {
                        Text(
                            text = subtitle,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }

                if (showBlockDialog && !isBlocked && !isOtherBlocked) {
                    CommonDialog(
                        title = "Block $title?",
                        content = stringResource(R.string.description_block).replace(
                            "\$title",
                            title,
                        ),
                        onDismissRequest = { showBlockDialog = false },
                        button = {
                            TextButtonDialog(
                                text = stringResource(R.string.cancel).uppercase(),
                                onClick = { showBlockDialog = false },
                            )
                            TextButtonDialog(
                                text = stringResource(R.string.block).uppercase(),
                                onClick = {
                                    onBlockClick()
                                    showBlockDialog = false
                                },
                                color = Color.Red,
                            )
                        },
                    )
                } else if (showBlockDialog && isBlocked && !isOtherBlocked) {
                    CommonDialog(
                        title = "Unblock $title?",
                        content = stringResource(R.string.description_unblock).replace(
                            "\$title",
                            title,
                        ),
                        onDismissRequest = { showBlockDialog = false },
                        button = {
                            TextButtonDialog(
                                text = stringResource(R.string.cancel).uppercase(),
                                onClick = { showBlockDialog = false },
                            )
                            TextButtonDialog(
                                text = stringResource(R.string.unblock).uppercase(),
                                onClick = {
                                    onBlockClick()
                                    showBlockDialog = false
                                },
                                color = Color.Red,
                            )
                        },
                    )
                }
                if (!(isBlocked == false && isOtherBlocked)) {
                    BlockMuteMenus(
                        isMuted = isMuted,
                        isBlocked = isBlocked,
                        isOtherBlocked = isOtherBlocked,
                        onMuteClick = onMuteClick,
                        onBlockClick = { showBlockDialog = true },
                    )
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
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp),
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp),
    )
}
