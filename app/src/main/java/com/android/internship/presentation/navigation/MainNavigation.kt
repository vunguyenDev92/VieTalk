package com.android.internship.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.android.internship.di.AppContainer
import com.android.internship.presentation.screens.SplashScreen
import com.android.internship.presentation.screens.chat.ChatScreen
import com.android.internship.presentation.screens.chatlist.ChatListScreen
import com.android.internship.presentation.screens.groupeditor.GroupEditorScreen
import com.android.internship.presentation.screens.signin.SignInScreen
import com.android.internship.presentation.screens.signup.SignUpScreen

fun NavGraphBuilder.main(
    navController: NavHostController,
    appContainer: AppContainer,
) {
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
            it.toRoute<Screen.Chat>()
            ChatScreen(
                navController = navController,
            )
        }

        composable<Screen.ChatList> {
            ChatListScreen(navController)
        }

        composable<Screen.GroupEditor> {
            GroupEditorScreen(navController)
        }
    }
}
