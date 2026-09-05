package com.afrouzi.directchat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.afrouzi.directchat.data.model.Messenger

@Composable
fun FallbackActionDialog(
    messenger: Messenger,
    isPersian: Boolean,
    onOpenWeb: () -> Unit,
    onOpenStore: () -> Unit,
    onCopyLink: () -> Unit,
    onDismiss: () -> Unit
) {
    val brandColor = Color(messenger.brandColorHex)
    val messengerTitle = if (isPersian) messenger.nameFa else messenger.nameEn

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(brandColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = messenger.iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = messengerTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (isPersian)
                        "این پیام‌رسان روی دستگاه شما یافت نشد یا باز نشد. از گزینه‌های زیر برای ادامه گفتگو استفاده کنید:"
                    else
                        "This messenger is not installed or couldn't be opened. Choose an alternative action:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Option 1: Web
                FallbackOptionRow(
                    icon = Icons.Default.Language,
                    title = if (isPersian) "نسخه تحت وب" else "Open Web",
                    subtitle = if (isPersian) "باز کردن در مرورگر اینترنت" else "Open in web browser",
                    accentColor = brandColor,
                    onClick = onOpenWeb
                )

                // Option 2: Store
                FallbackOptionRow(
                    icon = Icons.Default.Shop,
                    title = if (isPersian) "نصب از استور" else "Install App",
                    subtitle = if (isPersian) "کافه‌بازار / مایکت / گوگل‌پلی" else "Install from store",
                    accentColor = brandColor,
                    onClick = onOpenStore
                )

                // Option 3: Copy Link
                FallbackOptionRow(
                    icon = Icons.Default.ContentCopy,
                    title = if (isPersian) "کپی لینک مستقیم" else "Copy Link",
                    subtitle = if (isPersian) "کپی برای ارسال به دیگران" else "Copy direct chat URL",
                    accentColor = brandColor,
                    onClick = onCopyLink
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = if (isPersian) "بستن" else "Close")
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@Composable
private fun FallbackOptionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}