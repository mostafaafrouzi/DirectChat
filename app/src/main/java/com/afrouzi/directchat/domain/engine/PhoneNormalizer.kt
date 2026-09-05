package com.afrouzi.directchat.domain.engine

import com.afrouzi.directchat.data.model.ParsedPhone

object PhoneNormalizer {

    private val PERSIAN_DIGITS = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
    private val ARABIC_DIGITS = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')

    fun toEnglishDigits(input: String): String {
        val sb = java.lang.StringBuilder(input.length)
        for (ch in input) {
            val pIdx = PERSIAN_DIGITS.indexOf(ch)
            if (pIdx >= 0) {
                sb.append(('0'.code + pIdx).toChar())
                continue
            }
            val aIdx = ARABIC_DIGITS.indexOf(ch)
            if (aIdx >= 0) {
                sb.append(('0'.code + aIdx).toChar())
                continue
            }
            sb.append(ch)
        }
        return sb.toString()
    }

    fun sanitize(input: String): String {
        val english = toEnglishDigits(input.trim())
        // Keep digits and leading +
        val hasLeadingPlus = english.startsWith("+")
        val digitsOnly = english.filter { it.isDigit() }
        return if (hasLeadingPlus) "+$digitsOnly" else digitsOnly
    }

    fun parse(input: String): ParsedPhone {
        val sanitized = sanitize(input)
        if (sanitized.isEmpty()) {
            return ParsedPhone.empty()
        }

        var digits = sanitized.removePrefix("+")
        // Handle double-zero international prefix e.g. 0098 -> 98
        if (digits.startsWith("00")) {
            digits = digits.substring(2)
        }

        val isIranianMobile: Boolean
        val national: String
        val intlWithPlus: String
        val intlNoPlus: String
        val isValid: Boolean
        val formatted: String

        when {
            // Domestic Iranian mobile starting with 09 (e.g. 09121234567, 11 digits)
            digits.startsWith("09") && digits.length == 11 -> {
                isIranianMobile = true
                national = digits
                val rest = digits.substring(1) // 9121234567
                intlNoPlus = "98$rest"
                intlWithPlus = "+$intlNoPlus"
                isValid = true
                formatted = "${digits.substring(0, 4)} ${digits.substring(4, 7)} ${digits.substring(7)}"
            }
            // Iranian mobile starting with 989 (e.g. 989121234567, 12 digits)
            digits.startsWith("989") && digits.length == 12 -> {
                isIranianMobile = true
                val rest = digits.substring(2) // 9121234567
                national = "0$rest"
                intlNoPlus = digits
                intlWithPlus = "+$intlNoPlus"
                isValid = true
                formatted = "${national.substring(0, 4)} ${national.substring(4, 7)} ${national.substring(7)}"
            }
            // Iranian mobile without leading 0 (e.g. 9121234567, 10 digits)
            digits.startsWith("9") && digits.length == 10 -> {
                isIranianMobile = true
                national = "0$digits"
                intlNoPlus = "98$digits"
                intlWithPlus = "+$intlNoPlus"
                isValid = true
                formatted = "${national.substring(0, 4)} ${national.substring(4, 7)} ${national.substring(7)}"
            }
            // Other numbers (international or landline)
            else -> {
                isIranianMobile = false
                national = if (digits.startsWith("0")) digits else "0$digits"
                intlNoPlus = digits
                intlWithPlus = "+$digits"
                isValid = digits.length in 7..15
                formatted = if (sanitized.startsWith("+")) "+$digits" else digits
            }
        }

        return ParsedPhone(
            rawInput = input,
            cleanDigits = digits,
            isIranian = isIranianMobile,
            nationalFormat = national,
            internationalWithPlus = intlWithPlus,
            internationalNoPlus = intlNoPlus,
            isValid = isValid,
            formattedDisplay = formatted
        )
    }

    fun extractPotentialPhone(text: String): String? {
        val sanitized = toEnglishDigits(text).trim()
        val regex = Regex("""(?:\+?98|0098|0)?9\d{9}""")
        val match = regex.find(sanitized.replace(Regex("""[\s\-\(\)\.]"""), ""))
        return match?.value
    }
}