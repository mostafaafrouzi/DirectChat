package com.afrouzi.directchat.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.afrouzi.directchat.R
import com.afrouzi.directchat.ui.components.ClipboardDetectionCard
import com.afrouzi.directchat.ui.components.FallbackActionDialog
import com.afrouzi.directchat.ui.components.MessageInputField
import com.afrouzi.directchat.ui.components.MessengerGrid
import com.afrouzi.directchat.ui.components.MessengerVariantDialog
import com.afrouzi.directchat.ui.components.PhoneInputField
import com.afrouzi.directchat.ui.viewmodel.DirectChatUiState
import com.afrouzi.directchat.ui.viewmodel.DirectChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: DirectChatUiState,
    viewModel: DirectChatViewModel,
    isPersian: Boolean,
    themePreference: String,
    onToggleTheme: () -> Unit,
    onToggleLanguage: () -> Unit,
    onNavigateToAbout: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle Snackbar messages
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            color = androidx.compose.ui.graphics.Color.White,
                            shadowElevation = 2.dp
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_app_logo),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.app_name),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.app_tagline),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    // Quick Language Switcher Button (FA / EN)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onToggleLanguage() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = "Language",
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isPersian) "EN" else "فا",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Quick Dark / Light / Auto Theme Switcher Button
                    IconButton(onClick = onToggleTheme) {
                        val icon = when (themePreference) {
                            "dark" -> Icons.Default.DarkMode
                            "light" -> Icons.Default.LightMode
                            else -> Icons.Default.BrightnessAuto
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = "Theme",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // About Screen Button
                    IconButton(onClick = onNavigateToAbout) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(R.string.about_title),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Floating Clipboard Detection Card
            ClipboardDetectionCard(
                detectedPhone = uiState.detectedClipboardPhone,
                onApply = { viewModel.applyDetectedClipboard() },
                onDismiss = { viewModel.dismissClipboardCard() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Phone Input
            PhoneInputField(
                value = uiState.phoneInput,
                parsedPhone = uiState.parsedPhone,
                onValueChange = { viewModel.onPhoneChanged(it) },
                onClear = { viewModel.clearPhone() },
                onPaste = { viewModel.pastePhone(it) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Optional Message Field
            MessageInputField(
                message = uiState.messageInput,
                isExpanded = uiState.isMessageExpanded,
                onMessageChange = { viewModel.onMessageChanged(it) },
                onToggleExpand = { viewModel.toggleMessageExpanded() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Messenger Section Header
            Text(
                text = stringResource(R.string.messengers_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(R.string.messengers_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Messengers Grid (8 symmetric cards)
            MessengerGrid(
                statuses = uiState.messengerStatuses,
                isPersian = isPersian,
                onMessengerClick = { messenger ->
                    viewModel.onMessengerClicked(context, messenger)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Variant Picker Dialog (when WhatsApp or Telegram has multiple installed versions)
    uiState.variantPickerMessenger?.let { messenger ->
        MessengerVariantDialog(
            messenger = messenger,
            variants = uiState.variantPickerList,
            isPersian = isPersian,
            onSelectVariant = { variant ->
                viewModel.onVariantSelected(context, variant)
            },
            onDismiss = { viewModel.dismissVariantPicker() }
        )
    }

    // Fallback Dialog if messenger not installed or launch failed
    uiState.fallbackMessenger?.let { messenger ->
        FallbackActionDialog(
            messenger = messenger,
            isPersian = isPersian,
            onOpenWeb = { viewModel.openWebFallback(context, messenger) },
            onOpenStore = { viewModel.openStore(context, messenger) },
            onCopyLink = { viewModel.copyDirectLink(context, messenger) },
            onDismiss = { viewModel.dismissFallbackDialog() }
        )
    }
}
