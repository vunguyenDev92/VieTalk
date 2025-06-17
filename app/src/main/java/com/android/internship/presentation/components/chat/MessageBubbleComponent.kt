package com.android.internship.presentation.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.android.internship.data.model.User
import com.android.internship.presentation.components.MessageItem
import com.android.internship.presentation.theme.Black
import com.android.internship.presentation.theme.GreenMess
import com.android.internship.presentation.theme.GreyMess
import com.android.internship.presentation.theme.White
import com.android.internship.presentation.theme.robotoFamily
@Composable
fun MessageBubbleComponent(
    item: MessageItem.MessageBubbles,
    currentUserAvatarUrl: String?,
    onMessageClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (item.isFromMe) GreenMess else GreyMess
    val alignment = if (item.isFromMe) Alignment.End else Alignment.Start
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onMessageClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            )
            .padding(vertical = 0.dp),
    ) {
        if (item.isSeenByExpanded && !item.isCloseToHeader) {
            MessageTimeHeaderComponent(message = item.message)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            if (item.isFromMe) Spacer(modifier = Modifier.weight(1f))

            if (!item.isFromMe) {
                if (item.showAvatar) {
                    AsyncImage(
                        model = item.senderAvatarUrl,
                        contentDescription = "Sender Avatar",
                        modifier = Modifier.size(40.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Spacer(modifier = Modifier.size(40.dp))
                }
            }

            Column(horizontalAlignment = alignment) {
                item.senderName?.let { name ->
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelMedium,
                        fontFamily = robotoFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }
                Box(
                    modifier = Modifier
                        .widthIn(max = screenWidth * 0.75f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(backgroundColor)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = item.message.content,
                        fontFamily = robotoFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = if (!item.isFromMe) Black else White,
                    )
                }
            }

            if (item.isFromMe) {
                if (item.showAvatar) {
                    AsyncImage(
                        model = currentUserAvatarUrl,
                        contentDescription = "My Avatar",
                        modifier = Modifier.size(40.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Spacer(modifier = Modifier.size(40.dp))
                }
            }

            if (!item.isFromMe) Spacer(modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 2.dp,
                    end = 48.dp,
                    start = 48.dp,
                ),
            horizontalArrangement = Arrangement.End,
        ) {
            if (item.avatarsOfUsersWhoLastSawThis.isNotEmpty()) {
                SeenByAvatarStack(users = item.avatarsOfUsersWhoLastSawThis)
            }
        }

        if (item.isSeenByExpanded && item.seenByUsers.isNotEmpty()) {
            Spacer(modifier = Modifier.padding(top = 4.dp))
            ExpandedSeenByIndicator(seenByUsers = item.seenByUsers)
        }
    }
}

@Composable
private fun SeenByAvatarStack(users: List<User>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy((-6).dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        users.take(5).forEach { user ->
            AsyncImage(
                model = user.avatar,
                contentDescription = "Seen by ${user.username}",
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.background, CircleShape),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
private fun ExpandedSeenByIndicator(
    seenByUsers: List<User>,
    modifier: Modifier = Modifier,
) {
    val seenByText = remember(seenByUsers) {
        buildString {
            val maxNamesToShow = 3
            val namesToShow = seenByUsers.take(maxNamesToShow).map { it.username }
            val remainingCount = seenByUsers.size - namesToShow.size

            append("Seen by ${namesToShow.joinToString(", ")}")
            if (remainingCount > 0) {
                append(" and $remainingCount others")
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 40.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = seenByText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
        )
    }
}
