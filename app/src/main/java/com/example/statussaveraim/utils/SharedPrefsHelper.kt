package com.example.statussaveraim.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("StatusSaverPrefs", Context.MODE_PRIVATE)

    var hasGivenPermission: Boolean
        get() = prefs.getBoolean("has_permission", false)
        set(value) = prefs.edit().putBoolean("has_permission", value).apply()

    var isFirstTimeLaunch: Boolean
        get() = prefs.getBoolean("is_first_time", true)
        set(value) = prefs.edit().putBoolean("is_first_time", value).apply()

    var isWhatsappBusinessSelected: Boolean
        get() = prefs.getBoolean("is_wa_business", false)
        set(value) = prefs.edit().putBoolean("is_wa_business", value).apply()
}