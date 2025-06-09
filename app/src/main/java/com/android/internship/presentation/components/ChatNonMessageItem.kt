package com.android.internship.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ChatNonMessageItem(
    avatarUrl: String,
    memberAvatars: List<String>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = memberAvatars.getOrNull(0),
            contentDescription = "Avatar of group member 1",
            modifier = Modifier
                .padding(top = 17.dp)
                .size(53.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
    }
}
