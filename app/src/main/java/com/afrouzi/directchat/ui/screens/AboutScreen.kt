package com.afrouzi.directchat.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.afrouzi.directchat.BuildConfig
import com.afrouzi.directchat.R
import com.afrouzi.directchat.ui.components.IosSegmentedControl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    isPersian: Boolean,
    currentLanguage: String,
    currentTheme: String,
    onLanguageChange: (String) -> Unit,
    onThemeChange: (String) -> Unit,
    onBack: () -> Unit,
    onOpenUrl: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    fun openUrl(url: String) {
        if (onOpenUrl != null) {
            onOpenUrl(url)
            return
        }
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val chooser = Intent.createChooser(
                    Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    },
                    null
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(chooser)
            } catch (e2: Exception) {
                android.util.Log.e("DirectChat", "Failed to open URL: $url", e2)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.about_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 18.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Icon & Hero
            Surface(
                modifier = Modifier.size(84.dp),
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                shadowElevation = 3.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_app_logo),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier.size(62.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = stringResource(R.string.version_label, BuildConfig.VERSION_NAME),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.app_tagline),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ==================== Appearance & Language Section (Inspired by android-dns-changer) ====================
            Text(
                text = stringResource(R.string.appearance_and_language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Language Header
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.language_setting_title),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Language Segmented Control
                    IosSegmentedControl(
                        items = listOf("fa", "en"),
                        selectedItem = currentLanguage,
                        onItemSelected = { onLanguageChange(it) },
                        itemLabel = {
                            if (it == "fa") "فارسی (IranSansX)" else "English (US)"
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(18.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    Spacer(modifier = Modifier.height(14.dp))

                    // Theme Header
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val themeIcon = when (currentTheme) {
                            "dark" -> Icons.Default.DarkMode
                            "light" -> Icons.Default.LightMode
                            else -> Icons.Default.BrightnessAuto
                        }
                        Icon(
                            imageVector = themeIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.theme_setting_title),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Theme Segmented Control
                    IosSegmentedControl(
                        items = listOf("system", "dark", "light"),
                        selectedItem = currentTheme,
                        onItemSelected = { onThemeChange(it) },
                        itemLabel = {
                            when (it) {
                                "dark" -> if (isPersian) "تاریک (Dark)" else "Dark"
                                "light" -> if (isPersian) "روشن (Light)" else "Light"
                                else -> if (isPersian) "سیستم (System)" else "System"
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==================== Developer Section ====================
            Text(
                text = if (isPersian) "توسعه‌دهنده" else "Developer",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isPersian) "مصطفی افروزی" else "Mostafa Afrouzi",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isPersian) "توسعه‌دهنده وب و موبایل • دیجیتال مارکتر" else "Web & Mobile Developer • Digital Marketer",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isPersian)
                            "من مصطفی افروزی هستم، توسعه‌دهنده اپلیکیشن‌های وب و موبایل با تمرکز بر روی معماری تمیز، سرعت و اتوماسیون مارکتینگ. هدفم ساخت محصولاتی با تجربه کاربری روان، بدون حاشیه و کارآمد برای نیازهای روزمره است."
                        else
                            "I'm Mostafa Afrouzi, a web & mobile application developer focused on SEO, Google Ads, and marketing automation. I build high-performance products with clean UX and rock-solid architecture.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Link 1: Official Website with language-specific UTM
                    DeveloperLinkRow(
                        drawableId = R.drawable.ic_website,
                        title = if (isPersian) "وب‌سایت رسمی (afrouzi.ir)" else "Official Website (afrouzi.ir/en)",
                        subtitle = if (isPersian) "مقالات، خدمات و پروژه‌ها" else "Articles, services & portfolio",
                        onClick = {
                            val url = if (isPersian)
                                "https://afrouzi.ir/?utm_source=directchat&utm_medium=about_screen&utm_campaign=directchat"
                            else
                                "https://afrouzi.ir/en/?utm_source=directchat&utm_medium=about_screen&utm_campaign=directchat"
                            openUrl(url)
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Link 2: GitHub Profile
                    DeveloperLinkRow(
                        drawableId = R.drawable.ic_github,
                        title = if (isPersian) "گیت‌هاب (GitHub)" else "GitHub Profile",
                        subtitle = "github.com/mostafaafrouzi",
                        onClick = { openUrl("https://github.com/mostafaafrouzi") }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Link 3: CafeBazaar Apps
                    DeveloperLinkRow(
                        drawableId = R.drawable.ic_cafebazaar,
                        title = if (isPersian) "اپلیکیشن‌های من در بازار" else "Other apps on CafeBazaar",
                        subtitle = "cafebazaar.ir/developer/057657612999",
                        onClick = { openUrl("https://cafebazaar.ir/developer/057657612999") }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Link 4: Myket Apps
                    DeveloperLinkRow(
                        drawableId = R.drawable.ic_myket,
                        title = if (isPersian) "اپلیکیشن‌های من در مایکت" else "Other apps on Myket",
                        subtitle = "myket.ir/developer/dev-102174",
                        onClick = { openUrl("https://myket.ir/developer/dev-102174") }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Link 5: LinkedIn Profile
                    DeveloperLinkRow(
                        drawableId = R.drawable.ic_linkedin,
                        title = if (isPersian) "لینکدین (LinkedIn)" else "LinkedIn Profile",
                        subtitle = "linkedin.com/in/mostafaafrouzi",
                        onClick = { openUrl("https://linkedin.com/in/mostafaafrouzi") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (isPersian)
                    "این پروژه به صورت کاملاً منبع‌باز منتشر شده و هیچ داده‌ای از شماره‌های ورودی یا پیام‌های شما را ذخیره یا به سرور ارسال نمی‌کند."
                else
                    "This project is fully open-source and privacy-friendly. None of your entered numbers or messages are stored or uploaded anywhere.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun DeveloperLinkRow(
    drawableId: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 6.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = drawableId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(16.dp)
        )
    }
}
