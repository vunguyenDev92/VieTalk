package com.android.internship.presentation.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun GroupAvatar(
    avatarUrls: List<String>,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
) {
    Box(
        modifier = modifier.size(size),
    ) {
        when {
            avatarUrls.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "👥",
                        fontSize = (size * 0.5f).value.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            avatarUrls.size == 1 -> {
                AsyncImage(
                    model = avatarUrls[0],
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }
            avatarUrls.size >= 2 -> {
                val smallSize = size * 0.7f

                AsyncImage(
                    model = avatarUrls[0],
                    contentDescription = "Avatar 1",
                    modifier = Modifier
                        .size(smallSize)
                        .offset(x = (-4).dp, y = (-2).dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                    contentScale = ContentScale.Crop,
                )

                AsyncImage(
                    model = avatarUrls[1],
                    contentDescription = "Avatar 2",
                    modifier = Modifier
                        .size(smallSize)
                        .offset(x = 4.dp, y = 2.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                        .align(Alignment.BottomEnd),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}
