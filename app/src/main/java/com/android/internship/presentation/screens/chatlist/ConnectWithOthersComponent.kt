package com.android.internship.presentation.screens.chatlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.android.internship.R

@Composable
fun ConnectWithOthersItem(
    chatItems: List<ChatListState.ChatUserItemState>,
    onClick: () -> Unit,
) {
    Column {
        Text(
            text = stringResource(R.string.connect_with_others),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.W300,
            ),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
        )
        LazyRow(
            modifier = Modifier.padding(start = 20.dp, top = 11.dp, bottom = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            items(chatItems) { item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AsyncImage(
                        model = item.avatar,
                        contentDescription = "Avatar member 1",
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.ic_person),
                        error = painterResource(R.drawable.ic_person_color),
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape).clickable(
                                onClick = onClick,
                            ),
                    )
                    Text(
                        text = item.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.W300,
                        ),
                    )
                }
            }
        }
        HorizontalDivider(thickness = 1.dp)
    }
}
