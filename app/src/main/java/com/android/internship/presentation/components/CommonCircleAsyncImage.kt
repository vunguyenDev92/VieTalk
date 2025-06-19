package com.android.internship.presentation.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.android.internship.R

@Composable
fun CommonCircleAsyncImage(
    url: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.ic_person_color),
        error = painterResource(R.drawable.ic_person_color),
        modifier = Modifier.clip(CircleShape).then(modifier),
    )
}
