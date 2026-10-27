package com.example.statussaveraim.models

data class LanguageModel(
    val name: String,
    val countryCode: String,
    var isSelected: Boolean = false
) {
    val flagUrl: String
        get() = "https://flagcdn.com/w160/$countryCode.png"
}