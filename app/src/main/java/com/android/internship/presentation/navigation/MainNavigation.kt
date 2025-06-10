package com.android.internship.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.android.internship.di.AppContainer
import com.android.internship.presentation.screens.SplashScreen
import com.android.internship.presentation.screens.chatlist.ListChatsScreen
import com.android.internship.presentation.screens.chat.ChatScreen
import com.android.internship.presentation.screens.signin.SignInScreen
import com.android.internship.presentation.screens.chatlist.ChatListViewModel
import com.android.internship.presentation.screens.chatlist.ChatListViewModelFactory

fun NavGraphBuilder.main(
    navController: NavHostController,
    appContainer: AppContainer
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
            // TODO: Navigate to sign up screen
        }

        composable<Screen.Chat> {
            ChatScreen(navController)
        }

        composable<Screen.ListChat> {
            val viewModel: ChatListViewModel = viewModel(
                factory = ChatListViewModelFactory(appContainer)
            )
            ListChatsScreen(
                navController = navController,
                viewModel = viewModel,
                onChatClick = { chatId ->
                    navController.navigate("chat/$chatId")
                }
            )
        }
    }
}
