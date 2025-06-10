package com.android.internship.presentation.screens.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.android.internship.R
import com.android.internship.presentation.CommonToastManager
import com.android.internship.presentation.components.CommonDialog
import com.android.internship.presentation.components.CommonProgressIndicator
import com.android.internship.presentation.components.CommonTextField
import com.android.internship.presentation.navigation.Screen
import com.android.internship.presentation.theme.Black
import com.android.internship.presentation.theme.Blue
import com.android.internship.presentation.theme.BlueLight
import com.android.internship.presentation.theme.Green
import com.android.internship.presentation.theme.GreenDark
import com.android.internship.presentation.theme.GreenLight
import com.android.internship.presentation.theme.GreyLight
import com.android.internship.presentation.theme.White

@Composable
fun SignInScreen(
    navController: NavController,
    signInViewModel: SignInViewModel =
        viewModel(factory = SignInViewModel.factory(navController.context)),
) {
    val signInState by signInViewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current

    if (signInState.signInSuccess) {
        SideEffect {
            navController.navigate(route = Screen.Chat) {
                popUpTo(Screen.SignIn) { inclusive = true }
                launchSingleTop = true
            }
            CommonToastManager.makeToast(
                icon = R.drawable.ic_success,
                iconColor = Green,
                backgroundColor = GreenLight,
                borderColor = GreenDark,
            ).show(
                message = navController.context.getString(
                    R.string.login_successful,
                ),
            )
        }
    }

    if (signInState.signInSuccess == false) {
        signInState.message?.let {
            CommonDialog(
                title = stringResource(R.string.error),
                content = it,
                onDismissRequest = {
                    signInViewModel.clearMessage()
                },
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
            .imePadding()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) {
                focusManager.clearFocus()
            },
        verticalArrangement = Arrangement.spacedBy(19.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.ic_vietalk),
                contentDescription = stringResource(R.string.app_logo),
                modifier = Modifier.size(200.dp),
            )
        }
        item {
            Text(
                text = stringResource(R.string.log_in),
                textAlign = TextAlign.Start,
                color = Black,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            CommonTextField(
                label = stringResource(R.string.email),
                textFieldState = signInState.emailState,
                onValueChange = signInViewModel::updateEmailState,
                modifier = Modifier,
            )
        }
        item {
            CommonTextField(
                label = stringResource(R.string.password),
                textFieldState = signInState.passwordState,
                onValueChange = signInViewModel::updatePasswordState,
                imeAction = ImeAction.Done,
                isTextObscured = true,
            )
        }
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd,
            ) {
                NavToSignUpButton(
                    onClick = {
                        navController.navigate(Screen.SignUp) {
                            launchSingleTop = true
                        }
                    },
                )
            }
        }
        item {
            SignInButton(
                isDisable = signInState.emailState.isError || signInState.passwordState.isError,
                onClick = {
                    focusManager.clearFocus()
                    signInViewModel.signIn()
                },
                isLoading = signInState.isLoading,
            )
        }
    }
}

@Composable
private fun SignInButton(
    isDisable: Boolean,
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
            .height(54.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(color = if (isDisable) GreyLight else BlueLight)
            .clickable(
                enabled = !isDisable && !isLoading,
                onClick = onClick,
            ),
    ) {
        if (isLoading) {
            CommonProgressIndicator(color = White)
        } else {
            Text(
                text = stringResource(R.string.login),
                textAlign = TextAlign.Center,
                color = White,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@Composable
private fun NavToSignUpButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.sign_up),
        textAlign = TextAlign.Start,
        color = Blue,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = onClick,
        ),
    )
}
