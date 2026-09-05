package com.afrouzi.directchat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.afrouzi.directchat.R
import com.afrouzi.directchat.data.model.MessengerStatus

@Composable
fun MessengerCard(
    status: MessengerStatus,
    isPersian: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val messenger = status.messenger
    val brandColor = Color(messenger.brandColorHex)
    val isInstalled = status.isInstalled

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(82.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = if (isInstalled) MaterialTheme.colorScheme.surface
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        tonalElevation = if (isInstalled) 3.dp else 0.dp,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isInstalled) brandColor.copy(alpha = 0.45f)
            else MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            // Messenger Icon Container with original authentic graphic colors
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(brandColor.copy(alpha = if (isInstalled) 0.14f else 0.06f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = messenger.iconRes),
                    contentDescription = messenger.nameEn,
                    modifier = Modifier.size(30.dp),
                    alpha = if (isInstalled) 1.0f else 0.45f
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (isPersian) messenger.nameFa else messenger.nameEn,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isInstalled) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(
                                if (isInstalled) Color(0xFF10B981)
                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            )
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isInstalled) {
                            if (status.installedVariants.size > 1) {
                                if (isPersian) "${status.installedVariants.size} نسخه نصب‌شده"
                                else "${status.installedVariants.size} installed"
                            } else {
                                stringResource(R.string.status_installed)
                            }
                        } else {
                            stringResource(R.string.status_not_installed)
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isInstalled) Color(0xFF10B981)
                                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
