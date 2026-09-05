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
    EITAA(
        id = "eitaa",
        nameFa = "ایتا",
        nameEn = "Eitaa",
        primaryPackage = "ir.eitaa.messenger",
        fallbackPackages = listOf(
            "ir.eitaa.messenger.web",
            "ir.eitaa.messenger.direct",
            "ir.eitaa.messenger2",
            "ir.eitaa",
            "com.eitaa.messenger"
        ),
        variants = listOf(
            MessengerVariant(
                id = "eitaa",
                nameFa = "ایتا",
                nameEn = "Eitaa",
                packageName = "ir.eitaa.messenger",
                iconRes = R.drawable.ic_eitaa_official
            )
        ),
        iconRes = R.drawable.ic_eitaa_official,
        brandColorHex = 0xFFE67E22,
        supportsPrefilledText = true,
        webFallbackUrl = "https://eitaa.com/",
        storePackageName = "ir.eitaa.messenger"
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
    RUBIKA(
        id = "rubika",
        nameFa = "روبیکا",
        nameEn = "Rubika",
        primaryPackage = "app.rbmain.a",
        fallbackPackages = listOf("ir.resaneh1.iptv", "ir.rubika.chat"),
        variants = listOf(
            MessengerVariant(
                id = "rubika",
                nameFa = "روبیکا",
                nameEn = "Rubika",
                packageName = "app.rbmain.a",
                iconRes = R.drawable.ic_rubika_official
            )
        ),
        iconRes = R.drawable.ic_rubika_official,
        brandColorHex = 0xFF7952B3,
        supportsPrefilledText = true,
        webFallbackUrl = "https://rubika.ir/",
        storePackageName = "app.rbmain.a"
    ),
    SOROUSH_PLUS(
        id = "soroush_plus",
        nameFa = "سروش پلاس",
        nameEn = "Soroush Plus",
        primaryPackage = "mobi.mmdt.ottplus",
        fallbackPackages = listOf("mobi.mmdt.ott"),
        variants = listOf(
            MessengerVariant(
                id = "soroush_plus",
                nameFa = "سروش پلاس",
                nameEn = "Soroush Plus",
                packageName = "mobi.mmdt.ottplus",
                iconRes = R.drawable.ic_soroush_official
            )
        ),
        iconRes = R.drawable.ic_soroush_official,
        brandColorHex = 0xFF1583D7,
        supportsPrefilledText = true,
        webFallbackUrl = "https://splus.ir/",
        storePackageName = "mobi.mmdt.ottplus"
    ),
    IGAP(
        id = "igap",
        nameFa = "آی‌گپ",
        nameEn = "iGap",
        primaryPackage = "net.iGap",
        fallbackPackages = emptyList(),
        variants = listOf(
            MessengerVariant(
                id = "igap",
                nameFa = "آی‌گپ",
                nameEn = "iGap",
                packageName = "net.iGap",
                iconRes = R.drawable.ic_igap_official
            )
        ),
        iconRes = R.drawable.ic_igap_official,
        brandColorHex = 0xFF007AFF,
        supportsPrefilledText = true,
        webFallbackUrl = "https://igap.net/",
        storePackageName = "net.iGap"
    ),
    GAP(
        id = "gap",
        nameFa = "گپ",
        nameEn = "Gap",
        primaryPackage = "com.gapafzar.messenger",
        fallbackPackages = emptyList(),
        variants = listOf(
            MessengerVariant(
                id = "gap",
                nameFa = "گپ",
                nameEn = "Gap",
                packageName = "com.gapafzar.messenger",
                iconRes = R.drawable.ic_gap_official
            )
        ),
        iconRes = R.drawable.ic_gap_official,
        brandColorHex = 0xFF6A1B9A,
        supportsPrefilledText = true,
        webFallbackUrl = "https://gap.im/",
        storePackageName = "com.gapafzar.messenger"
    );

    val allPackages: List<String>
        get() = (listOf(primaryPackage) + fallbackPackages + variants.map { it.packageName }).distinct()
}