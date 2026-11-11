package com.example.statussaveraim.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.statussaveraim.R
import com.example.statussaveraim.ads.AdsConstant
import com.example.statussaveraim.ads.AdsConstant.SplashPreferenceAd
import com.example.statussaveraim.ads.AppOpenSplash
import com.example.statussaveraim.ads.BillingHelper
import com.example.statussaveraim.ads.LoadAds
import com.example.statussaveraim.ads.LoadAds.shouldShowAdmob
import com.example.statussaveraim.ads.ShowAds.showingDirectAd
import com.example.statussaveraim.databinding.ActivitySpalshBinding
import com.example.statussaveraim.utils.MyApp
import com.example.statussaveraim.utils.SharedPrefsHelper
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import org.json.JSONObject
import kotlin.Boolean
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.getValue

class SplashActivity : AppCompatActivity() {
    val binding: ActivitySpalshBinding by lazy {
        ActivitySpalshBinding.inflate(layoutInflater)
    }

    val mFirebaseRemoteConfig: FirebaseRemoteConfig by lazy { FirebaseRemoteConfig.getInstance() }
    var jsonObjectMainAds = JSONObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        simulateLoading()
        getFromRemoteConfig()
        binding.btnContinue.setOnClickListener {
            showingDirectAd(this, {
                moveToMain()
            })
        }
    }

    fun getFromRemoteConfig() {
        if (true) {
            val configBuilder =
                FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0)
            val cacheInterval: Long = 0
            configBuilder.minimumFetchIntervalInSeconds = cacheInterval
            mFirebaseRemoteConfig.setConfigSettingsAsync(configBuilder.build())
            mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task: Task<Boolean?> ->
                if (task.isSuccessful) {
                    try {
                        AdsConstant.MainAdsControl = mFirebaseRemoteConfig.getString("ads")
                        jsonObjectMainAds = JSONObject(AdsConstant.MainAdsControl!!)


                        LoadAds.next_ads_time =
                            jsonObjectMainAds.getLong("timedurationtoshowinterstital")
                        LoadAds.backpressadcontrol =
                            jsonObjectMainAds.getBoolean("backpressadcontrol")
                        LoadAds.IsAppOnTimer =
                            jsonObjectMainAds.getBoolean("IsAppOnTimer")
                        AdsConstant.willAllAdsNotShow =
                            jsonObjectMainAds.getBoolean("willAllAdsNotShow")
                        LoadAds.adShowAfter =
                            jsonObjectMainAds.getInt("interstitialAdsToShowCount")

                        AdsConstant.languagebottombanner =
                            jsonObjectMainAds.getInt("langbottombanner")
                        AdsConstant.languagebottomnative =
                            jsonObjectMainAds.getInt("langbottomnative")

                        AdsConstant.intro1bottombanner =
                            jsonObjectMainAds.getInt("intro1bottombanner")
                        AdsConstant.intro1bottomnative =
                            jsonObjectMainAds.getInt("intro1bottomnative")

                        AdsConstant.intro2bottombanner =
                            jsonObjectMainAds.getInt("intro2bottombanner")
                        AdsConstant.intro2bottomnative =
                            jsonObjectMainAds.getInt("intro2bottomnative")


                        AdsConstant.intro3bottombanner =
                            jsonObjectMainAds.getInt("intro3bottombanner")
                        AdsConstant.intro3bottombanner =
                            jsonObjectMainAds.getInt("intro3bottomnative")

                        AdsConstant.permbottombanner =
                            jsonObjectMainAds.getInt("permbottombanner")
                        AdsConstant.permbottomnative =
                            jsonObjectMainAds.getInt("permbottomnative")

                        AdsConstant.homebottombanner =
                            jsonObjectMainAds.getInt("homebottombanner")
                        AdsConstant.homebottomnative =
                            jsonObjectMainAds.getInt("homebottomnative")

                        AdsConstant.exitbottombanner =
                            jsonObjectMainAds.getInt("exitbottombanner")
                        AdsConstant.exitbottomnative =
                            jsonObjectMainAds.getInt("exitbottomnative")


                        AdsConstant.settingbottombanner =
                            jsonObjectMainAds.getInt("settingbottombanner")
                        AdsConstant.settingbottomnative =
                            jsonObjectMainAds.getInt("settingbottomnative")

                        AdsConstant.bannerborderwidth =
                            jsonObjectMainAds.getInt("bannerborderwidth")
                        AdsConstant.bannercardradius =
                            jsonObjectMainAds.getInt("bannercardradius")
                        AdsConstant.nativeborderwidth =
                            jsonObjectMainAds.getInt("nativeborderwidth")
                        AdsConstant.nativecardradius =
                            jsonObjectMainAds.getInt("nativecardradius")

                        AdsConstant.bannerboardercolor =
                            jsonObjectMainAds.getString("bannerboardercolor")
                        AdsConstant.nativebordercolor =
                            jsonObjectMainAds.getString("nativebordercolor")
                        AdsConstant.nativecardbgcolor =
                            jsonObjectMainAds.getString("nativecardbgcolor")
                        AdsConstant.nativeheadlinecolor =
                            jsonObjectMainAds.getString("nativeheadlinecolor")
                        AdsConstant.nativedescriptioncolor =
                            jsonObjectMainAds.getString("nativedescriptioncolor")

                        AdsConstant.nativeAdicontextcolor =
                            jsonObjectMainAds.getString("nativeAdicontextcolor")
                        AdsConstant.nativeAdiconbgclr =
                            jsonObjectMainAds.getString("nativeAdiconbgclr")
                        AdsConstant.nativebuttoncolor =
                            jsonObjectMainAds.getString("nativebuttoncolor")
                        AdsConstant.nativebuttontextcolor =
                            jsonObjectMainAds.getString("nativebuttontextcolor")

                        AdsConstant.interstitialAdId =
                            jsonObjectMainAds.getString("interstitialAdId")
                        AdsConstant.nativeAdId =
                            jsonObjectMainAds.getString("nativeAdId")
                        AdsConstant.appOpenAdId =
                            jsonObjectMainAds.getString("appOpenAdId")
                        AdsConstant.rewardedInterstitialAdId =
                            jsonObjectMainAds.getString("rewardedInterstitialAdId")
                        AdsConstant.RewarededvideoId =
                            jsonObjectMainAds.getString("RewarededvideoId")

                        AdsConstant.interstitialAdsToShowCount =
                            jsonObjectMainAds.getInt("interstitialAdsToShowCount")

                        AdsConstant.SplashPreferenceAd =
                            jsonObjectMainAds.getInt("SplashPreferenceAd")
                        loadSplash(jsonObjectMainAds.getInt("SplashPreferenceAd"))
                        Log.e(
                            "remoteConfig", "onComplete:  ${AdsConstant.MainAdsControl}"
                        )
                    } catch (e: Exception) {
                        Log.e(
                            "remoteConfig", "Exception 1:  ${e.localizedMessage}"
                        )
                        loadSplash(SplashPreferenceAd)
                    }
                }else{
                    loadSplash(SplashPreferenceAd)

                }
            }
        } else {
            Log.e(
                "remoteConfig", "Else :  "
            )
            loadSplash(SplashPreferenceAd)
        }
    }

    private fun simulateLoading() {
        binding.progressBar.progress = 0
        binding.progressBar.visibility = View.VISIBLE
        binding.btnContinue.visibility = View.GONE

        val handler = Handler(Looper.getMainLooper())
        var progressStatus = 0

        Thread {
            while (progressStatus < 100) {
                progressStatus += 1
                handler.post {
                    binding.progressBar.progress = progressStatus
                }
                try {
                    Thread.sleep(120) // Adjust speed here, 20ms * 100 = 2 seconds
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            handler.post {
                binding.progressBar.visibility = View.GONE
                binding.btnContinue.visibility = View.VISIBLE
            }
        }.start()
    }

    fun loadSplash(value: Int) {

        try {
            MobileAds.initialize(this) { initializationStatus ->

                initializationStatus.adapterStatusMap.forEach { (adapter, status) ->

                    Log.d(
                        "AdMediation",
                        "Adapter: $adapter"
                    )

                    Log.d(
                        "AdMediation",
                        "Status: ${status.initializationState}"
                    )

                    Log.d(
                        "AdMediation",
                        "Description: ${status.description}"
                    )
                }
            }

        } catch (e: Exception) {
        }
        val billing = BillingHelper(this)
        if (billing.shouldShowAds()) {
        if (value == 2) {
            LoadAds.loadDirectAd(
                this,
                LoadAds.interstitial_Splash,
                object : LoadAds.OnVideoLoad {
                    override fun onLoaded() {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.btnContinue.visibility = View.VISIBLE
                    }

                    override fun onFailed() {

                        binding.progressBar.visibility = View.INVISIBLE
                        binding.btnContinue.visibility = View.VISIBLE
                    }

                    override fun onPurchased() {

                        binding.progressBar.visibility = View.INVISIBLE
                        binding.btnContinue.visibility = View.VISIBLE
                    }
                })
        }
        else {
            val splashAd = MyApp.appOpenSplash
            if (shouldShowAdmob && splashAd != null) {
                splashAd.loadStreetViewTruckCTStartAppOpenAd(
                    object :
                        AppOpenSplash.SplashCallback {
                        override fun onAdLoad() {

                            binding.progressBar.visibility = View.INVISIBLE
                            showingAdmobSplash()
                        }

                        override fun onAdFailed() {

                            moveToMain()
                            binding.progressBar.visibility = View.INVISIBLE

                        }

                    })

            } else {
                moveToMain()
            }
        }
        } else {
            moveToMain()
        }
    }

    private fun moveToMain() {
        val sharedPrefs = SharedPrefsHelper(this)
        if (sharedPrefs.isFirstTimeLaunch) {
            val intent = Intent(this, LanguageActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        finish()
    }

    fun showingAdmobSplash() {
        val splashAd = MyApp.appOpenSplash
        if (splashAd != null && splashAd.isStartAdAvailable) {
            splashAd.showStreetViewTruckCTStartAppOpenAd(
                this,
                object :
                    AppOpenSplash.SplashShowAppOpenCallback {
                    override fun onAdDismissedFullScreenContent() {
                        moveToMain()
                    }

                    override fun onAdFailedToShowFullScreenContent() {
                        moveToMain()
                    }

                })
        } else {
            moveToMain()
        }
    }

}