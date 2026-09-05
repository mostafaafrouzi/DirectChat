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

        val (actionUri, pkg) = when (messenger) {
            Messenger.WHATSAPP -> {
                val phoneParam = phone.internationalNoPlus
                val uriStr = if (encodedText.isNotBlank()) {
                    "https://api.whatsapp.com/send?phone=$phoneParam&text=$encodedText"
                } else {
                    "https://api.whatsapp.com/send?phone=$phoneParam"
                }
                Uri.parse(uriStr) to (targetPackage ?: messenger.primaryPackage)
            }
            Messenger.TELEGRAM -> {
                val phoneParam = phone.internationalWithPlus
                val uriStr = if (encodedText.isNotBlank()) {
                    "tg://msg?to=$phoneParam&text=$encodedText"
                } else {
                    "tg://resolve?phone=$phoneParam"
                }
                Uri.parse(uriStr) to (targetPackage ?: messenger.primaryPackage)
            }
            Messenger.EITAA -> {
                val phoneParam = phone.internationalNoPlus
                val uriStr = "https://eitaa.com/$phoneParam"
                Uri.parse(uriStr) to (targetPackage ?: messenger.primaryPackage)
            }
            Messenger.BALE -> {
                val phoneParam = if (phone.isIranian) phone.nationalFormat else phone.internationalWithPlus
                val uriStr = "bale://chat?uid=$phoneParam"
                Uri.parse(uriStr) to (targetPackage ?: messenger.primaryPackage)
            }
            Messenger.RUBIKA -> {
                val phoneParam = if (phone.isIranian) phone.nationalFormat else phone.internationalWithPlus
                val uriStr = "rubika://open?phone=$phoneParam"
                Uri.parse(uriStr) to (targetPackage ?: messenger.primaryPackage)
            }
            Messenger.SOROUSH_PLUS -> {
                val phoneParam = if (phone.isIranian) phone.nationalFormat else phone.internationalWithPlus
                val uriStr = "soroush://resolve?phone=$phoneParam"
                Uri.parse(uriStr) to (targetPackage ?: messenger.primaryPackage)
            }
            Messenger.IGAP -> {
                val phoneParam = if (phone.isIranian) phone.nationalFormat else phone.internationalWithPlus
                val uriStr = "igap://resolve?phone=$phoneParam"
                Uri.parse(uriStr) to (targetPackage ?: messenger.primaryPackage)
            }
            Messenger.GAP -> {
                val phoneParam = if (phone.isIranian) phone.nationalFormat else phone.internationalWithPlus
                val uriStr = "gap://resolve?phone=$phoneParam"
                Uri.parse(uriStr) to (targetPackage ?: messenger.primaryPackage)
            }
        }

        return Intent(Intent.ACTION_VIEW, actionUri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            setPackage(pkg)
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
            Messenger.TELEGRAM -> {
                "https://t.me/${phone.internationalWithPlus}"
            }
            Messenger.EITAA -> "https://eitaa.com/"
            Messenger.BALE -> "https://ble.ir/"
            Messenger.RUBIKA -> "https://rubika.ir/"
            Messenger.SOROUSH_PLUS -> "https://splus.ir/"
            Messenger.IGAP -> "https://igap.net/"
            Messenger.GAP -> "https://gap.im/"
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
            Messenger.EITAA -> "https://eitaa.com/"
            Messenger.BALE -> "https://ble.ir/"
            Messenger.RUBIKA -> "https://rubika.ir/"
            Messenger.SOROUSH_PLUS -> "https://splus.ir/"
            Messenger.IGAP -> "https://igap.net/"
            Messenger.GAP -> "https://gap.im/"
        }
    }
}