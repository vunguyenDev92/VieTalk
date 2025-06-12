package com.android.internship.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.android.internship.presentation.screens.SplashScreen
import com.android.internship.presentation.screens.chat.ChatScreen
import com.android.internship.presentation.screens.signin.SignInScreen
import com.android.internship.presentation.screens.signup.SignUpScreen

fun NavGraphBuilder.main(navController: NavHostController) {
    navigation(
        startDestination = Screen.Splash,
        route = Route.Main::class,
    ) {
        composable<Screen.Splash> {
            SplashScreen(navController)
        }

        composable<Screen.SignIn> {
            SignInScreen(navController)
        }

        composable<Screen.SignUp> {
            SignUpScreen(navController)
        }

        composable<Screen.Chat> {
            ChatScreen(navController)
        }
    }
}
