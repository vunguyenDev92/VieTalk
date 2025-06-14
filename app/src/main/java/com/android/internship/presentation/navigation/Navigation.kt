package com.android.internship.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.android.internship.di.AppContainer
import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    data object Root : Route()

    @Serializable
    data object Main : Route()
}

@Composable
fun Navigation(appContainer: AppContainer) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Main,
        route = Route.Root::class,
    ) {
        main(navController, appContainer)
    }
}
