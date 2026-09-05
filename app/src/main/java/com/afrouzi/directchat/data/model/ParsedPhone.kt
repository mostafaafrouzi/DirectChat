package com.afrouzi.directchat.data.model

data class ParsedPhone(
    val rawInput: String,
    val cleanDigits: String,
    val isIranian: Boolean,
    val nationalFormat: String,
    val internationalWithPlus: String,
    val internationalNoPlus: String,
    val isValid: Boolean,
    val formattedDisplay: String
) {
    companion object {
        fun empty() = ParsedPhone(
            rawInput = "",
            cleanDigits = "",
            isIranian = false,
            nationalFormat = "",
            internationalWithPlus = "",
            internationalNoPlus = "",
            isValid = false,
            formattedDisplay = ""
        )
    }
}
