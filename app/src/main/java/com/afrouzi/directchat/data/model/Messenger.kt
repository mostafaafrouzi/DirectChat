package com.afrouzi.directchat.data.model

import androidx.annotation.DrawableRes
import com.afrouzi.directchat.R

data class MessengerVariant(
    val id: String,
    val nameFa: String,
    val nameEn: String,
    val packageName: String,
    @DrawableRes val iconRes: Int,
    val isInstalled: Boolean = false
)

enum class Messenger(
    val id: String,
    val nameFa: String,
    val nameEn: String,
    val primaryPackage: String,
    val fallbackPackages: List<String>,
    val variants: List<MessengerVariant>,
    @DrawableRes val iconRes: Int,
    val brandColorHex: Long,
    val supportsPrefilledText: Boolean,
    val webFallbackUrl: String,
    val storePackageName: String
) {
    WHATSAPP(
        id = "whatsapp",
        nameFa = "واتساپ",
        nameEn = "WhatsApp",
        primaryPackage = "com.whatsapp",
        fallbackPackages = listOf("com.whatsapp.w4b"),
        variants = listOf(
            MessengerVariant(
                id = "whatsapp_regular",
                nameFa = "واتساپ معمولی",
                nameEn = "WhatsApp",
                packageName = "com.whatsapp",
                iconRes = R.drawable.ic_whatsapp
            ),
            MessengerVariant(
                id = "whatsapp_business",
                nameFa = "واتساپ بیزینس",
                nameEn = "WhatsApp Business",
                packageName = "com.whatsapp.w4b",
                iconRes = R.drawable.ic_whatsapp_business
            )
        ),
        iconRes = R.drawable.ic_whatsapp,
        brandColorHex = 0xFF25D366,
        supportsPrefilledText = true,
        webFallbackUrl = "https://wa.me/",
        storePackageName = "com.whatsapp"
    ),
    TELEGRAM(
        id = "telegram",
        nameFa = "تلگرام",
        nameEn = "Telegram",
        primaryPackage = "org.telegram.messenger",
        fallbackPackages = listOf(
            "org.telegram.messenger.web",
            "org.telegram.plus",
            "org.thunderdog.challegram",
            "tw.nekomimi.nekogram"
        ),
        variants = listOf(
            MessengerVariant(
                id = "telegram_official",
                nameFa = "تلگرام اصلی",
                nameEn = "Official Telegram",
                packageName = "org.telegram.messenger",
                iconRes = R.drawable.ic_telegram
            ),
            MessengerVariant(
                id = "telegram_direct",
                nameFa = "تلگرام مستقیم (Direct APK)",
                nameEn = "Telegram Direct",
                packageName = "org.telegram.messenger.web",
                iconRes = R.drawable.ic_telegram
            ),
            MessengerVariant(
                id = "telegram_plus",
                nameFa = "پلاس مسنجر (Plus)",
                nameEn = "Plus Messenger",
                packageName = "org.telegram.plus",
                iconRes = R.drawable.ic_telegram
            )
        ),
        iconRes = R.drawable.ic_telegram,
        brandColorHex = 0xFF229ED9,
        supportsPrefilledText = true,
        webFallbackUrl = "https://t.me/",
        storePackageName = "org.telegram.messenger"
    ),
    BALE(
        id = "bale",
        nameFa = "بله",
        nameEn = "Bale",
        primaryPackage = "ir.nasim",
        fallbackPackages = listOf("ir.ble.messenger"),
        variants = listOf(
            MessengerVariant(
                id = "bale",
                nameFa = "بله",
                nameEn = "Bale",
                packageName = "ir.nasim",
                iconRes = R.drawable.ic_bale
            )
        ),
        iconRes = R.drawable.ic_bale,
        brandColorHex = 0xFF00B894,
        supportsPrefilledText = true,
        webFallbackUrl = "https://ble.ir/",
        storePackageName = "ir.nasim"
    ),
    SIGNAL(
        id = "signal",
        nameFa = "سیگنال",
        nameEn = "Signal",
        primaryPackage = "org.thoughtcrime.securesms",
        fallbackPackages = emptyList(),
        variants = listOf(
            MessengerVariant(
                id = "signal",
                nameFa = "سیگنال",
                nameEn = "Signal",
                packageName = "org.thoughtcrime.securesms",
                iconRes = R.drawable.ic_signal
            )
        ),
        iconRes = R.drawable.ic_signal,
        brandColorHex = 0xFF3A76F0,
        supportsPrefilledText = true,
        webFallbackUrl = "https://signal.me/#p/",
        storePackageName = "org.thoughtcrime.securesms"
    ),
    VIBER(
        id = "viber",
        nameFa = "وایبر",
        nameEn = "Viber",
        primaryPackage = "com.viber.voip",
        fallbackPackages = emptyList(),
        variants = listOf(
            MessengerVariant(
                id = "viber",
                nameFa = "وایبر",
                nameEn = "Viber",
                packageName = "com.viber.voip",
                iconRes = R.drawable.ic_viber
            )
        ),
        iconRes = R.drawable.ic_viber,
        brandColorHex = 0xFF7360F2,
        supportsPrefilledText = true,
        webFallbackUrl = "https://viber.click/",
        storePackageName = "com.viber.voip"
    ),
    SMS(
        id = "sms",
        nameFa = "پیامک (SMS)",
        nameEn = "SMS / Messages",
        primaryPackage = "com.google.android.apps.messaging",
        fallbackPackages = listOf("com.android.mms", "com.samsung.android.messaging"),
        variants = listOf(
            MessengerVariant(
                id = "sms",
                nameFa = "پیامک",
                nameEn = "SMS",
                packageName = "com.google.android.apps.messaging",
                iconRes = R.drawable.ic_sms_message
            )
        ),
        iconRes = R.drawable.ic_sms_message,
        brandColorHex = 0xFF1976D2,
        supportsPrefilledText = true,
        webFallbackUrl = "",
        storePackageName = "com.google.android.apps.messaging"
    ),
    SKYPE(
        id = "skype",
        nameFa = "اسکایپ",
        nameEn = "Skype",
        primaryPackage = "com.skype.raider",
        fallbackPackages = listOf("com.skype.m2"),
        variants = listOf(
            MessengerVariant(
                id = "skype",
                nameFa = "اسکایپ",
                nameEn = "Skype",
                packageName = "com.skype.raider",
                iconRes = R.drawable.ic_skype
            )
        ),
        iconRes = R.drawable.ic_skype,
        brandColorHex = 0xFF0078D4,
        supportsPrefilledText = false,
        webFallbackUrl = "https://web.skype.com/",
        storePackageName = "com.skype.raider"
    ),
    IMO(
        id = "imo",
        nameFa = "ایمو",
        nameEn = "IMO",
        primaryPackage = "com.imo.android.imoim",
        fallbackPackages = listOf("com.imo.android.imolite"),
        variants = listOf(
            MessengerVariant(
                id = "imo",
                nameFa = "ایمو",
                nameEn = "IMO",
                packageName = "com.imo.android.imoim",
                iconRes = R.drawable.ic_imo
            )
        ),
        iconRes = R.drawable.ic_imo,
        brandColorHex = 0xFF00A3E0,
        supportsPrefilledText = false,
        webFallbackUrl = "https://imo.im/",
        storePackageName = "com.imo.android.imoim"
    );

    val allPackages: List<String>
        get() = (listOf(primaryPackage) + fallbackPackages + variants.map { it.packageName }).distinct()
}