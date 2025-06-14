package com.android.internship.presentation.screens.chatlist

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.android.internship.R
import com.android.internship.presentation.theme.Black
import com.android.internship.presentation.theme.White

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
            AvatarGroup(memberAvatars)
        } else {
            Avatar(memberAvatars, name, isOnline)
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

@Composable
private fun Avatar(
    memberAvatars: List<String>,
    name: String,
    isOnline: Boolean,
) {
    Box(contentAlignment = Alignment.BottomEnd) {
        AsyncImage(
            model = memberAvatars.getOrNull(0),
            contentDescription = "Avatar of $name",
            placeholder = painterResource(R.drawable.ic_vietalk),
            error = painterResource(R.drawable.ic_error),
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        if (isOnline) {
            Canvas(
                modifier = Modifier,
            ) {
                drawCircle(
                    color = White,
                    radius = 6.dp.toPx(),
                    center = Offset(
                        this.size.width - 8.dp.toPx(),
                        this.size.height - 8.dp.toPx(),
                    ),
                )
                drawCircle(
                    color = Color(0xff1ba505),
                    radius = 5.dp.toPx(),
                    center = Offset(
                        this.size.width - 8.dp.toPx(),
                        this.size.height - 8.dp.toPx(),
                    ),
                )
            }
        }
    }
}

@Composable
private fun AvatarGroup(memberAvatars: List<String>) {
    Box {
        AsyncImage(
            model = memberAvatars.getOrNull(0),
            contentDescription = "Avatar of group member 1",
            placeholder = painterResource(R.drawable.ic_person),
            error = painterResource(R.drawable.ic_person),
            modifier = Modifier
                .padding(top = 17.dp)
                .size(53.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )

        if (memberAvatars.size > 1) {
            AsyncImage(
                model = memberAvatars[1],
                contentDescription = "Avatar of group member 2",
                placeholder = painterResource(R.drawable.ic_person),
                error = painterResource(R.drawable.ic_person),
                modifier = Modifier
                    .padding(start = 15.dp, bottom = 5.dp)
                    .size(53.dp)
                    .padding(start = 5.dp, bottom = 5.dp)
                    .clip(CircleShape),

                contentScale = ContentScale.Crop,
            )
        }
    }
}
