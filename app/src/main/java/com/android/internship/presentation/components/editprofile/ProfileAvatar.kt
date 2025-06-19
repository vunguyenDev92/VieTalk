package com.android.internship.presentation.components.editprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.android.internship.R

@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier,
    avatarSize: Int = 160,
    cameraButtonSize: Int = 36,
    avatarUrl: String? = null,
    isLoading: Boolean = false,
    onCameraClick: () -> Unit = {},
) {
    Box(
        modifier = modifier.size(avatarSize.dp),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_person_color),
            error = painterResource(id = R.drawable.ic_person_color),
            modifier = Modifier
                .size(avatarSize.dp)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.1f)),
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size((avatarSize * 0.8).dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp,
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .clip(CircleShape)
                .clickable(enabled = !isLoading) { onCameraClick() },
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = "Change profile picture",
                modifier = Modifier.size(cameraButtonSize.dp),
            )
        }
    }
}
