package com.android.internship.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.internship.R
import com.android.internship.presentation.theme.Blue
import kotlinx.coroutines.delay

@Composable
fun CommonProgressIndicator(
    modifier: Modifier = Modifier,
    isLoading: Boolean = true,
    color: Color = Blue,
    pulseRateMs: Long = 50,
    backgroundColor: Color? = null,
) {
    var angle by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isLoading) {
        while (isLoading) {
            angle = (angle - 20) % 360
            delay(pulseRateMs)
        }
    }

    Box(
        modifier = modifier.fillMaxSize().apply {
            if (backgroundColor != null) {
                this.background(backgroundColor)
            }
        },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_loading_indicator),
            contentDescription = stringResource(R.string.loading_indicator),
            tint = color,
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.Center)
                .rotate(angle),
        )
    }
}
