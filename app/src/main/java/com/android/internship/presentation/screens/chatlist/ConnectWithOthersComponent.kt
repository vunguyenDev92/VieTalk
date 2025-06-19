package com.android.internship.presentation.screens.chatlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.android.internship.R
import com.android.internship.presentation.components.CommonCircleAsyncImage

@Composable
fun ConnectWithOthersItem(
    chatItems: List<ChatListState.ChatUserItemState>,
    onClick: (String) -> Unit,
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
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                Spacer(modifier = Modifier.width(6.dp))
            }
            items(chatItems) { item ->
                Column(
                    modifier = Modifier.padding(end = 10.dp).width(50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CommonCircleAsyncImage(
                        item.avatar,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable(
                                onClick = { onClick(item.id) },
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
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(thickness = 1.dp)
    }
}
