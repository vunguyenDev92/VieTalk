package com.android.internship.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.android.internship.presentation.screens.SplashScreen
import com.android.internship.presentation.screens.chat.ChatScreen
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
            val args = it.toRoute<Screen.Chat>()
//            ChatScreen(
//                navController = navController,
//                roomId = args.roomId,
//                currentUserId = args.userId,
//            )
        }
    }
}
