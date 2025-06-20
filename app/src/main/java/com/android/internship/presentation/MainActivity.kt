package com.android.internship.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.android.internship.di.AppContainer
import com.android.internship.presentation.navigation.Navigation
import com.android.internship.presentation.navigation.Screen
import com.android.internship.presentation.theme.AppTheme

class MainActivity : ComponentActivity() {
    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContainer = AppContainer(applicationContext)

        var screen: Screen? = null

        intent.extras?.let {
            screen = handlerNextScreen(it)
        }

        enableEdgeToEdge()
        setContent {
            MainContent(screen)
        }
    }

    @Composable
    private fun MainContent(screen: Screen?) {
        AppTheme {
            Scaffold {
                Box(modifier = Modifier.padding(it)) {
                    Navigation(appContainer = appContainer, startDestination = screen)
                    CommonToastManager.ToastHost()
                }
            }
        }
    }

    private fun handlerNextScreen(bundle: Bundle): Screen? {
        var screen: Screen? = null
        val targetScreen = bundle.getString(TARGET_SCREEN)

        when (targetScreen) {
            Screen.Chat.NAME -> {
                bundle.getString(RID)?.let {
                    screen = Screen.Chat(it)
                }
            }
        }
        return screen
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        var screen: Screen? = null
        intent.extras?.let {
            screen = handlerNextScreen(it)
        }
        setContent {
            MainContent(screen)
        }
    }

    companion object {
        const val RID = "rid"
        const val TARGET_SCREEN = "targetScreen"

        fun newInstance(
            context: Context,
            data: Map<String, String>?,
        ) = Intent(context, MainActivity::class.java).apply {
            data?.forEach { (key, value) ->
                putExtra(key, value)
            }
        }
    }
}
