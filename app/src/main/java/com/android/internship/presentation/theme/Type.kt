package com.android.internship.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.android.internship.R

val robotoFamily = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_bold, FontWeight.Bold),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_thin, FontWeight.Thin),
    Font(R.font.roboto_black, FontWeight.Black),
    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.roboto_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.roboto_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.roboto_thin_italic, FontWeight.Thin, FontStyle.Italic),
    Font(R.font.roboto_black_italic, FontWeight.Black, FontStyle.Italic),
    Font(R.font.roboto_light_italic, FontWeight.Light, FontStyle.Italic),
)

// Set of Material typography styles to start with
val Typography =
    Typography(
        bodyLarge = Typography().bodyLarge.copy(
            fontFamily = robotoFamily,
            fontSize = 16.sp,
        ),
        bodyMedium = Typography().bodyMedium.copy(
            fontFamily = robotoFamily,
            fontSize = 14.sp,
        ),
        bodySmall = Typography().bodySmall.copy(
            fontFamily = robotoFamily,
            fontSize = 12.sp,
        ),
        displayLarge = Typography().displayLarge.copy(
            fontFamily = robotoFamily,
            fontSize = 57.sp,
        ),
        displayMedium = Typography().displayMedium.copy(
            fontFamily = robotoFamily,
            fontSize = 45.sp,
        ),
        displaySmall = Typography().displaySmall.copy(
            fontFamily = robotoFamily,
            fontSize = 36.sp,
        ),
        headlineLarge = Typography().headlineLarge.copy(
            fontFamily = robotoFamily,
            fontSize = 32.sp,
        ),
        headlineMedium = Typography().headlineMedium.copy(
            fontFamily = robotoFamily,
            fontSize = 28.sp,
        ),
        headlineSmall = Typography().headlineSmall.copy(
            fontFamily = robotoFamily,
            fontSize = 24.sp,
        ),
        labelLarge = Typography().labelLarge.copy(
            fontFamily = robotoFamily,
            fontSize = 14.sp,
        ),
        labelMedium = Typography().labelMedium.copy(
            fontFamily = robotoFamily,
            fontSize = 12.sp,
        ),
        labelSmall = Typography().labelSmall.copy(
            fontFamily = robotoFamily,
            fontSize = 11.sp,
        ),
        titleLarge = Typography().titleLarge.copy(
            fontFamily = robotoFamily,
            fontSize = 22.sp,
        ),
        titleMedium = Typography().titleMedium.copy(
            fontFamily = robotoFamily,
            fontSize = 16.sp,
        ),
        titleSmall = Typography().titleSmall.copy(
            fontFamily = robotoFamily,
            fontSize = 14.sp,
        ),
    )
