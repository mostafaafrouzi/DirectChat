package com.afrouzi.directchat.data.model

data class MessengerStatus(
    val messenger: Messenger,
    val isInstalled: Boolean,
    val installedPackageName: String?,
    val installedVariants: List<MessengerVariant> = emptyList()
)
