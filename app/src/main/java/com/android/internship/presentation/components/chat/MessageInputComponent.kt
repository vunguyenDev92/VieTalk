package com.android.internship.presentation.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.internship.R
import com.android.internship.presentation.theme.ButtonSend

// Enum để quản lý trạng thái input
enum class InputState {
    KEYBOARD,
    EMOJI_PICKER,
    NONE,
}

@Composable
fun MessageInputComponent(
    messageText: TextFieldValue,
    onMessageChange: (TextFieldValue) -> Unit,
    onSendMessage: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Message",
    isEnabled: Boolean = true,
    onEmojiClick: () -> Unit,
    onEmojiPickerVisibilityChange: ((Boolean) -> Unit)? = null,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    // Biến trạng thái chính để quản lý keyboard và emoji picker
    var inputState by remember { mutableStateOf(InputState.NONE) }
    var isInputFocused by remember { mutableStateOf(false) }

    // Tính toán showEmojiPicker dựa trên inputState
    val showEmojiPicker = inputState == InputState.EMOJI_PICKER

    LaunchedEffect(showEmojiPicker) {
        onEmojiPickerVisibilityChange?.invoke(showEmojiPicker)
    }

    // Quản lý trạng thái focus - FIX: Đóng emoji picker khi focus text field
    LaunchedEffect(isInputFocused) {
        if (isInputFocused) {
            if (inputState == InputState.EMOJI_PICKER) {
                // Đóng emoji picker khi focus vào text field
                inputState = InputState.KEYBOARD
            } else if (inputState != InputState.KEYBOARD) {
                inputState = InputState.KEYBOARD
            }
        }
    }

    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                when (inputState) {
                                    InputState.EMOJI_PICKER -> {
                                        // Đang hiện emoji picker -> ẩn đi
                                        inputState = InputState.NONE
                                    }
                                    InputState.KEYBOARD -> {
                                        // Đang hiện keyboard -> ẩn keyboard và hiện emoji picker
                                        focusRequester.freeFocus()
                                        keyboardController?.hide()
                                        inputState = InputState.EMOJI_PICKER
                                    }
                                    InputState.NONE -> {
                                        // Không có gì hiện -> hiện emoji picker
                                        focusRequester.freeFocus()
                                        keyboardController?.hide()
                                        inputState = InputState.EMOJI_PICKER
                                    }
                                }
                                onEmojiClick()
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_emoji),
                            contentDescription = "Emoji",
                            tint = if (showEmojiPicker) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        BasicTextField(
                            value = messageText,
                            onValueChange = onMessageChange,
                            enabled = isEnabled,
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                            ),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Send,
                            ),
                            keyboardActions = KeyboardActions(
                                onSend = {
                                    if (messageText.text.isNotBlank()) {
                                        onSendMessage()
                                        focusRequester.freeFocus()
                                        keyboardController?.hide()
                                        inputState = InputState.NONE
                                    }
                                },
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .onFocusChanged { focusState ->
                                    isInputFocused = focusState.isFocused
                                    if (!focusState.isFocused && inputState == InputState.KEYBOARD) {
                                        inputState = InputState.NONE
                                    }
                                }
                                .clickable {
                                    if (inputState == InputState.EMOJI_PICKER) {
                                        inputState = InputState.KEYBOARD
                                    }
                                    focusRequester.requestFocus()
                                },
                        )

                        if (messageText.text.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                fontSize = 16.sp,
                            )
                        }
                    }
                }
            }

            IconButton(
                onClick = {
                    if (messageText.text.isNotBlank()) {
                        onSendMessage()
                        focusRequester.freeFocus()
                        keyboardController?.hide()
                        inputState = InputState.NONE
                    }
                },
                enabled = isEnabled && messageText.text.isNotBlank(),
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (messageText.text.isNotBlank()) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            ButtonSend
                        },
                    ),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "Send",
                    tint = if (messageText.text.isNotBlank()) {
                        Color.White
                    } else {
                        Color.White
                    },
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        if (showEmojiPicker) {
            EmojiPickerInline(
                onEmojiSelected = { emoji ->
                    val currentText = messageText.text
                    val selection = messageText.selection
                    val newText = currentText.substring(0, selection.start) +
                        emoji +
                        currentText.substring(selection.end)
                    val newSelection = TextRange(selection.start + emoji.length)
                    onMessageChange(TextFieldValue(newText, newSelection))

                    // Giữ emoji picker hiện để có thể chọn tiếp emoji khác
                    focusRequester.freeFocus()
                    keyboardController?.hide()
                    inputState = InputState.EMOJI_PICKER
                },
                onDismiss = {
                    inputState = InputState.NONE
                },
                keyboardController = keyboardController,
                focusRequester = focusRequester,
            )
        }
    }
}

@Composable
fun EmojiPickerInline(
    onEmojiSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    keyboardController: SoftwareKeyboardController? = null,
    focusRequester: FocusRequester? = null,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 0.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Chọn emoji",
                    style = MaterialTheme.typography.titleSmall,
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(24.dp),
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                        contentDescription = "Close",
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            EmojiGrid(
                onEmojiSelected = { emoji ->
                    focusRequester?.freeFocus()
                    keyboardController?.hide()
                    onEmojiSelected(emoji)
                },
            )
        }
    }
}

@Composable
fun EmojiGrid(
    onEmojiSelected: (String) -> Unit,
) {
    val emojis = remember {
        listOf(
            "😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣",
            "😊", "😇", "🙂", "🙃", "😉", "😌", "😍", "🥰",
            "😘", "😗", "😙", "😚", "😋", "😛", "😝", "😜",
            "🤪", "🤨", "🧐", "🤓", "😎", "🤩", "🥳", "😏",
            "😒", "😞", "😔", "😟", "😕", "🙁", "☹️", "😣",
            "😖", "😫", "😩", "🥺", "😢", "😭", "😤", "😠",
            "😡", "🤬", "🤯", "😳", "🥵", "🥶", "😱", "😨",
            "😰", "😥", "😓", "🤗", "🤔", "🤭", "🤫", "🤥",
            "😶", "😐", "😑", "😬", "🙄", "😯", "😦", "😧",
            "😮", "😲", "🥱", "😴", "🤤", "😪", "😵", "🤐",
            "👍", "👎", "👌", "✌️", "🤞", "🤟", "🤘", "🤙",
            "👈", "👉", "👆", "👇", "☝️", "✋", "🤚", "🖐",
            "🖖", "👋", "🤏", "💪", "🦾", "🖕", "✍️", "🙏",
            "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍",
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(8),
        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(emojis) { emoji ->
            Text(
                text = emoji,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable {
                        onEmojiSelected(emoji)
                    }
                    .padding(4.dp),
            )
        }
    }
}
