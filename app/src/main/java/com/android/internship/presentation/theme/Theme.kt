package com.android.internship.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

private val LightColorScheme =
    lightColorScheme(
        background = White,
        error = LightRed,
    )

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun AppTheme(
    content: @Composable () -> Unit,
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as Activity).window
                    WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
                }
            }

            CompositionLocalProvider(
                LocalContentColor provides colorScheme.onBackground,
                content = content,
            )
        },
    )
}
