package com.android.internship.presentation.screens.groupeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.android.internship.R
import com.android.internship.presentation.navigation.Screen
import com.android.internship.presentation.theme.BlueLight
import com.android.internship.presentation.theme.GreyLight
import com.android.internship.presentation.theme.LightRed
import com.android.internship.presentation.theme.White

@Composable
fun GroupEditorScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    isCreate: Boolean = true,
    initialName: String = "",
    initialMembers: Set<String> = emptySet(),
    viewModel: GroupEditorViewModel = viewModel(
        factory = GroupEditorViewModel.factory(navController.context, initialName, initialMembers),
    ),
) {
    val groupEditorState by viewModel.state.collectAsState()

    if (groupEditorState.isSuccess) {
        groupEditorState.groupId?.let {
            navController.navigate(Screen.Chat(it)) {
                launchSingleTop = true
                popUpTo(Screen.ChatList) { inclusive = true }
            }
        }
    }

    Column(
        modifier = modifier
            .padding(bottom = 100.dp)
            .fillMaxSize(),
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 85.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = stringResource(R.string.back),
                modifier = Modifier
                    .padding(start = 15.dp)
                    .size(30.dp)
                    .clickable(
                        onClick = { navController.popBackStack(Screen.ChatList, inclusive = false) },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ),
            )
            Text(
                text = if (isCreate) stringResource(R.string.create_new_group) else "Edit group",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        CustomTextField(
            value = groupEditorState.groupName,
            onValueChange = {
                viewModel.onEvent(
                    event = GroupEditorViewModel
                        .GroupEditorEvent
                        .OnNameInputChange(it),
                )
            },
            label = stringResource(R.string.name_group_optional),
            hint = stringResource(R.string.enter_name_group),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(bottom = 24.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp)),
        )

        CustomTextField(
            value = groupEditorState.memberInput,
            onValueChange = {
                viewModel.onEvent(
                    event = GroupEditorViewModel.GroupEditorEvent.OnMemberInputChange(it),
                )
            },
            label = stringResource(R.string.members),
            hint = stringResource(R.string.member_id),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(bottom = 21.dp)
                .fillMaxWidth(),
            trailingContent = {
                Text(
                    stringResource(R.string.add).uppercase(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (groupEditorState.memberInput.isBlank()) {
                            Color(0x80333333)
                        } else {
                            Color(0xFF0288E9)
                        },
                    ),
                    modifier = Modifier
                        .padding(start = 12.dp, end = 16.dp)
                        .clickable(
                            onClick = {
                                if (groupEditorState.memberInput.isNotBlank()) {
                                    viewModel.onEvent(
                                        event = GroupEditorViewModel.GroupEditorEvent.OnMembersChange(
                                            groupEditorState.members + groupEditorState.memberInput.trim(),
                                        ),
                                    )
                                    viewModel.onEvent(
                                        event = GroupEditorViewModel.GroupEditorEvent.OnMemberInputChange(
                                            "",
                                        ),
                                    )
                                }
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ),
                )
            },
        )

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 8.dp),
        ) {
            items(items = groupEditorState.members.toList()) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.id) + item,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W700,
                        ),
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_remove),
                        contentDescription = stringResource(R.string.remove),
                        tint = LightRed,
                        modifier = Modifier.size(24.dp).clickable(
                            onClick = {
                                viewModel.onEvent(
                                    event = GroupEditorViewModel.GroupEditorEvent.OnMembersChange(
                                        groupEditorState.members.filter { it != item }.toSet(),
                                    ),
                                )
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ),
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        SubmitButton(
            text = if (isCreate) {
                stringResource(R.string.create)
            } else {
                stringResource(R.string.update)
            },
            onclick = {
                if (isCreate) {
                    viewModel.createGroup()
                } else {
                    viewModel.updateGroup()
                }
            },
            modifier = Modifier.padding(horizontal = 33.dp).fillMaxWidth(),
            enabled = groupEditorState.canSubmit,
        )
    }
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
    trailingContent: (@Composable (() -> Unit))? = null,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = Color(0x80333333),
            fontSize = 12.sp,
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
                        .height(56.dp)
                        .border(
                            width = 1.dp,
                            color = Color(0xffd0d1db),
                            shape = RoundedCornerShape(8.dp),
                        )
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty() && hint != null) {
                            Text(
                                text = hint,
                                color = Color(0xffcccccc),
                            )
                        }
                        innerTextField()
                    }

                    trailingContent?.invoke()
                }
            },
        )
    }
}

@Composable
private fun SubmitButton(
    text: String,
    onclick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .padding(vertical = 33.dp)
            .height(54.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(
                color = if (enabled) {
                    BlueLight
                } else {
                    GreyLight
                },
            )
            .clickable(enabled = enabled, onClick = onclick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.W600,
                color = White,
            ),
        )
    }
}
