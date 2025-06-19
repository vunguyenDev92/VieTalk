package com.android.internship.presentation.components.editprofile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.android.internship.presentation.theme.robotoFamily

@Composable
fun EmailDisplay(
    email: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = email,
        color = Color(0xFF333333),
        fontSize = 16.sp,
        modifier = modifier,
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Normal,
    )
}
