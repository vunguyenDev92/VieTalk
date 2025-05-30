package com.android.internship.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    @Serializable
    data object Splash : Screen()

    @Serializable
    data object SignIn : Screen()
}
