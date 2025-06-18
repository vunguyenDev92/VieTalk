package com.android.internship.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CommonNavigationDrawer(
    isDrawerOpen: Boolean,
    closeDrawer: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isDrawerOpen,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0x61000000))
                .clickable(onClick = closeDrawer),
        )
    }

    AnimatedVisibility(
        visible = isDrawerOpen,
        enter = slideInHorizontally() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut(),
    ) {
        Row(modifier = modifier) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(304.dp)
                    .background(MaterialTheme.colorScheme.background),
            ) {
                content()
            }
        }
    }
}
