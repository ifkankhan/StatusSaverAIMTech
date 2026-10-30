package com.example.statussaveraim.ads


import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.statussaveraim.R
import com.example.statussaveraim.utils.MyApp
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.Date

class AppOpenSplash(private val myApplication: MyApp) :
    LifecycleObserver,
    Application.ActivityLifecycleCallbacks {
    private val TAG = "Splashzzzz-Ad"

    private var callBackLoading: AppOpenAd.AppOpenAdLoadCallback? = null
    private var adLoadingTime: Long = 0

    companion object {
        public var startOpenAd: AppOpenAd? = null
        private var isDisplayingAd = false
    }

    init {
        myApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun showStreetViewTruckCTStartAppOpenAd(
        activity: Activity,
        callBack: SplashShowAppOpenCallback
    ) {
        // Check if activity is still valid
        if (activity.isFinishing || activity.isDestroyed) {
            callBack.onAdFailedToShowFullScreenContent()
            return
        }

        try {
            val dialog = Dialog(activity)

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.loading_dialog)
            dialog.getWindow()?.let { window ->
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            if (!isDisplayingAd && isStartAdAvailable) {
                val fullScreenContentCallback: FullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            try {
                                val ownerActivity = dialog.context as? Activity
                                if (ownerActivity != null && !ownerActivity.isFinishing && !ownerActivity.isDestroyed) {
                                    if (dialog.isShowing) {
                                        dialog.dismiss()
                                    }
                                } else if (ownerActivity == null) {
                                    if (dialog.isShowing) {
                                        dialog.dismiss()
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "Error dismissing dialog: ${e.message}")
                            }

                            startOpenAd = null
                            isDisplayingAd = false
                            callBack.onAdDismissedFullScreenContent()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            try {
                                val ownerActivity = dialog.context as? Activity
                                if (ownerActivity != null && !ownerActivity.isFinishing && !ownerActivity.isDestroyed) {
                                    if (dialog.isShowing) {
                                        dialog.dismiss()
                                    }
                                } else if (ownerActivity == null) {
                                    if (dialog.isShowing) {
                                        dialog.dismiss()
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "Error dismissing dialog: ${e.message}")
                            }
                            callBack.onAdFailedToShowFullScreenContent()
                        }

                        override fun onAdShowedFullScreenContent() {
                            isDisplayingAd = true
                        }
                    }
                try {
                    if (!activity.isFinishing && !activity.isDestroyed) {
                        dialog.show()
                        Handler(Looper.getMainLooper()).postDelayed({

                            if (!activity.isFinishing && !activity.isDestroyed) {
                                try {
                                    startOpenAd!!.show(activity)
                                    Log.d(TAG, "showStartAppOpenAd: Showing App open ad")
                                    startOpenAd!!.fullScreenContentCallback =
                                        fullScreenContentCallback
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error showing ad: ${e.message}")
                                    try {
                                        if (dialog.isShowing) {
                                            dialog.dismiss()
                                        }
                                    } catch (e2: Exception) {
                                        Log.e(
                                            TAG,
                                            "Error dismissing dialog after ad failure: ${e2.message}"
                                        )
                                    }
                                    callBack.onAdFailedToShowFullScreenContent()
                                }
                            } else {
                                try {
                                    if (dialog.isShowing) {
                                        dialog.dismiss()
                                    }
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error dismissing dialog: ${e.message}")
                                }
                            }
                        }, 1000)
                    }
                }
                catch (e: Exception) {
                    Log.e(TAG, "Error showing dialog: ${e.message}")
                    callBack.onAdFailedToShowFullScreenContent()
                }
            }
            else {
                Log.d(TAG, "Can not show ad.")
                callBack.onAdFailedToShowFullScreenContent()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in showStreetViewTruckCTStartAppOpenAd: ${e.message}")
            callBack.onAdFailedToShowFullScreenContent()
        }

    }

    fun loadStreetViewTruckCTStartAppOpenAd(callBack: SplashCallback) {
        if (isStartAdAvailable) {
            // callBack.onAdCallBack()
            Log.d(TAG, "Ad is available App Open Ad ")
        } else {
            Log.d(TAG, "Ad requesting App Open Ad ")
            callBackLoading = object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(p0: AppOpenAd) {
                    super.onAdLoaded(p0)
                    Log.d(TAG, "onAppOpenAdLoaded")
                    startOpenAd = p0
                    adLoadingTime = Date().time

                    callBack.onAdLoad()
                    // callBack.onAdCallBack()
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    Log.d(TAG, "onAppOpenAdFailedToLoad : Error : $p0")
                    Log.d(TAG, " onAppOpenAdFailedToLoad:  id ${LoadAds.app_open_splash_ad_id_admob}")
                    callBack.onAdFailed()
                    // callBack.onAdCallBack()
                }
            }

            Log.d(TAG, "loadStartAppOpenAd: Not Purchased id ${LoadAds.app_open_splash_ad_id_admob}")
            try {
                AppOpenAd.load(
                    myApplication,
                    LoadAds.app_open_splash_ad_id_admob,
                    AdRequest.Builder().build(),
                    callBackLoading!!
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error loading app open ad: ${e.message}")
                callBack.onAdFailed()
            }


        }
    }

    interface SplashCallback {
        fun onAdLoad()

        fun onAdFailed()
    }

    private fun callForLoadingTime(hours: Long): Boolean {
        val difference = Date().time - adLoadingTime
        val numMilliSecondsPerHour: Long = 3600000
        return difference < numMilliSecondsPerHour * hours
    }

    val isStartAdAvailable: Boolean
        get() = startOpenAd != null && callForLoadingTime(4)

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    fun clearAllOpenAd() {
        Log.d(TAG, "clearAllOpenAd: ")
        startOpenAd = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Log.d(TAG, "OnLifecycleEvent onStart")
        Log.d(TAG, "onStart: Showing App open Resume AD")
        //showStreetViewTruckCTStartAppOpenAd()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Log.d(TAG, "OnLifecycleEvent onStop")
    }

    interface SplashShowAppOpenCallback {
        fun onAdDismissedFullScreenContent()
        fun onAdFailedToShowFullScreenContent()
    }
}