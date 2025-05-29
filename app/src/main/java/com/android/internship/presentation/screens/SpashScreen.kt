package com.android.internship.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.android.internship.R

@Composable
fun SplashScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("signInScreen") {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(150.dp)
            )
            Text(
                text = "ChatFun",
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
            Text(
                text = "The world's fastest messaging app.\n" +
                    "It is free and secure.",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }

    }
}
