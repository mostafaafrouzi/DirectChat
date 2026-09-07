package com.afrouzi.directchat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.afrouzi.directchat.data.model.Messenger
import com.afrouzi.directchat.data.model.MessengerStatus

@Composable
fun MessengerGrid(
    statuses: List<MessengerStatus>,
    isPersian: Boolean,
    onMessengerClick: (Messenger) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Group messengers into rows of 2 for a symmetrical, uniform grid
        statuses.chunked(2).forEach { rowStatuses ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (rowStatuses.size == 1) {
                    val status = rowStatuses.first()
                    MessengerCard(
                        status = status,
                        isPersian = isPersian,
                        onClick = { onMessengerClick(status.messenger) },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    rowStatuses.forEach { status ->
                        MessengerCard(
                            status = status,
                            isPersian = isPersian,
                            onClick = { onMessengerClick(status.messenger) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
