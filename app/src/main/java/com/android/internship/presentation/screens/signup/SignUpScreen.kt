package com.android.internship.presentation.screens.signup

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
import androidx.compose.ui.platform.LocalContext
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
import com.android.internship.presentation.theme.BlueLight
import com.android.internship.presentation.theme.Green
import com.android.internship.presentation.theme.GreenDark
import com.android.internship.presentation.theme.GreenLight
import com.android.internship.presentation.theme.GreyLight
import com.android.internship.presentation.theme.White

@Composable
fun SignUpScreen(
    navController: NavController,
    signUpViewModel: SignUpViewModel =
        viewModel(factory = SignUpViewModel.factory(context = LocalContext.current)),
) {
    val signUpState by signUpViewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current

    if (signUpState.signUpSuccess) {
        SideEffect {
            navController.navigate(route = Screen.ChatList) {
                popUpTo(Screen.SignIn) { inclusive = true }
                launchSingleTop = true
            }
            CommonToastManager.makeToast(
                icon = R.drawable.ic_success,
                iconColor = Green,
                backgroundColor = GreenLight,
                borderColor = GreenDark,
            ).show(
                message = navController.context.getString(R.string.register_successful),
            )
        }
    }

    if (signUpState.signUpSuccess == false) {
        signUpState.message?.let {
            CommonDialog(
                title = stringResource(R.string.error),
                content = it,
                onDismissRequest = {
                    signUpViewModel.clearMessage()
                },
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = stringResource(R.string.back),
            modifier = Modifier
                .size(50.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        navController.popBackStack()
                    },
                ),
        )
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
                text = stringResource(R.string.sign_up),
                textAlign = TextAlign.Start,
                color = Black,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            CommonTextField(
                label = stringResource(R.string.email),
                textFieldState = signUpState.emailState,
                onValueChange = signUpViewModel::updateEmailState,
                modifier = Modifier,
            )
        }
        item {
            CommonTextField(
                label = stringResource(R.string.password),
                textFieldState = signUpState.passwordState,
                onValueChange = signUpViewModel::updatePasswordState,
                isTextObscured = true,
            )
        }
        item {
            CommonTextField(
                label = stringResource(R.string.confirm_password),
                textFieldState = signUpState.confirmPasswordState,
                onValueChange = signUpViewModel::updateConfirmPasswordState,
                imeAction = ImeAction.Done,
                isTextObscured = true,
            )
        }
        item {
            SignUpButton(
                isDisable = signUpState.emailState.isError || signUpState.passwordState.isError || signUpState.confirmPasswordState.isError,
                onClick = {
                    focusManager.clearFocus()
                    signUpViewModel.signUp()
                },
                isLoading = signUpState.isLoading,
            )
        }
    }
}

@Composable
private fun SignUpButton(
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
                text = stringResource(R.string.sign_up),
                textAlign = TextAlign.Center,
                color = White,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}
