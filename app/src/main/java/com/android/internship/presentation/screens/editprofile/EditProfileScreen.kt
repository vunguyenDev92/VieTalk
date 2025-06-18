package com.android.internship.presentation.screens.editprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.internship.presentation.components.editprofile.EmailDisplay
import com.android.internship.presentation.components.editprofile.NameInputField
import com.android.internship.presentation.components.editprofile.ProfileAvatar
import com.android.internship.presentation.components.editprofile.ProfileTopAppBar
import com.android.internship.presentation.components.editprofile.SaveButton
@Composable
fun ProfileScreen(navController: NavController) {
    val initialName = "John Lennon"
    val email = "your_email@gmail.com"
    var name by remember { mutableStateOf(initialName) }

    Scaffold(
        topBar = {
            ProfileTopAppBar(
                onBackClick = { navController.popBackStack() },
            )
        },
        containerColor = Color.White,
        modifier = Modifier.imePadding(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            ProfileAvatar(
                onCameraClick = { /* TODO: Add camera logic here */ },
            )

            Spacer(modifier = Modifier.height(24.dp))

            EmailDisplay(email = email)

            Spacer(modifier = Modifier.height(20.dp))

            NameInputField(
                name = name,
                onNameChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.weight(1f))
			Spacer(modifier = Modifier.height(20.dp))
            SaveButton(
                onSaveClick = {
                    navController.popBackStack()
                },
                isEnabled = name.isNotBlank(),
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
