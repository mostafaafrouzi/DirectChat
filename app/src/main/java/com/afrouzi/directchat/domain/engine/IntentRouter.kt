package com.afrouzi.directchat.domain.engine

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.afrouzi.directchat.data.model.Messenger
import com.afrouzi.directchat.data.model.MessengerStatus
import com.afrouzi.directchat.data.model.MessengerVariant
import com.afrouzi.directchat.data.model.ParsedPhone
import java.net.URLEncoder

object IntentRouter {

    fun isPackageInstalled(context: Context, packageName: String): Boolean {
        val pm = context.packageManager
        // 1. Check getLaunchIntentForPackage
        try {
            if (pm.getLaunchIntentForPackage(packageName) != null) {
                return true
            }
        } catch (_: Exception) {}

        // 2. Check getPackageInfo
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                pm.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                pm.getPackageInfo(packageName, 0)
            }
            return true
        } catch (_: PackageManager.NameNotFoundException) {
        } catch (_: Exception) {}

        // 3. Check queryIntentActivities
        try {
            val intent = Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .setPackage(packageName)
            val list = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                pm.queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                pm.queryIntentActivities(intent, 0)
            }
            if (list.isNotEmpty()) return true
        } catch (_: Exception) {}

        return false
    }

    fun getMessengerStatus(context: Context, messenger: Messenger): MessengerStatus {
        if (messenger == Messenger.SMS) {
            return MessengerStatus(
                messenger = messenger,
                isInstalled = true,
                installedPackageName = null,
                installedVariants = messenger.variants.map { it.copy(isInstalled = true) }
            )
        }

        val installedVariants = mutableListOf<MessengerVariant>()
        for (variant in messenger.variants) {
            if (isPackageInstalled(context, variant.packageName)) {
                installedVariants.add(variant.copy(isInstalled = true))
            }
        }

        var installedPkg: String? = installedVariants.firstOrNull()?.packageName
        if (installedPkg == null) {
            for (pkg in messenger.allPackages) {
                if (isPackageInstalled(context, pkg)) {
                    installedPkg = pkg
                    break
                }
            }
        }

        val isInstalled = installedVariants.isNotEmpty() || installedPkg != null

        return MessengerStatus(
            messenger = messenger,
            isInstalled = isInstalled,
            installedPackageName = installedPkg,
            installedVariants = installedVariants
        )
    }

    fun getAllStatuses(context: Context): List<MessengerStatus> {
        return Messenger.entries.map { getMessengerStatus(context, it) }
    }

    fun buildDirectIntent(
        messenger: Messenger,
        phone: ParsedPhone,
        message: String = "",
        targetPackage: String? = null
    ): Intent {
        val encodedText = if (message.isNotBlank()) URLEncoder.encode(message.trim(), "UTF-8") else ""

        if (messenger == Messenger.SMS) {
            val uri = Uri.parse("smsto:${phone.internationalWithPlus}")
            return Intent(Intent.ACTION_SENDTO, uri).apply {
                if (message.isNotBlank()) {
                    putExtra("sms_body", message.trim())
                }
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }

        val (actionUri, pkg) = when (messenger) {
            Messenger.WHATSAPP -> {
                val phoneParam = phone.internationalNoPlus
                val uriStr = if (encodedText.isNotBlank()) {
                    "https://wa.me/$phoneParam?text=$encodedText"
                } else {
                    "https://wa.me/$phoneParam"
                }
                Uri.parse(uriStr) to (targetPackage ?: messenger.primaryPackage)
            }
            Messenger.TELEGRAM -> {
                val phoneParam = phone.internationalWithPlus
                val uriStr = "tg://resolve?phone=$phoneParam"
                Uri.parse(uriStr) to (targetPackage ?: messenger.primaryPackage)
            }
            Messenger.BALE -> {
                val phoneParam = phone.internationalNoPlus
                val uriStr = "https://ble.ir/$phoneParam"
                Uri.parse(uriStr) to (targetPackage ?: messenger.primaryPackage)
            }
            Messenger.SIGNAL -> {
                val phoneParam = phone.internationalWithPlus
                val uriStr = "https://signal.me/#p/$phoneParam"
                Uri.parse(uriStr) to (targetPackage ?: messenger.primaryPackage)
            }
            Messenger.SMS -> {
                Uri.parse("smsto:${phone.internationalWithPlus}") to null
            }
        }

        return Intent(Intent.ACTION_VIEW, actionUri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (!pkg.isNullOrBlank()) {
                setPackage(pkg)
            }
        }
    }

    fun buildWebFallbackIntent(messenger: Messenger, phone: ParsedPhone, message: String = ""): Intent {
        val encodedText = if (message.isNotBlank()) URLEncoder.encode(message.trim(), "UTF-8") else ""
        val url = when (messenger) {
            Messenger.WHATSAPP -> {
                if (encodedText.isNotBlank()) {
                    "https://wa.me/${phone.internationalNoPlus}?text=$encodedText"
                } else {
                    "https://wa.me/${phone.internationalNoPlus}"
                }
            }
            Messenger.TELEGRAM -> "https://t.me/${phone.internationalWithPlus}"
            Messenger.BALE -> "https://ble.ir/${phone.internationalNoPlus}"
            Messenger.SIGNAL -> "https://signal.me/#p/${phone.internationalWithPlus}"
            Messenger.SMS -> "smsto:${phone.internationalWithPlus}"
        }
        return Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    fun buildStoreIntent(packageName: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    fun buildDirectLinkUrl(messenger: Messenger, phone: ParsedPhone, message: String = ""): String {
        val encodedText = if (message.isNotBlank()) URLEncoder.encode(message.trim(), "UTF-8") else ""
        return when (messenger) {
            Messenger.WHATSAPP -> {
                if (encodedText.isNotBlank()) "https://wa.me/${phone.internationalNoPlus}?text=$encodedText"
                else "https://wa.me/${phone.internationalNoPlus}"
            }
            Messenger.TELEGRAM -> "https://t.me/${phone.internationalWithPlus}"
            Messenger.BALE -> "https://ble.ir/${phone.internationalNoPlus}"
            Messenger.SIGNAL -> "https://signal.me/#p/${phone.internationalWithPlus}"
            Messenger.SMS -> "smsto:${phone.internationalWithPlus}"
        }
    }
}