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
import com.example.statussaveraim.activities.ConsentScreen
import com.example.statussaveraim.activities.SplashActivity
import com.example.statussaveraim.utils.MyApp
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.Date

class AppOpen_Ad(private val myApplication: MyApp) : LifecycleObserver,
    Application.ActivityLifecycleCallbacks {


    private val TAG = "StartAdLogs:"
    private var startOpenAd: AppOpenAd? = null
    private var callBackLoading: AppOpenAd.AppOpenAdLoadCallback? = null
    private var runningActivity: Activity? = null
    private var adLoadingTime: Long = 0

    companion object {
        private var isDisplayingAd = false
    }

    init {
        myApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun safeDismissDialog(dialog: Dialog?) {
        try {
            if (dialog != null && dialog.isShowing) {
                // Do not rely on 'runningActivity' here because it might temporarily be the AdActivity 
                // which is finishing when the ad is dismissed.
                val ownerActivity = dialog.context as? Activity
                if (ownerActivity != null && !ownerActivity.isFinishing && !ownerActivity.isDestroyed) {
                    dialog.dismiss()
                } else if (ownerActivity == null) {
                    dialog.dismiss()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error dismissing dialog: ${e.message}")
        }
    }

    fun showHoneyBeeMapNavigationStartAppOpenAd() {
        val billing = BillingHelper(myApplication)
        if (runningActivity != null && !runningActivity!!.isFinishing && !runningActivity!!.isDestroyed   && billing.shouldShowAds()) {
            try {
                val dialog = Dialog(runningActivity!!)

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
                    val fullScreenContentCallback: FullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                safeDismissDialog(dialog)
                                startOpenAd = null
                                isDisplayingAd = false
                                loadHoneyBeeMapNavigationStartAppOpenAd()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                safeDismissDialog(dialog)
                                startOpenAd = null
                                isDisplayingAd = false
                                loadHoneyBeeMapNavigationStartAppOpenAd()
                                Log.d(TAG, "AdError: ${adError.message}")
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Sometimes it's better to dismiss the dialog here to avoid it being visible underneath the ad,
                                // but dismissing it onAdDismissed is also fine.
                                Log.d(TAG, "showStartAppOpenAd: Start Showing")
                                isDisplayingAd = true
                            }
                        }

                    try {
                        if (!runningActivity!!.isFinishing && !runningActivity!!.isDestroyed) {
                            dialog.show()
                            Handler(Looper.getMainLooper()).postDelayed({

                                if (runningActivity != null && !runningActivity!!.isFinishing && !runningActivity!!.isDestroyed) {
                                    try {
                                        startOpenAd!!.show(runningActivity!!)
                                        Log.d(TAG, "showStartAppOpenAd: Showing App open ad")
                                        startOpenAd!!.fullScreenContentCallback =
                                            fullScreenContentCallback
                                    } catch (e: Exception) {
                                        Log.e(TAG, "Error showing ad: ${e.message}")
                                        safeDismissDialog(dialog)
                                    }
                                } else {
                                    safeDismissDialog(dialog)
                                }
                            }, 1000)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error showing dialog: ${e.message}")
                    }
                } else {
                    Log.d(TAG, "Can not show ad.")
                    if (LoadAds.should_show_app_open) {
                        loadHoneyBeeMapNavigationStartAppOpenAd()
                    }

                }
            } catch (e: Exception) {
            }
        }
    }

    fun loadHoneyBeeMapNavigationStartAppOpenAd() {

        val billing = BillingHelper(myApplication)
        if (isStartAdAvailable || !billing.shouldShowAds()) {
            Log.d(TAG, "Ad is available App Open Ad ")
        } else {
            Log.d(TAG, "Ad requesting App Open Ad ")
            callBackLoading = object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(p0: AppOpenAd) {
                    super.onAdLoaded(p0)
                    Log.d(TAG, "onAppOpenAdLoaded")
                    this@AppOpen_Ad.startOpenAd = p0
                    adLoadingTime = Date().time
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    Log.d(TAG, "onAppOpenAdFailedToLoad : Error : $p0")
                }
            }

            try {
                AppOpenAd.load(
                    myApplication,
                    myApplication.getString(R.string.AM_APPOPEN_AD_ID),
                    AdRequest.Builder().build(),
                    callBackLoading!!
                )
            } catch (e: Exception) {
            }
        }

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
        runningActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        runningActivity = activity
    }

    override fun onActivityDestroyed(activity: Activity) {
        runningActivity = null
    }

    fun clearAllOpenAd() {
        Log.d(TAG, "clearAllOpenAd: ")
        startOpenAd = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Log.d(TAG, "OnLifecycleEvent onStart")
        if (LoadAds.canShowAppOpen && runningActivity !is SplashActivity && runningActivity !is ConsentScreen /*&& runningActivity !is ConsentActivity*/) {
            Log.d(TAG, "onStart: Showing App open Resume AD")

            showHoneyBeeMapNavigationStartAppOpenAd()

        } else {
            loadHoneyBeeMapNavigationStartAppOpenAd()
        }
    }

}