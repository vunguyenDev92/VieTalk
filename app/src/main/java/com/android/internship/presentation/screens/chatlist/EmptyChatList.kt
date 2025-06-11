package com.android.internship.presentation.screens.chatlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.internship.R
import com.android.internship.presentation.theme.Black

@Composable
fun EmptyChatList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_empty_list_message_1),
            contentDescription = stringResource(R.string.empty_list_illustration),
            modifier = Modifier.size(180.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.it_looks_like_you_haven_t_added_anything_here),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.W300,
            ),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.tap_the),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W300,
                ),
                textAlign = TextAlign.Center,
            )
            Icon(
                painter = painterResource(R.drawable.ic_group),
                contentDescription = stringResource(R.string.create_new_group_icon),
                tint = Black,
                modifier = Modifier.size(24.dp),
            )
            Text(
                text = stringResource(R.string.button_to_get_started),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W300,
                ),
                textAlign = TextAlign.Center,
            )
        }
    }
}
