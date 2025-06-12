package com.android.internship.presentation.screens.groupeditor

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun GroupEditorScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.create_new_group),
    initialName: String = "",
    initialMembers: List<String> = emptyList(),
    viewModel: GroupEditorViewModel = viewModel(
        factory = GroupEditorViewModel.factory(navController.context, initialName, initialMembers),
    ),
) {
    val groupEditorState by viewModel.state.collectAsState()

    val canSubmit = groupEditorState.members.isNotEmpty()

    if (groupEditorState.isSuccess) {
        navController.popBackStack()
    }

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
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier.size(30.dp),
                )
            }
            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 56.dp),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    textAlign = TextAlign.Center,
                ),
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

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
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = groupEditorState.memberInput,
            onValueChange = {
                viewModel.onEvent(
                    event = GroupEditorViewModel
                        .GroupEditorEvent
                        .OnMemberInputChange(it),
                )
            },
            label = stringResource(R.string.members),
            hint = stringResource(R.string.member_id),
            modifier = Modifier.fillMaxWidth(),
            trailingContent = {
                TextButton(
                    onClick = {
                        if (groupEditorState.memberInput.isNotBlank()) {
                            viewModel.onEvent(
                                event = GroupEditorViewModel
                                    .GroupEditorEvent
                                    .OnMembersChange(
                                        groupEditorState.members + groupEditorState.memberInput.trim(),
                                    ),
                            )
                            viewModel.onEvent(
                                event = GroupEditorViewModel
                                    .GroupEditorEvent
                                    .OnNameInputChange(""),
                            )
                        }
                    },
                ) {
                    Text(
                        stringResource(R.string.add).uppercase(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0x80333333),
                        ),
                    )
                }
            },
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(groupEditorState.members.size) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "ID " + groupEditorState.members[index],
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W700,
                        ),
                    )
                    IconButton(onClick = {
                        viewModel.onEvent(
                            event = GroupEditorViewModel
                                .GroupEditorEvent
                                .OnMembersChange(
                                    groupEditorState.members.filterIndexed { i, _ -> i != index },
                                ),
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(
                                R.string.remove,
                            ),
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (title == stringResource(R.string.create_new_group)) {
            Button(
                onClick = {
                    viewModel.createGroup()
                },
                enabled = canSubmit,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.create),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.W600,
                    ),
                )
            }
        } else {
            Button(
                onClick = {
                    // TODO: viewModel update group
                },
                enabled = canSubmit,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.create),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.W600,
                    ),
                )
            }
        }
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
    Column(modifier = modifier.padding(vertical = 6.dp)) {
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
                            shape = RoundedCornerShape(1.dp),
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

                    trailingContent?.let {
                        Spacer(modifier = Modifier.width(8.dp))
                        it()
                    }
                }
            },
        )
    }
}
