package com.android.internship.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.internship.R
import com.android.internship.presentation.navigation.Screen
import com.android.internship.presentation.theme.Black
import com.android.internship.presentation.theme.Black35
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        delay(1000)
        navController.navigate(Screen.SignIn) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_vietalk),
            contentDescription = stringResource(R.string.app_logo),
            modifier = Modifier.size(224.dp),
        )
    }
}
