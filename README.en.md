# DirectChat (چت‌مستقیم)

<div align="center">

![DirectChat Banner](https://img.shields.io/badge/DirectChat-Instant%20Chat%20Launcher-00A86B?style=for-the-badge&logo=android&logoColor=white)

[![Android CI](https://github.com/mostafaafrouzi/DirectChat/actions/workflows/release.yml/badge.svg)](https://github.com/mostafaafrouzi/DirectChat/actions)
[![Latest Release](https://img.shields.io/github/v/release/mostafaafrouzi/DirectChat?color=blue&label=Official%20Release)](https://github.com/mostafaafrouzi/DirectChat/releases/latest)
[![Platform](https://img.shields.io/badge/Platform-Android%208.0%2B%20(API%2026%2B)-brightgreen.svg)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4.svg)](https://developer.android.com/jetpack/compose)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**A native, ultra-fast and modern Android application to start direct conversations with any phone number across all popular messaging platforms without saving them to contacts.**

[مستندات فارسی](README.md) | [Download Official APK](https://github.com/mostafaafrouzi/DirectChat/releases/latest) | [Report Bug / Feedback](https://github.com/mostafaafrouzi/DirectChat/issues)

</div>

---

## 💡 The Problem & Solution

Whenever you need to send a quick message, inquire about a product price, or coordinate temporarily with a new phone number on WhatsApp, Telegram, or other messaging apps, you are forced to save the contact in your phone book first, wait for contact synchronization, find the conversation in the app, and remember to clean up unwanted numbers later.

**DirectChat** eliminates this friction with a single tap:
1. Enter or use the smart **Paste** button to insert the number.
2. Tap your messenger of choice.
3. Jump straight into the conversation with that person!

---

## ✨ Key Features

### 1. Instant Direct Chat Without Saving Contacts
- Send messages immediately without cluttering your address book with temporary or one-time contacts.
- Enhanced privacy: Your Status, profile picture, and Last Seen are never exposed to unknown parties.

### 2. Streamlined Support for 5 Working Messengers
A balanced, clean layout with 5 fully operational, reliable messaging channels:
- **WhatsApp:** Unified card for standard WhatsApp and WhatsApp Business using modern `wa.me` deep links.
- **Telegram:** Unified card supporting Google Play edition, official Direct APK, and Plus Messenger, directly launching 1-on-1 contact chats (`tg://resolve`).
- **Bale:** Popular banking & social messenger with official direct phone links (`ble.ir/98...`).
- **Signal:** The world's most secure encrypted messenger via official phone deep links (`signal.me/#p/+...`).
- **Default SMS / Messages:** Full-width dedicated system intent card available on 100% of Android devices even offline.

### 3. Unified Variant Chooser Dialog
- Instead of cluttering the UI with multiple cards, WhatsApp and Telegram versions are merged into single smart cards.
- When multiple variants are installed simultaneously (e.g. WhatsApp and WhatsApp Business), a dialog appears allowing you to select your preferred app. If only one is installed, it launches directly.

### 4. Smart Paste & Clear Buttons
- High-visibility **Paste** chip right inside the phone field for one-tap insertion of copied numbers.
- Automatically morphs into a **Clear** button once a number is entered.
- Automatic clipboard phone number detection upon app launch.

### 5. Optional Pre-filled Message Draft
- Prepare your greeting, inquiry, or note before jumping into the messaging app, automatically populating the chat input (supported by WhatsApp and SMS per platform protocols).

### 6. IRANSansX Eco & Expressive Typography
- Integrated Persian typography with **IRANSansX Eco** font family alongside standard modern Latin typography.
- Pixel-perfect bidirectional RTL (Right-to-Left) and LTR (Left-to-Right) layout mirroring.

### 7. Apple iOS-Style Segmented Controls
- Fluid animated Segmented Controls with haptic-friendly sliders for theme selection (Light, OLED Deep Dark, or System Auto).
- Instant language switching (English / Persian) with zero reload flicker.
- Quick toggle buttons in the Top App Bar for fast access.

### 8. Zero-Crash Smart Fallback
- If the selected messenger is not installed, the app never crashes; instead, it presents options to open the web version, view the official store download page (Google Play / CafeBazaar / Myket), or copy the direct chat link.

---

## 📸 Screenshots Gallery

<div align="center">

| Home (Dark Theme - Persian) | Home (Light Theme - English) | Variant Chooser Dialog |
| :---: | :---: | :---: |
| <img src="docs/screenshots/01_home_dark_fa.png" width="260"/> | <img src="docs/screenshots/02_home_light_en.png" width="260"/> | <img src="docs/screenshots/03_variant_dialog.png" width="260"/> |

| Theme & Language Settings | Active Input & Pre-filled Text | Centered App Launcher Icon |
| :---: | :---: | :---: |
| <img src="docs/screenshots/04_about_settings.png" width="260"/> | <img src="docs/screenshots/05_phone_input_active.png" width="260"/> | <img src="docs/screenshots/06_launcher_icon.png" width="260"/> |

</div>

---

## 🔒 Privacy & Security

- **100% Ad-Free:** Zero banners, interstitial ads, or commercial notifications.
- **No Trackers or Analytics:** No third-party SDKs, Firebase Analytics, or data collection.
- **Completely Offline:** Does not require the `android.permission.INTERNET` permission in its manifest; all operations are executed strictly via local Android OS intents.
- **Zero Data Retention:** Entered numbers are never saved to internal databases or external servers.

---

## 🛠️ Technical Specifications

<div dir="ltr">

| Specification | Value |
| :--- | :--- |
| **Application ID** | `com.afrouzi.directchat` |
| **Minimum SDK** | `26` (Android 8.0 Oreo) |
| **Target / Compile SDK** | `35` (Android 15) |
| **Kotlin Version** | `2.0.21` |
| **Jetpack Compose BOM** | `2024.10.01` (Material 3) |
| **Architecture** | Single-Activity MVVM + StateFlow + DataStore |
| **Release Version** | `1.0.0` |
| **Version Code** | `1` |
| **Signing Scheme** | RSA 4096-bit / APK Signature Scheme v2 |

</div>

---

## 📦 Building & Running Locally

To build and test the project in a local development environment:

```bash
# Clone the repository
git clone https://github.com/mostafaafrouzi/DirectChat.git
cd DirectChat

# Run unit tests
./gradlew test

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install directly to connected device or emulator via ADB
adb install -r app/build/outputs/apk/release/app-release.apk
```

---

## ⬇️ Download Latest Release

- **GitHub Releases:** [Download Official v1.0.0 APK and AAB Binaries](https://github.com/mostafaafrouzi/DirectChat/releases/latest)

---

## 👨‍💻 Developer & Author

Designed and engineered by **Mostafa Afrouzi**:

* 🌐 **Official Website:** [afrouzi.ir/en](https://afrouzi.ir/en/?utm_source=directchat&utm_medium=github_readme&utm_campaign=directchat)
* 🐙 **GitHub:** [github.com/mostafaafrouzi](https://github.com/mostafaafrouzi)
* 💼 **LinkedIn:** [linkedin.com/in/mostafaafrouzi](https://linkedin.com/in/mostafaafrouzi)
* 🛍️ **Apps on CafeBazaar:** [Developer Profile](https://cafebazaar.ir/developer/057657612999)
* 📱 **Apps on Myket:** [Developer Profile](https://myket.ir/developer/dev-102174)

---

## 📄 License

This project is open-source and released under the [MIT License](LICENSE).
