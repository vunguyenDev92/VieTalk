package com.android.internship.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.android.internship.di.AppContainer
import com.android.internship.presentation.components.utils.IConnectivityObserver
import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    data object Root : Route()

    @Serializable
    data object Main : Route()
}

@Composable
fun Navigation(appContainer: AppContainer, startDestination: Screen? = null) {
    val navController = rememberNavController()
    val networkStatus = appContainer.connectivityObserver.observe().collectAsState(initial = IConnectivityObserver.Status.Unavailable)
    val isNetworkAvailable = networkStatus.value == IConnectivityObserver.Status.Available

    NavHost(
        navController = navController,
        startDestination = Route.Main,
        route = Route.Root::class,
    ) {
        if (startDestination != null) {
            main(
                navController = navController,
                appContainer = appContainer,
                startDestination = startDestination,
                isNetworkAvailable = isNetworkAvailable,
            )
        } else {
            main(
                navController = navController,
                appContainer = appContainer,
                isNetworkAvailable = isNetworkAvailable,
            )
        }
    }
}
