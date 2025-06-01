package com.android.internship.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.android.internship.presentation.theme.Black10
import com.android.internship.presentation.theme.Black35

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
    Column(modifier = modifier) {
        OutlinedTextField(
            value = textFieldState.value,
            onValueChange = onValueChange,
            label = { Text(label, style = MaterialTheme.typography.bodyLarge) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (isTextObscured) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                },
            ),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Black10,
                unfocusedBorderColor = Black10,
                focusedLabelColor = Black35,
                unfocusedLabelColor = Black35,
            ),
        )
        if (textFieldState.isError && textFieldState.errorMessage != null) {
            Text(
                text = textFieldState.errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
