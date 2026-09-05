package com.afrouzi.directchat.ui.viewmodel

import com.afrouzi.directchat.data.model.Messenger
import com.afrouzi.directchat.data.model.MessengerStatus
import com.afrouzi.directchat.data.model.MessengerVariant
import com.afrouzi.directchat.data.model.ParsedPhone

data class DirectChatUiState(
    val phoneInput: String = "",
    val parsedPhone: ParsedPhone = ParsedPhone.empty(),
    val messageInput: String = "",
    val isMessageExpanded: Boolean = false,
    val detectedClipboardPhone: String? = null,
    val messengerStatuses: List<MessengerStatus> = emptyList(),
    val snackbarMessage: String? = null,
    val fallbackMessenger: Messenger? = null,
    val variantPickerMessenger: Messenger? = null,
    val variantPickerList: List<MessengerVariant> = emptyList()
)
