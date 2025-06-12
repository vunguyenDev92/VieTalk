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
import androidx.compose.foundation.layout.width
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
            .padding(horizontal = 2.dp, vertical = 4.dp),
        horizontalAlignment = alignment,
    ) {
        if (item.isSeenByExpanded) {
            MessageTimeHeaderComponent(message = item.message)
            Spacer(modifier = Modifier.padding(top = 4.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (item.isFromMe) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.Bottom,
        ) {
            if (!item.isFromMe) {
                AsyncImage(
                    model = item.senderAvatarUrl,
                    contentDescription = "Sender Avatar",
                    modifier = Modifier.size(40.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Column(horizontalAlignment = alignment) {
                item.senderName?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 2.dp),
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
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (!item.isFromMe) {
                            Black
                        } else {
                            White
                        },
                    )
                }
            }

            if (item.isFromMe) {
                Spacer(modifier = Modifier.width(8.dp))
                AsyncImage(
                    model = currentUserAvatarUrl,
                    contentDescription = "My Avatar",
                    modifier = Modifier.size(40.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }
        }

        val shouldShowSeenIndicator = item.isSeenByExpanded && item.seenByUsers.isNotEmpty()
        if (shouldShowSeenIndicator) {
            Spacer(modifier = Modifier.padding(top = 4.dp))
            ExpandedSeenByIndicator(
                seenByUsers = item.seenByUsers,
                modifier = Modifier.padding(
                    start = if (!item.isFromMe) 32.dp else 0.dp,
                    end = if (item.isFromMe) 32.dp else 0.dp,
                ),
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
            val maxNamesToShow = 4
            val namesToShow = seenByUsers.take(maxNamesToShow)
            val remainingCount = seenByUsers.size - namesToShow.size

            append("Seen by ${namesToShow.joinToString { it.username }}")
            if (remainingCount > 0) {
                append(" and $remainingCount others")
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = seenByText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SeenByAvatarRow(users = seenByUsers)
    }
}

@Composable
private fun SeenByAvatarRow(users: List<User>) {
    val maxVisibleAvatars = 4
    val usersToShow = users.take(maxVisibleAvatars)
    val remainingCount = users.size - usersToShow.size

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        usersToShow.forEach { user ->
            AsyncImage(
                model = user.avatar,
                contentDescription = "Seen by ${user.username}",
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.background, CircleShape),
                contentScale = ContentScale.Crop,
            )
            Spacer(Modifier.width(2.dp))
        }

        if (remainingCount > 0) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "+$remainingCount",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
