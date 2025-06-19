package com.android.internship.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.internship.R

@Composable
fun CommonGroupAvatar(
    avatars: List<String>,
    size: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(size.dp),
    ) {
        CommonCircleAsyncImage(
            url = avatars.getOrNull(1),
            contentDescription = stringResource(R.string.avatar),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size((size * 0.75).dp),
        )

        CommonCircleAsyncImage(
            url = avatars.getOrNull(0),
            contentDescription = stringResource(R.string.avatar),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size((size * 0.75).dp),
        )
    }
}
