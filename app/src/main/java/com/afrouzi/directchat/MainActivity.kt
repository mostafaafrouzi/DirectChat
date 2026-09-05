package com.afrouzi.directchat

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.afrouzi.directchat.data.repository.SettingsRepository
import com.afrouzi.directchat.ui.screens.AboutScreen
import com.afrouzi.directchat.ui.screens.HomeScreen
import com.afrouzi.directchat.ui.theme.DirectChatTheme
import com.afrouzi.directchat.ui.viewmodel.DirectChatViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val viewModel: DirectChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val settingsRepository = SettingsRepository.getInstance(applicationContext)

        handleIncomingIntent(intent)

        setContent {
            val language by settingsRepository.languageFlow.collectAsState(initial = SettingsRepository.getDefaultSystemLanguage())
            val themePreference by settingsRepository.themeFlow.collectAsState(initial = "system")

            val isPersian = language == "fa"
            val layoutDirection = if (isPersian) LayoutDirection.Rtl else LayoutDirection.Ltr
            val coroutineScope = rememberCoroutineScope()

            val baseContext = LocalContext.current
            val localizedContext = remember(language, baseContext) {
                val locale = if (isPersian) Locale("fa") else Locale.ENGLISH
                Locale.setDefault(locale)
                val config = Configuration(baseContext.resources.configuration)
                config.setLocale(locale)
                config.setLayoutDirection(locale)
                baseContext.createConfigurationContext(config)
            }

            CompositionLocalProvider(
                LocalLayoutDirection provides layoutDirection,
                LocalContext provides localizedContext
            ) {
                DirectChatTheme(themePreference = themePreference, isPersian = isPersian) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        val uiState by viewModel.uiState.collectAsState()
                        var currentScreen by remember { mutableStateOf("home") }

                        BackHandler(enabled = currentScreen == "about") {
                            currentScreen = "home"
                        }

                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = {
                                if (targetState == "about") {
                                    (slideInHorizontally { width -> if (isPersian) -width else width } + fadeIn())
                                        .togetherWith(slideOutHorizontally { width -> if (isPersian) width else -width } + fadeOut())
                                } else {
                                    (slideInHorizontally { width -> if (isPersian) width else -width } + fadeIn())
                                        .togetherWith(slideOutHorizontally { width -> if (isPersian) -width else width } + fadeOut())
                                }
                            },
                            label = "ScreenTransition"
                        ) { screen ->
                            when (screen) {
                                "home" -> HomeScreen(
                                    uiState = uiState,
                                    viewModel = viewModel,
                                    isPersian = isPersian,
                                    themePreference = themePreference,
                                    onToggleTheme = {
                                        val nextTheme = when (themePreference) {
                                            "dark" -> "light"
                                            "light" -> "dark"
                                            else -> "dark"
                                        }
                                        coroutineScope.launch { settingsRepository.setTheme(nextTheme) }
                                    },
                                    onToggleLanguage = {
                                        val nextLang = if (isPersian) "en" else "fa"
                                        coroutineScope.launch { settingsRepository.setLanguage(nextLang) }
                                    },
                                    onNavigateToAbout = { currentScreen = "about" }
                                )
                                "about" -> AboutScreen(
                                    isPersian = isPersian,
                                    currentLanguage = language,
                                    currentTheme = themePreference,
                                    onLanguageChange = { lang ->
                                        coroutineScope.launch { settingsRepository.setLanguage(lang) }
                                    },
                                    onThemeChange = { theme ->
                                        coroutineScope.launch { settingsRepository.setTheme(theme) }
                                    },
                                    onBack = { currentScreen = "home" }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshStatuses(this)
        viewModel.checkClipboard(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIncomingIntent(intent)
    }

    private fun handleIncomingIntent(intent: Intent?) {
        if (intent == null) return
        if (intent.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (!sharedText.isNullOrBlank()) {
                viewModel.pastePhone(sharedText)
            }
        }
    }
}