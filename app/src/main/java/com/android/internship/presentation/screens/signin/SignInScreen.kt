package com.android.internship.presentation.screens.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.android.internship.R
import com.android.internship.presentation.components.CommonTextField
import com.android.internship.presentation.navigation.Screen
import com.android.internship.presentation.theme.Accent

@Composable
fun SignInScreen(
    navController: NavController,
    signInViewModel: SignInViewModel =
        viewModel(factory = SignInViewModel.factory(navController.context)),
) {
    val signInState by signInViewModel.state.collectAsState()

    if (signInState.isLoading) {
        Dialog(onDismissRequest = {}) {
            CircularProgressIndicator()
        }
    }

    if (signInState.signInSuccess) {
        SideEffect {
            navController.navigate(route = Screen.Chat) {
                popUpTo(Screen.SignIn) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Scaffold {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 20.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item { SignInHeader() }
            item {
                CommonTextField(
                    label = stringResource(R.string.your_email),
                    textFieldState = signInState.emailState,
                    onValueChange = signInViewModel::updateEmailState,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
            item {
                CommonTextField(
                    label = stringResource(R.string.your_password),
                    textFieldState = signInState.passwordState,
                    onValueChange = signInViewModel::updatePasswordState,
                    modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 20.dp),
                    imeAction = ImeAction.Done,
                    isTextObscured = true,
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DontHaveAnAccountButton(
                        onClick = {
                            navController.navigate(Screen.SignUp) {
                                launchSingleTop = true
                            }
                        },
                    )

                    SignInButton(
                        isDisable = signInState.emailState.isError || signInState.passwordState.isError,
                        onClick = { signInViewModel.signIn() },
                    )
                }
            }
        }
    }
}

@Composable
private fun SignInHeader() {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = stringResource(R.string.app_logo),
        modifier = Modifier.size(100.dp),
    )
    Text(
        text = stringResource(R.string.sign_in),
        textAlign = TextAlign.Center,
        color = Accent,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 20.dp),
    )
}

@Composable
private fun SignInButton(
    isDisable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(
            id = if (isDisable) {
                R.drawable.arrow_right_disable
            } else {
                R.drawable.arrow_right_enable
            },
        ),
        contentDescription = stringResource(R.string.sign_in),
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(1000.dp))
            .clickable(
                enabled = !isDisable,
                onClick = onClick,
            ),
    )
}

@Composable
private fun DontHaveAnAccountButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.don_t_have_an_account),
        textAlign = TextAlign.Start,
        color = Accent,
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier.clickable(onClick = onClick),
    )
}
