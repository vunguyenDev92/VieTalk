@file:OptIn(ExperimentalMaterial3Api::class)

package com.android.internship.presentation.screens.editprofile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.android.internship.R
import com.android.internship.di.AppContainer
import com.android.internship.presentation.CommonToastManager
import com.android.internship.presentation.components.CommonDialog
import com.android.internship.presentation.components.editprofile.EmailDisplay
import com.android.internship.presentation.components.editprofile.NameInputField
import com.android.internship.presentation.components.editprofile.ProfileAvatar
import com.android.internship.presentation.components.editprofile.ProfileTopAppBar
import com.android.internship.presentation.components.editprofile.SaveButton
import com.android.internship.presentation.theme.Green
import com.android.internship.presentation.theme.GreenDark
import com.android.internship.presentation.theme.GreenLight

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val appContainer = remember { AppContainer(context) }
    val viewModel: EditProfileViewModel = viewModel(
            factory = EditProfileViewModelFactory(
                    authRepository = appContainer.authRepository,
                    userRepository = appContainer.userRepository,
                    imageRepository = appContainer.imageRepository
                                                 )
                                                   )

    val uiState by viewModel.uiState.collectAsState()

    uiState.successMessage?.let { message ->
        LaunchedEffect(message) {
            CommonToastManager.makeToast(
                    icon = R.drawable.ic_success,
                    iconColor = Green,
                    backgroundColor = GreenLight,
                    borderColor = GreenDark,
                                        ).show(message)
            viewModel.clearSuccessMessage()
        }
    }

    if (uiState.showDialog) {
        CommonDialog(
			title = stringResource(R.string.notification),
            content = uiState.errorMessage ?: "An unexpected error occurred." ,
			onDismissRequest = { viewModel.dismissDialog() }
                    )
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
                                                               ) { uri: Uri? ->
        uri?.let { viewModel.uploadAvatar(it.toString()) }
    }

    Scaffold(
            topBar = { ProfileTopAppBar(onBackClick = { navController.popBackStack() }) },
            containerColor = Color.White,
            modifier = Modifier.imePadding(),
            ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                    modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    contentAlignment = Alignment.Center
               ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Column(
                            modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                          ) {
                        Spacer(modifier = Modifier.height(20.dp))

                        ProfileAvatar(
                                avatarUrl = uiState.userAvatarUrl,
                                isLoading = uiState.isUploadingAvatar,
                                onCameraClick = { imagePickerLauncher.launch("image/*") }
                                     )

                        Spacer(modifier = Modifier.height(24.dp))

                        EmailDisplay(email = uiState.userEmail)

                        Spacer(modifier = Modifier.height(20.dp))

                        NameInputField(
                                name = uiState.userName,
                                onNameChange = viewModel::onNameChange,
                                modifier = Modifier.fillMaxWidth()
                                      )

                        Spacer(modifier = Modifier.weight(1f))

                        Spacer(modifier = Modifier.height(20.dp))

                        SaveButton(
                                onSaveClick = viewModel::saveProfile,
                                isEnabled = viewModel.isDataChanged() && !uiState.isSaving,
                                isLoading = uiState.isSaving
                                  )

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }

            CommonToastManager.ToastHost()
        }
    }
}
