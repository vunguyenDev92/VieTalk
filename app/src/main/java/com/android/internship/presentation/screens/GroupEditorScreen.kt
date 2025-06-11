package com.android.internship.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.internship.R
import com.android.internship.presentation.navigation.Screen

@Composable
fun GroupEditorScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    title: String = "Create New Group",
    initialName: String = "",
    initialMembers: List<String> = emptyList(),
) {
    var groupName by remember { mutableStateOf(initialName) }
    var memberInput by remember { mutableStateOf("") }
    var members by remember { mutableStateOf(initialMembers) }

    val canSubmit = members.isNotEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    modifier = Modifier.size(30.dp),
                )
            }
            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 56.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        CustomTextField(
            value = groupName,
            onValueChange = { groupName = it },
            label = "Name Group (Optional)",
            hint = "Enter Name Group",
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = memberInput,
            onValueChange = { memberInput = it },
            label = "Members",
            hint = "Member ID",
            modifier = Modifier.fillMaxWidth(),
            trailingContent = {
                TextButton(
                    onClick = {
                        if (memberInput.isNotBlank()) {
                            members = members + memberInput.trim()
                            memberInput = ""
                        }
                    },
                ) {
                    Text("ADD")
                }
            },
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(members.size) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "ID " + members[index],
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W700,
                    )
                    IconButton(onClick = {
                        members = members.filterIndexed { i, _ -> i != index }
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Remove")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navController.navigate(Screen.ChatList) },
            enabled = canSubmit,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = if (title.contains("update", true)) "Update" else "Create")
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
    trailingContent: (@Composable (() -> Unit))? = null,
) {
    Column(modifier = modifier.padding(vertical = 6.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 4.dp),
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.medium,
                        )
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty() && hint != null) {
                            Text(
                                text = hint,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            )
                        }
                        innerTextField()
                    }

                    trailingContent?.let {
                        Spacer(modifier = Modifier.width(8.dp))
                        it()
                    }
                }
            },
        )
    }
}
