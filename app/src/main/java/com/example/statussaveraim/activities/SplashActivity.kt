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
import com.example.statussaveraim.ads.AppOpenSplash
import com.example.statussaveraim.ads.BillingHelper
import com.example.statussaveraim.ads.LoadAds
import com.example.statussaveraim.ads.LoadAds.shouldShowAdmob
import com.example.statussaveraim.ads.ShowAds.showingDirectAd
import com.example.statussaveraim.databinding.ActivitySpalshBinding
import com.example.statussaveraim.utils.MyApp
import com.example.statussaveraim.utils.SharedPrefsHelper
import com.google.android.gms.ads.MobileAds
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.getValue

class SplashActivity : AppCompatActivity() {
    val binding: ActivitySpalshBinding by lazy {
        ActivitySpalshBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        simulateLoading()
        loadSplash(2)
        binding.btnContinue.setOnClickListener {
            showingDirectAd(this, {
                moveToMain()
            })
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
                    Thread.sleep(100) // Adjust speed here, 20ms * 100 = 2 seconds
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