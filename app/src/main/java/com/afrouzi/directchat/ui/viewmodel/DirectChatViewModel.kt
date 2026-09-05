package com.afrouzi.directchat.ui.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afrouzi.directchat.R
import com.afrouzi.directchat.data.model.Messenger
import com.afrouzi.directchat.data.model.MessengerVariant
import com.afrouzi.directchat.domain.engine.ClipboardWatcher
import com.afrouzi.directchat.domain.engine.IntentRouter
import com.afrouzi.directchat.domain.engine.PhoneNormalizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class DirectChatViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DirectChatUiState())
    val uiState: StateFlow<DirectChatUiState> = _uiState.asStateFlow()

    fun onPhoneChanged(input: String) {
        val parsed = PhoneNormalizer.parse(input)
        _uiState.update { it.copy(phoneInput = input, parsedPhone = parsed) }
    }

    fun onMessageChanged(input: String) {
        _uiState.update { it.copy(messageInput = input) }
    }

    fun toggleMessageExpanded() {
        _uiState.update { it.copy(isMessageExpanded = !it.isMessageExpanded) }
    }

    fun clearPhone() {
        _uiState.update { it.copy(phoneInput = "", parsedPhone = PhoneNormalizer.parse("")) }
    }

    fun pastePhone(text: String) {
        val clean = PhoneNormalizer.sanitize(text)
        onPhoneChanged(clean)
    }

    fun checkClipboard(context: Context) {
        viewModelScope.launch {
            val detected = ClipboardWatcher.getDetectedPhone(context)
            if (detected != null && detected != _uiState.value.phoneInput) {
                _uiState.update { it.copy(detectedClipboardPhone = detected) }
            }
        }
    }

    fun applyDetectedClipboard() {
        val detected = _uiState.value.detectedClipboardPhone ?: return
        pastePhone(detected)
        _uiState.update { it.copy(detectedClipboardPhone = null) }
    }

    fun dismissClipboardCard() {
        _uiState.update { it.copy(detectedClipboardPhone = null) }
    }

    fun refreshStatuses(context: Context) {
        val statuses = IntentRouter.getAllStatuses(context)
        _uiState.update { it.copy(messengerStatuses = statuses) }
    }

    fun onMessengerClicked(context: Context, messenger: Messenger) {
        val state = _uiState.value
        val phone = state.parsedPhone
        if (!phone.isValid) {
            _uiState.update { it.copy(snackbarMessage = context.getString(R.string.error_invalid_number)) }
            return
        }

        val status = state.messengerStatuses.find { it.messenger == messenger }
        val isInstalled = status?.isInstalled ?: false

        if (isInstalled) {
            val installedVariants = status?.installedVariants.orEmpty()
            if (installedVariants.size > 1) {
                // If multiple variants installed (e.g. WhatsApp + WhatsApp Business, or Telegram + Telegram Direct)
                _uiState.update {
                    it.copy(
                        variantPickerMessenger = messenger,
                        variantPickerList = installedVariants
                    )
                }
            } else if (installedVariants.size == 1) {
                launchMessenger(context, messenger, installedVariants.first().packageName)
            } else {
                launchMessenger(context, messenger, status?.installedPackageName)
            }
        } else {
            // Messenger not installed, show fallback options dialog
            _uiState.update { it.copy(fallbackMessenger = messenger) }
        }
    }

    fun onVariantSelected(context: Context, variant: MessengerVariant) {
        val messenger = _uiState.value.variantPickerMessenger ?: return
        dismissVariantPicker()
        launchMessenger(context, messenger, variant.packageName)
    }

    fun dismissVariantPicker() {
        _uiState.update { it.copy(variantPickerMessenger = null, variantPickerList = emptyList()) }
    }

    fun launchMessenger(context: Context, messenger: Messenger, targetPackage: String? = null) {
        val state = _uiState.value
        val phone = state.parsedPhone
        try {
            val intent = IntentRouter.buildDirectIntent(
                messenger = messenger,
                phone = phone,
                message = state.messageInput,
                targetPackage = targetPackage
            )
            context.startActivity(intent)
        } catch (_: Exception) {
            // Smart Fallback: launch app directly via launch intent & copy number to clipboard
            val pkg = targetPackage ?: messenger.primaryPackage
            val launchIntent = context.packageManager.getLaunchIntentForPackage(pkg)
            if (launchIntent != null) {
                try {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("DirectChat Phone", phone.rawInput)
                    clipboard.setPrimaryClip(clip)
                    context.startActivity(launchIntent)
                    val appName = messenger.nameFa
                    val msg = context.getString(R.string.copied_and_opened, appName)
                    _uiState.update { it.copy(snackbarMessage = msg) }
                    return
                } catch (_: Exception) {}
            }
            _uiState.update { it.copy(fallbackMessenger = messenger) }
        }
    }

    fun openWebFallback(context: Context, messenger: Messenger) {
        try {
            val intent = IntentRouter.buildWebFallbackIntent(
                messenger = messenger,
                phone = _uiState.value.parsedPhone,
                message = _uiState.value.messageInput
            )
            context.startActivity(intent)
        } catch (_: Exception) {
            _uiState.update { it.copy(snackbarMessage = context.getString(R.string.error_app_not_found)) }
        }
        dismissFallbackDialog()
    }

    fun openStore(context: Context, messenger: Messenger) {
        try {
            val intent = IntentRouter.buildStoreIntent(messenger.storePackageName)
            context.startActivity(intent)
        } catch (_: Exception) {
            _uiState.update { it.copy(snackbarMessage = context.getString(R.string.error_app_not_found)) }
        }
        dismissFallbackDialog()
    }

    fun copyDirectLink(context: Context, messenger: Messenger) {
        val link = IntentRouter.buildDirectLinkUrl(
            messenger = messenger,
            phone = _uiState.value.parsedPhone,
            message = _uiState.value.messageInput
        )
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("DirectChat Link", link)
        clipboard.setPrimaryClip(clip)
        _uiState.update {
            it.copy(snackbarMessage = context.getString(R.string.link_copied_success))
        }
        dismissFallbackDialog()
    }

    fun dismissFallbackDialog() {
        _uiState.update { it.copy(fallbackMessenger = null) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
