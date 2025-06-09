package com.android.internship.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.android.internship.presentation.screens.SplashScreen
import com.android.internship.presentation.screens.listchats.ListChatsScreen
import com.android.internship.presentation.screens.signin.SignInScreen

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
            // TODO: Navigate to sign up screen
        }

        composable<Screen.Chat> {
            // TODO: Navigate to chat screen
        }

        composable<Screen.ListChat> {
            ListChatsScreen(
                navController = navController,
                onChatClick = { chatId ->
                    navController.navigate("chat/$chatId")
                }
            )
        }
    }
}
