package com.android.internship.presentation.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.android.internship.data.model.User
import com.android.internship.presentation.components.MessageItem

@Composable
fun TypingIndicatorComponent(
	item: MessageItem.TypingIndicator,
	modifier: Modifier = Modifier,
							) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 8.dp, vertical = 12.dp),
		horizontalArrangement = Arrangement.Start,
		verticalAlignment = Alignment.CenterVertically,
	   ) {
		AvatarStack(users = item.typingUsers)

		Spacer(modifier = Modifier.width(12.dp))

		Text(
			text = item.displayText,
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			)
	}
}

@Composable
private fun AvatarStack(users: List<User>) {
	val maxVisibleAvatars = 2
	val usersToShow = users.take(maxVisibleAvatars)
	val remainingCount = users.size - usersToShow.size

	Row(
		horizontalArrangement = Arrangement.spacedBy((-10).dp),
		verticalAlignment = Alignment.CenterVertically
	   ) {
		usersToShow.forEach { user ->
			AsyncImage(
				model = user.avatar,
				contentDescription = "Avatar of ${user.username}",
				modifier = Modifier
					.size(32.dp)
					.clip(CircleShape)
					.border(2.dp, MaterialTheme.colorScheme.background, CircleShape),
				contentScale = ContentScale.Crop
					  )
		}

		if (remainingCount > 0) {
			Box(
				modifier = Modifier
					.size(32.dp)
					.clip(CircleShape)
					.background(MaterialTheme.colorScheme.surfaceVariant)
					.border(2.dp, MaterialTheme.colorScheme.background, CircleShape),
				contentAlignment = Alignment.Center
			   ) {
				Text(
					text = "+$remainingCount",
					style = MaterialTheme.typography.bodySmall,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.onSurfaceVariant
					)
			}
		}
	}
}
