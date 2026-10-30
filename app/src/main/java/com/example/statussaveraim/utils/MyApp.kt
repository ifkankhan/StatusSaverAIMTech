package com.example.statussaveraim.utils

import android.app.Application
import android.util.Log
import com.example.statussaveraim.ads.AppOpenSplash
import com.example.statussaveraim.ads.AppOpen_Ad

class MyApp : Application() {

    companion object {

        var myApp: MyApp? = null

        var appOpenSplash: AppOpenSplash? = null
        var appOpen: AppOpen_Ad? = null
        fun getStr(id: Int): String {
            return try {
                myApp?.getString(id) ?: run {
                    // Fallback: try to get string from application context if available
                    try {
                        myApp?.applicationContext?.getString(id) ?: "ad_unit_id_placeholder"
                    } catch (e: Exception) {
                        Log.e(
                            "TAG_AppOpen",
                            "Error getting string from application context: ${e.message}"
                        )
                        "ad_unit_id_placeholder"
                    }
                }
            } catch (e: Exception) {
                Log.e("TAG_AppOpen", "Error getting string resource: ${e.message}")
                "ad_unit_id_placeholder"
            }
        }
    }


    override fun onCreate() {
        super.onCreate()

        myApp = this


        appOpenSplash = AppOpenSplash(this)
        appOpen = AppOpen_Ad(this)


    }
}
