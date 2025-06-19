package com.android.internship.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.android.internship.R
import com.android.internship.presentation.theme.Grey
import com.android.internship.presentation.theme.GreyLight

private object StringConstants {
    const val VISIBLE_CHAR = '*'
    const val SHOW_PASSWORD = "Show password"
    const val HIDE_PASSWORD = "Hide password"
}

@Composable
fun CommonTextField(
    label: String,
    textFieldState: TextFieldState,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
    isTextObscured: Boolean = false,
) {
    val focusManager = LocalFocusManager.current
    val isError = textFieldState.isError && textFieldState.errorMessage != null
    var isTextVisible by remember { mutableStateOf(true) }

    Box(
        modifier = modifier.padding(bottom = if (isError) 0.dp else 19.dp),
    ) {
        Box(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .border(
                    1.dp,
                    if (isError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        Color(0xffd1d1d1)
                    },
                    RoundedCornerShape(5.dp),
                ),
        )
        Box(
            modifier = Modifier.absoluteOffset(y = (-4).dp),
        ) {
            EditTextField(
                textFieldState = textFieldState,
                onValueChange = onValueChange,
                isTextObscured = isTextObscured,
                isTextVisible = isTextVisible,
                imeAction = imeAction,
                focusManager = focusManager,
            )
            if (isTextObscured) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Box(
                        modifier = Modifier
                            .size(1.dp, 24.dp)
                            .background(GreyLight),
                    )
                    Icon(
                        painter = painterResource(id = if (isTextVisible) R.drawable.ic_visible else R.drawable.ic_invisible),
                        contentDescription = if (isTextVisible) StringConstants.SHOW_PASSWORD else StringConstants.HIDE_PASSWORD,
                        tint = Color(0xff575757),
                        modifier = Modifier
                            .padding(start = 11.dp, end = 16.dp)
                            .size(24.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                            ) { isTextVisible = !isTextVisible },
                    )
                }
            }
        }
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W500),
            color = if (isError) MaterialTheme.colorScheme.error else Grey,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .offset(y = (-8).dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 8.dp),
        )
    }
    if (isError) {
        Text(
            text = textFieldState.errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(top = 7.dp, bottom = 17.dp)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun EditTextField(
    textFieldState: TextFieldState,
    onValueChange: (String) -> Unit,
    isTextObscured: Boolean,
    isTextVisible: Boolean,
    imeAction: ImeAction,
    focusManager: FocusManager,
) {
    TextField(
        value = textFieldState.value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.fillMaxWidth().padding(end = if (isTextObscured) 40.dp else 0.dp),
        singleLine = true,
        visualTransformation = if (isTextVisible && isTextObscured) {
            PasswordVisualTransformation(
                StringConstants.VISIBLE_CHAR,
            )
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            },
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        ),
    )
}
