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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.internship.R

@Composable
fun CommonNavigationDrawer(
    isDrawerOpen: Boolean,
    closeDrawer: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.app_name),
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
                Text(
                    title,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                )

                HorizontalDivider()

                content()
            }
        }
    }
}
