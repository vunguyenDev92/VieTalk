package com.android.internship.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.android.internship.di.AppContainer
import com.android.internship.presentation.screens.SplashScreen
import com.android.internship.presentation.screens.chat.ChatScreen
import com.android.internship.presentation.screens.chatlist.ChatListScreen
import com.android.internship.presentation.screens.editprofile.ProfileScreen
import com.android.internship.presentation.screens.groupeditor.GroupEditorScreen
import com.android.internship.presentation.screens.signin.SignInScreen
import com.android.internship.presentation.screens.signup.SignUpScreen
fun NavGraphBuilder.main(
    navController: NavHostController,
    appContainer: AppContainer,
    startDestination: Screen = Screen.Splash,
    isNetworkAvailable: Boolean,
) {
    navigation(
        startDestination = startDestination,
        route = Route.Main::class,
    ) {
        composable<Screen.Splash> {
            SplashScreen(navController = navController)
        }

        composable<Screen.SignIn> {
            SignInScreen(
                navController = navController,
                appContainer = appContainer,
            )
        }

        composable<Screen.SignUp> {
            SignUpScreen(
                navController = navController,
                appContainer = appContainer,
            )
        }

        composable<Screen.Chat> {
            ChatScreen(
                navController = navController,
                appContainer = appContainer,
                isNetworkAvailable = isNetworkAvailable,
            )
        }

        composable<Screen.ChatList> {
            ChatListScreen(
                navController = navController,
                appContainer = appContainer,
                isNetworkAvailable = isNetworkAvailable,
            )
        }

        composable<Screen.GroupEditor> {
            GroupEditorScreen(
                navController = navController,
                appContainer = appContainer,
            )
        }

        composable<Screen.EditProfile> {
            ProfileScreen(
                navController = navController,
                appContainer = appContainer,
            )
        }
    }
}
