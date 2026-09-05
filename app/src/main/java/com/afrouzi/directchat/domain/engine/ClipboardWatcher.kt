package com.afrouzi.directchat.domain.engine

import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context

object ClipboardWatcher {

    fun getDetectedPhone(context: Context): String? {
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                ?: return null

            if (!clipboard.hasPrimaryClip()) return null

            val clipDescription = clipboard.primaryClipDescription ?: return null
            if (!clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) &&
                !clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML)) {
                return null
            }

            val item = clipboard.primaryClip?.getItemAt(0) ?: return null
            val rawText = item.text?.toString() ?: return null

            if (rawText.isBlank() || rawText.length > 100) return null

            PhoneNormalizer.extractPotentialPhone(rawText)
        } catch (_: Exception) {
            null
        }
    }
}
