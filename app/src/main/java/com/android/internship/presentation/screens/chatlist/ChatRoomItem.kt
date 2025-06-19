package com.android.internship.presentation.screens.chatlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.internship.presentation.components.CommonAvatar
import com.android.internship.presentation.components.CommonGroupAvatar
import com.android.internship.presentation.theme.Black

@Composable
fun ChatRoomItem(
    isGroup: Boolean,
    name: String,
    memberAvatars: List<String>,
    lastMessage: String,
    lastMessageTime: String,
    isOnline: Boolean,
    lastSenderName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isGroup) {
            CommonGroupAvatar(
                avatars = memberAvatars,
                size = 70,
            )
        } else {
            CommonAvatar(
                avatar = memberAvatars.getOrNull(0).toString(),
                modifier = Modifier.size(70.dp),
                isOnline = isOnline,
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = name,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W400,
                ),
                color = Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = buildAnnotatedString {
                        if (isGroup && lastSenderName.isNotEmpty()) {
                            append(lastSenderName)
                            append(": ")
                        }
                        append(lastMessage)
                    },
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                    ),
                    color = Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp),
                )
                Text(
                    text = lastMessageTime,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                    ),
                    color = Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
