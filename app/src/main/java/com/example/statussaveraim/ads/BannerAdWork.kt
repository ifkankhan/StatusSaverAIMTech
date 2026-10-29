package com.example.statussaveraim.ads

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Keep
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.statussaveraim.R
import com.example.statussaveraim.ads.AdsConstant.bannerAdId
import com.example.statussaveraim.ads.AdsConstant.isPurchase
import com.example.statussaveraim.ads.LoadAds.shouldShowAdmob
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError


@Keep
object BannerAdWork {

    private var adView: AdView? = null
    private var adViewTopCollapsible: AdView? = null
    private var adViewBottomCollapsible: AdView? = null
    private var adViewRectangle: AdView? = null
    private var bannerAdLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    private var bannerAdLoading = false
    private var topCollapsibleLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    private var topCollapsibleLoading = false
    private var bottomCollapsibleLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    private var bottomCollapsibleLoading = false
    private var rectangleLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    private var rectangleLoading = false

    val TAG = "Tag_BannerAD"

    val bannerToNativeMap = hashMapOf(
        1 to 3,
        2 to 3,
        3 to 3,
        4 to 4,
        5 to 1
    )

    private fun showBanner(
        context: Context,
        frameLayout: FrameLayout?,
        adOnOff: Int,
        bannerAfterNativeFailure: Boolean,
        lifecycleOwner: LifecycleOwner,
        adId: String = ""
    ) {

        if (adOnOff == 0 || isPurchase) {

            return
        }

        when (adOnOff) {
            1 -> {

                if (bannerAdLoading)
                    return

                bannerAdLoading = true

                var adRequest: AdRequest?

                adView = AdView(context)
                val finalAdId = if (adId.isNotEmpty()) adId else if (bannerAdId.isEmpty()) context.getString(
                    R.string.AM_BANNER_AD_ID) else bannerAdId
                adView!!.adUnitId = finalAdId

                adRequest = AdRequest.Builder().build()
                adView!!.setAdSize(getAdSize(context))

                adView!!.adListener = object : AdListener() {

                    override fun onAdLoaded() {

                        Log.d(TAG, "onAdLoaded:  Ad ID App ${adView!!.adUnitId}")
                        bannerAdLoaded.postValue(true)
                        bannerAdLoading = false
                    }

                    override fun onAdImpression() {
                        bannerAdLoaded.postValue(false)
                        bannerAdLoading = false
                        Log.d(TAG, "onAdImpression: ")
                        adView = null
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        adView = null

                        bannerAdLoaded.postValue(false)
                        bannerAdLoading = false
                        Log.d(TAG, "onAdLoaded:  Ad ID App ${context.getString(R.string.AM_BANNER_AD_ID)}")
                        Log.d(TAG, "onAdImpression: ${p0.toString()}")
                        //  showBanner(context, frameLayout, adOnOff, false, lifecycleOwner)


                    }

                }

                try {
                    adView!!.loadAd(adRequest)
                } catch (e: Exception) {

                }

            }

            2 -> {
                if (topCollapsibleLoading)
                    return

                topCollapsibleLoading = true

                var adRequest: AdRequest?

//                if (AdsConstant.collapsibleTopON == 0)
//                    return

                adViewTopCollapsible = AdView(context)
                val finalAdId = if (adId.isNotEmpty()) adId else if (bannerAdId.isEmpty()) context.getString(R.string.AM_BANNER_AD_ID) else bannerAdId
                adViewTopCollapsible!!.adUnitId = finalAdId

                adViewTopCollapsible!!.setAdSize(getAdSize(context))

                val extras = Bundle()
                extras.putString("collapsible", "top")
                adRequest = AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter::class.java, extras).build()

                adViewTopCollapsible!!.adListener = object : AdListener() {

                    override fun onAdLoaded() {
                        Log.d(TAG, "onAdLoaded:  Ad ID App ${adViewTopCollapsible!!.adUnitId}")
                        topCollapsibleLoaded.postValue(true)
                        topCollapsibleLoading = false

                    }

                    override fun onAdImpression() {
                        Log.d(TAG, "onAdImpression: topCollapsible")
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        adViewTopCollapsible = null

                        topCollapsibleLoaded.postValue(false)
                        topCollapsibleLoading = false



                    }

                }

                try {
                    adViewTopCollapsible!!.loadAd(adRequest)
                } catch (e: Exception) {

                }


            }

            3 -> {
                if (bottomCollapsibleLoading)
                    return

                bottomCollapsibleLoading = true


                var adRequest: AdRequest?

//                if (AdsConstant.collapsibleBottomON == 0)
//                    return

                adViewBottomCollapsible = AdView(context)
                val finalAdId = if (adId.isNotEmpty()) adId else if (bannerAdId.isEmpty()) context.getString(R.string.AM_BANNER_AD_ID) else bannerAdId
                adViewBottomCollapsible!!.adUnitId = finalAdId
                Log.d(TAG, "Ad Id: $bannerAdId")
                adViewBottomCollapsible!!.setAdSize(getAdSize(context))
                val extras = Bundle()
                extras.putString("collapsible", "bottom")
                adRequest = AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter::class.java, extras).build()

                adViewBottomCollapsible!!.adListener = object : AdListener() {

                    override fun onAdLoaded() {
                        Log.d(
                            TAG,
                            "onAdLoaded:  Ad ID App adViewBottomCollapsible ${adViewBottomCollapsible!!.adUnitId}"
                        )
                        bottomCollapsibleLoaded.postValue(true)
                        bottomCollapsibleLoading = false
                    }

                    override fun onAdImpression() {
                        bottomCollapsibleLoaded.postValue(false)
                        bottomCollapsibleLoading = false

                        adViewBottomCollapsible = null
//                        loadBanner(context, frameLayout, adID, adOnOff)
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        adViewBottomCollapsible = null

                        bottomCollapsibleLoaded.postValue(false)
                        bottomCollapsibleLoading = false
                        Log.d(TAG, "onAdFailedToLoad:  Ad ID App  ${p0.message}")



                    }

                }

                try {
                    adViewBottomCollapsible!!.loadAd(adRequest)
                } catch (e: Exception) {

                }

            }

            4 -> {
                if (bannerAdLoading)
                    return

                bannerAdLoading = true

                var adRequest: AdRequest?

                adView = AdView(context)
                val finalAdId = if (adId.isNotEmpty()) adId else if (bannerAdId.isEmpty()) context.getString(R.string.AM_BANNER_AD_ID) else bannerAdId
                adView!!.adUnitId = finalAdId

                adRequest = AdRequest.Builder().build()
                adView!!.setAdSize(AdSize.LARGE_BANNER)

                adView!!.adListener = object : AdListener() {

                    override fun onAdLoaded() {

                        Log.d(TAG, "onAdLoaded:  Ad ID App ${adView!!.adUnitId}")
                        bannerAdLoaded.postValue(true)
                        bannerAdLoading = false
                    }

                    override fun onAdImpression() {
                        bannerAdLoaded.postValue(false)
                        bannerAdLoading = false

                        adView = null
//                        loadBanner(context, frameLayout, adID, adOnOff)
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        adView = null
                        Log.d(TAG, "onAdImpression: ${p0.toString()}")
                        bannerAdLoaded.postValue(false)
                        bannerAdLoading = false



                    }

                }

                try {
                    adView!!.loadAd(adRequest)
                } catch (e: Exception) {

                }


            }

            5 -> {

                if (rectangleLoading)
                    return

                rectangleLoading = true

                var adRequest: AdRequest?

                adViewRectangle = AdView(context)
                val finalAdId = if (adId.isNotEmpty()) adId else if (bannerAdId.isEmpty()) context.getString(R.string.AM_BANNER_AD_ID) else bannerAdId
                adViewRectangle!!.adUnitId = finalAdId

                adRequest = AdRequest.Builder().build()
                adViewRectangle!!.setAdSize(AdSize.MEDIUM_RECTANGLE)

                try {
                    adViewRectangle!!.loadAd(adRequest)
                } catch (e: Exception) {

                }

                adViewRectangle!!.adListener = object : AdListener() {

                    override fun onAdLoaded() {
                        Log.d(TAG, "onAdLoaded:  Ad ID App ${adViewRectangle!!.adUnitId}")
                        rectangleLoaded.postValue(true)
                        rectangleLoading = false
                    }

                    override fun onAdImpression() {
                        rectangleLoaded.postValue(false)
                        rectangleLoading = false

                        adViewRectangle = null
//                        loadBanner(context, frameLayout, adID, adOnOff)
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        adViewRectangle = null

                        rectangleLoaded.postValue(false)
                        rectangleLoading = false


                    }
                }
            }

            6 -> {

                if (bannerAdLoading)
                    return

                bannerAdLoading = true

                var adRequest: AdRequest?

                adView = AdView(context)
                val finalAdId = if (adId.isNotEmpty()) adId else if (bannerAdId.isEmpty()) context.getString(R.string.AM_BANNER_AD_ID) else bannerAdId
                adView!!.adUnitId = finalAdId

                adRequest = AdRequest.Builder().build()
                adView!!.setAdSize(getInlineAdaptiveBannerSize(context))

                adView!!.adListener = object : AdListener() {

                    override fun onAdLoaded() {

                        Log.d(TAG, "onAdLoaded: ")
                        bannerAdLoaded.postValue(true)
                        bannerAdLoading = false
                    }

                    override fun onAdImpression() {
                        bannerAdLoaded.postValue(false)
                        bannerAdLoading = false
                        Log.d(TAG, "onAdImpression: ")
                        adView = null
//                        loadBanner(context, frameLayout, adID, adOnOff)
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        adView = null

                        bannerAdLoaded.postValue(false)
                        bannerAdLoading = false
                        Log.d(TAG, "onAdImpression: ${p0.toString()}")



                    }

                }

                try {
                    adView!!.loadAd(adRequest)
                } catch (e: Exception) {

                }

            }

            7 -> {

                if (bannerAdLoading)
                    return

                bannerAdLoading = true

                var adRequest: AdRequest?

                adView = AdView(context)
                val finalAdId = if (adId.isNotEmpty()) adId else if (bannerAdId.isEmpty()) context.getString(R.string.AM_BANNER_AD_ID) else bannerAdId
                adView!!.adUnitId = finalAdId

                adRequest = AdRequest.Builder().build()
                adView!!.setAdSize(
                    getInlineAdaptiveBannerWithHeightad(
                        context,
                        AdsConstant.bannercustombannerheight
                    )
                )

                adView!!.adListener = object : AdListener() {

                    override fun onAdLoaded() {

                        Log.d(TAG, "onAdLoaded: ")
                        bannerAdLoaded.postValue(true)
                        bannerAdLoading = false
                    }

                    override fun onAdImpression() {
                        bannerAdLoaded.postValue(false)
                        bannerAdLoading = false
                        Log.d(TAG, "onAdImpression: ")
                        adView = null
//                        loadBanner(context, frameLayout, adID, adOnOff)
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        adView = null

                        bannerAdLoaded.postValue(false)
                        bannerAdLoading = false
                        Log.d(TAG, "onAdImpression: ${p0.toString()}")
                        


                    }

                }

                try {
                    adView!!.loadAd(adRequest)
                } catch (e: Exception) {

                }

            }
        }
    }

    fun loadFreshBanner(
        adOnOff: Int,
        context: Context,
        frameLayout: FrameLayout?,
        adId: String = ""
    ) {
        if (!shouldShowAdmob || isPurchase || frameLayout == null || adOnOff == 0) return
        frameLayout.visibility = View.VISIBLE

        try {
            val adView = AdView(context)
            val finalAdId = if (adId.isNotEmpty()) adId else if (bannerAdId.isEmpty()) context.getString(R.string.AM_BANNER_AD_ID) else bannerAdId
            adView.adUnitId = finalAdId

            var adRequest: AdRequest = AdRequest.Builder().build()

            when (adOnOff) {
                1 -> adView.setAdSize(getAdSize(context))
                2, 3 -> {
                    adView.setAdSize(getAdSize(context))
                    val extras = Bundle()
                    extras.putString("collapsible", if (adOnOff == 2) "top" else "bottom")
                    adRequest = AdRequest.Builder()
                        .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                        .build()
                }
                4 -> adView.setAdSize(AdSize.LARGE_BANNER)
                5 -> adView.setAdSize(AdSize.MEDIUM_RECTANGLE)
                6 -> adView.setAdSize(getInlineAdaptiveBannerSize(context))
                7 -> adView.setAdSize(getInlineAdaptiveBannerWithHeightad(context, AdsConstant.bannercustombannerheight))
                else -> adView.setAdSize(getAdSize(context))
            }

            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Log.d(TAG, "onAdLoaded: Fresh Banner")
                    frameLayout.removeAllViews()
                    frameLayout.addView(adView)
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    Log.d(TAG, "onAdFailedToLoad Fresh Banner: ${p0.message}")
                }
            }

            adView.loadAd(adRequest)
        } catch (e: Exception) {
            Log.e(TAG, "loadFreshBanner exception: ${e.message}")
        }
    }

    fun loadFreshCollapsibleBanner(
        context: Context,
        frameLayout: FrameLayout?,
        isTop: Boolean = false,
        adId: String = ""
    ) {
        if (!shouldShowAdmob || isPurchase || frameLayout == null) return
        frameLayout.visibility = View.VISIBLE

        try {
            val adView = AdView(context)
            val finalAdId = if (adId.isNotEmpty()) adId else if (bannerAdId.isEmpty()) context.getString(R.string.AM_BANNER_AD_ID) else bannerAdId
            adView.adUnitId = finalAdId
            adView.setAdSize(getAdSize(context))

            val extras = Bundle()
            extras.putString("collapsible", if (isTop) "top" else "bottom")
            val adRequest = AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                .build()

            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Log.d(TAG, "onAdLoaded: Fresh Collapsible Banner")
                    frameLayout.removeAllViews()
                    frameLayout.addView(adView)
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    Log.d(TAG, "onAdFailedToLoad Fresh Collapsible Banner: ${p0.message}")
                }
            }

            adView.loadAd(adRequest)
        } catch (e: Exception) {
            Log.e(TAG, "loadFreshCollapsibleBanner exception: ${e.message}")
        }
    }

    fun showAdmobBanner(
        adOnOff: Int,
        context: Context,
        frameLayout: FrameLayout?,
        bannerAfterNativeFailure: Boolean,
        lifecycleOwner: LifecycleOwner,
        adId: String = ""
    ) {
        if(!shouldShowAdmob){
            return
        }
        if (frameLayout == null) {
            return
        }

        if (adOnOff == 0 || isPurchase) {

            frameLayout?.visibility = View.GONE

            return
        }

        frameLayout?.visibility = View.VISIBLE

        try {
            when (adOnOff) {
                2 -> {

                    if (!topCollapsibleLoading && topCollapsibleLoaded.value == false) {

                        showBanner(
                            context,
                            frameLayout,
                            adOnOff,
                            bannerAfterNativeFailure,
                            lifecycleOwner,
                            adId
                        )

                        topCollapsibleLoaded.observe(lifecycleOwner, Observer<Boolean> { adLoaded ->
                            if (adLoaded) {
                                showAdmobBanner(
                                    adOnOff,
                                    context,
                                    frameLayout,
                                    bannerAfterNativeFailure,
                                    lifecycleOwner,
                                    adId
                                )
                                topCollapsibleLoaded.removeObservers(lifecycleOwner)
                            }
                        })

                        return
                    }

                    if (topCollapsibleLoaded.value == false) {

                        topCollapsibleLoaded.observe(lifecycleOwner, Observer<Boolean> { adLoaded ->
                            if (adLoaded) {
                                showAdmobBanner(
                                    adOnOff,
                                    context,
                                    frameLayout,
                                    bannerAfterNativeFailure,
                                    lifecycleOwner,
                                    adId
                                )
                                topCollapsibleLoaded.removeObservers(lifecycleOwner)
                            }
                        })

                        return
                    }

                    if (adViewTopCollapsible?.parent != null) {
                        (adViewTopCollapsible?.parent as? ViewGroup)?.removeView(
                            adViewTopCollapsible
                        )
                    }

                    frameLayout?.removeAllViews()
                    frameLayout?.addView(adViewTopCollapsible)

                }

                3 -> {

                    if (!bottomCollapsibleLoading && bottomCollapsibleLoaded.value == false) {

                        showBanner(
                            context,
                            frameLayout,
                            adOnOff,
                            bannerAfterNativeFailure,
                            lifecycleOwner,
                            adId
                        )

                        bottomCollapsibleLoaded.observe(
                            lifecycleOwner,
                            Observer<Boolean> { adLoaded ->
                                if (adLoaded) {
                                    showAdmobBanner(
                                        adOnOff,
                                        context,
                                        frameLayout,
                                        bannerAfterNativeFailure,
                                        lifecycleOwner
                                    )
                                    bottomCollapsibleLoaded.removeObservers(lifecycleOwner)
                                }
                            })

                        return
                    }

                    if (bottomCollapsibleLoaded.value == false) {

                        bottomCollapsibleLoaded.observe(
                            lifecycleOwner,
                            Observer<Boolean> { adLoaded ->
                                if (adLoaded) {
                                    showAdmobBanner(
                                        adOnOff,
                                        context,
                                        frameLayout,
                                        bannerAfterNativeFailure,
                                        lifecycleOwner
                                    )
                                    bottomCollapsibleLoaded.removeObservers(lifecycleOwner)
                                }
                            })

                        return
                    }

                    if (adViewBottomCollapsible?.parent != null) {
                        (adViewBottomCollapsible?.parent as? ViewGroup)?.removeView(
                            adViewBottomCollapsible
                        )
                    }

                    frameLayout?.removeAllViews()
                    frameLayout?.addView(adViewBottomCollapsible)
                }

                5 -> {

                    if (!rectangleLoading && rectangleLoaded.value == false) {

                        showBanner(
                            context,
                            frameLayout,
                            adOnOff,
                            bannerAfterNativeFailure,
                            lifecycleOwner,
                            adId
                        )

                        rectangleLoaded.observe(lifecycleOwner, Observer<Boolean> { adLoaded ->
                            if (adLoaded) {
                                showAdmobBanner(
                                    adOnOff,
                                    context,
                                    frameLayout,
                                    bannerAfterNativeFailure,
                                    lifecycleOwner,
                                    adId
                                )
                                rectangleLoaded.removeObservers(lifecycleOwner)
                            }
                        })

                        return
                    }

                    if (rectangleLoaded.value == false) {

                        rectangleLoaded.observe(lifecycleOwner, Observer<Boolean> { adLoaded ->
                            if (adLoaded) {
                                showAdmobBanner(
                                    adOnOff,
                                    context,
                                    frameLayout,
                                    bannerAfterNativeFailure,
                                    lifecycleOwner,
                                    adId
                                )
                                rectangleLoaded.removeObservers(lifecycleOwner)
                            }
                        })

                        return
                    }

                    if (adViewRectangle?.parent != null) {
                        (adViewRectangle?.parent as? ViewGroup)?.removeView(adViewRectangle)
                    }

                    frameLayout?.removeAllViews()
                    frameLayout?.addView(adViewRectangle)
                }

                else -> {
                    if (!bannerAdLoading && bannerAdLoaded.value == false) {

                        showBanner(
                            context,
                            frameLayout,
                            adOnOff,
                            bannerAfterNativeFailure,
                            lifecycleOwner,
                            adId
                        )

                        bannerAdLoaded.observe(lifecycleOwner, Observer<Boolean> { adLoaded ->
                            if (adLoaded) {
                                showAdmobBanner(
                                    adOnOff,
                                    context,
                                    frameLayout,
                                    bannerAfterNativeFailure,
                                    lifecycleOwner,
                                    adId
                                )
                                bannerAdLoaded.removeObservers(lifecycleOwner)
                            }
                        })

                        return
                    }

                    if (bannerAdLoaded.value == false) {

                        bannerAdLoaded.observe(lifecycleOwner, Observer<Boolean> { adLoaded ->
                            if (adLoaded) {
                                showAdmobBanner(
                                    adOnOff,
                                    context,
                                    frameLayout,
                                    bannerAfterNativeFailure,
                                    lifecycleOwner,
                                    adId
                                )
                                bannerAdLoaded.removeObservers(lifecycleOwner)
                            }
                        })

                        return
                    }

                    if (adView?.parent != null) {
                        (adView?.parent as? ViewGroup)?.removeView(adView)
                    }

                    frameLayout?.removeAllViews()
                    frameLayout?.addView(adView)
                }
            }
        } catch (e: Exception) {
//            Log.wtf("BANNER EXCEPTION", e.message + e.stackTraceToString())
        }

    }

    private fun showIntro(
        adId: String,
        context: Context,
        frameLayout: FrameLayout?,
        adOnOff: Int,
        bannerAfterNativeFailure: Boolean,
        lifecycleOwner: LifecycleOwner,
    ) {

        if (adOnOff == 0 || isPurchase) {

            return
        }

        when (adOnOff) {
            1 -> {

                if (bannerAdLoading)
                    return

                bannerAdLoading = true

                var adRequest: AdRequest?

                adView = AdView(context)
                adView!!.adUnitId = adId


                adRequest = AdRequest.Builder().build()
                adView!!.setAdSize(getAdSize(context))

                adView!!.adListener = object : AdListener() {

                    override fun onAdLoaded() {

                        Log.d(TAG, "onAdLoaded:  Ad ID Intro ${adView!!.adUnitId}")
                        bannerAdLoaded.postValue(true)
                        bannerAdLoading = false
                    }

                    override fun onAdImpression() {
                        bannerAdLoaded.postValue(false)
                        bannerAdLoading = false
                        Log.d(TAG, "onAdImpression: ")
                        adView = null
//                        loadBanner(context, frameLayout, adID, adOnOff)
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        adView = null

                        bannerAdLoaded.postValue(false)
                        bannerAdLoading = false
                        Log.d(TAG, "onAdImpression: ${p0.toString()}")
                       

                    }

                }

                try {
                    adView!!.loadAd(adRequest)
                } catch (e: Exception) {

                }

            }

            2 -> {
                if (topCollapsibleLoading)
                    return

                topCollapsibleLoading = true

                var adRequest: AdRequest?

//                if (AdsConstant.collapsibleTopON == 0)
//                    return

                adViewTopCollapsible = AdView(context)
                adViewTopCollapsible!!.adUnitId = adId


                adViewTopCollapsible!!.setAdSize(getAdSize(context))

                val extras = Bundle()
                extras.putString("collapsible", "top")
                adRequest = AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter::class.java, extras).build()

                adViewTopCollapsible!!.adListener = object : AdListener() {

                    override fun onAdLoaded() {
                        Log.d(TAG, "onAdLoaded:  Ad ID Intro ${adViewTopCollapsible!!.adUnitId}")
                        topCollapsibleLoaded.postValue(true)
                        topCollapsibleLoading = false

                    }

                    override fun onAdImpression() {
                        topCollapsibleLoaded.postValue(false)
                        topCollapsibleLoading = false

                        adViewTopCollapsible = null
//                        loadBanner(context, frameLayout, adID, adOnOff)
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        adViewTopCollapsible = null

                        topCollapsibleLoaded.postValue(false)
                        topCollapsibleLoading = false

                    }

                }

                try {
                    adViewTopCollapsible!!.loadAd(adRequest)
                } catch (e: Exception) {

                }


            }

            3 -> {
                if (bottomCollapsibleLoading)
                    return

                bottomCollapsibleLoading = true


                var adRequest: AdRequest?

//                if (AdsConstant.collapsibleBottomON == 0)
//                    return

                adViewBottomCollapsible = AdView(context)
                adViewBottomCollapsible!!.adUnitId = adId


                adViewBottomCollapsible!!.setAdSize(getAdSize(context))
                val extras = Bundle()
                extras.putString("collapsible", "bottom")
                adRequest = AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter::class.java, extras).build()

                adViewBottomCollapsible!!.adListener = object : AdListener() {

                    override fun onAdLoaded() {
                        Log.d(TAG, "onAdLoaded:  Ad ID Intro ${adViewBottomCollapsible!!.adUnitId}")
                        bottomCollapsibleLoaded.postValue(true)
                        bottomCollapsibleLoading = false
                    }

                    override fun onAdImpression() {
                        bottomCollapsibleLoaded.postValue(false)
                        bottomCollapsibleLoading = false

                        adViewBottomCollapsible = null
                        Log.d(TAG, "onAdImpression: ")
//                        loadBanner(context, frameLayout, adID, adOnOff)
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        adViewBottomCollapsible = null

                        bottomCollapsibleLoaded.postValue(false)
                        bottomCollapsibleLoading = false
                        Log.d(TAG, "onAdFailedToLoad: ${p0.message}")

                    }

                }

                try {
                    adViewBottomCollapsible!!.loadAd(adRequest)
                } catch (e: Exception) {

                }

            }

            4 -> {
                if (bannerAdLoading)
                    return

                bannerAdLoading = true

                var adRequest: AdRequest?

                adView = AdView(context)
                adView!!.adUnitId = adId


                adRequest = AdRequest.Builder().build()
                adView!!.setAdSize(AdSize.LARGE_BANNER)

                adView!!.adListener = object : AdListener() {

                    override fun onAdLoaded() {

                        Log.d(TAG, "onAdLoaded:  Ad ID Intro ${adView!!.adUnitId}")
                        bannerAdLoaded.postValue(true)
                        bannerAdLoading = false
                    }

                    override fun onAdImpression() {
                        bannerAdLoaded.postValue(false)
                        bannerAdLoading = false

                        adView = null
//                        loadBanner(context, frameLayout, adID, adOnOff)
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        adView = null
                        Log.d(TAG, "onAdImpression: ${p0.toString()}")
                        bannerAdLoaded.postValue(false)
                        bannerAdLoading = false

                    }

                }

                try {
                    adView!!.loadAd(adRequest)
                } catch (e: Exception) {

                }


            }

            5 -> {

                if (rectangleLoading)
                    return

                rectangleLoading = true

                var adRequest: AdRequest?

                adViewRectangle = AdView(context)
                adViewRectangle!!.adUnitId = adId


                adRequest = AdRequest.Builder().build()
                adViewRectangle!!.setAdSize(AdSize.MEDIUM_RECTANGLE)

                try {
                    adViewRectangle!!.loadAd(adRequest)
                } catch (e: Exception) {

                }

                adViewRectangle!!.adListener = object : AdListener() {

                    override fun onAdLoaded() {
                        Log.d(TAG, "onAdLoaded:  Ad ID Intro ${adView!!.adUnitId}")
                        rectangleLoaded.postValue(true)
                        rectangleLoading = false
                    }

                    override fun onAdImpression() {
                        rectangleLoaded.postValue(false)
                        rectangleLoading = false

                        adViewRectangle = null
//                        loadBanner(context, frameLayout, adID, adOnOff)
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        adViewRectangle = null

                        rectangleLoaded.postValue(false)
                        rectangleLoading = false
                    }
                }
            }

            6 -> {

                if (bannerAdLoading)
                    return

                bannerAdLoading = true

                var adRequest: AdRequest?

                adView = AdView(context)

                adView!!.adUnitId = adId


                adRequest = AdRequest.Builder().build()
                adView!!.setAdSize(getInlineAdaptiveBannerSize(context))

                adView!!.adListener = object : AdListener() {

                    override fun onAdLoaded() {

                        Log.d(TAG, "onAdLoaded: ")
                        bannerAdLoaded.postValue(true)
                        bannerAdLoading = false
                    }

                    override fun onAdImpression() {
                        bannerAdLoaded.postValue(false)
                        bannerAdLoading = false
                        Log.d(TAG, "onAdImpression: ")
                        adView = null
//                        loadBanner(context, frameLayout, adID, adOnOff)
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        adView = null

                        bannerAdLoaded.postValue(false)
                        bannerAdLoading = false
                        Log.d(TAG, "onAdImpression: ${p0.toString()}")

                    }

                }

                try {
                    adView!!.loadAd(adRequest)
                } catch (e: Exception) {

                }

            }

            7 -> {

                if (bannerAdLoading)
                    return

                bannerAdLoading = true

                var adRequest: AdRequest?

                adView = AdView(context)
                adView!!.adUnitId = adId


                adRequest = AdRequest.Builder().build()
                adView!!.setAdSize(
                    getInlineAdaptiveBannerWithHeightad(
                        context,
                        AdsConstant.bannercustombannerheight
                    )
                )

                adView!!.adListener = object : AdListener() {

                    override fun onAdLoaded() {

                        Log.d(TAG, "onAdLoaded: ")
                        bannerAdLoaded.postValue(true)
                        bannerAdLoading = false
                    }

                    override fun onAdImpression() {
                        bannerAdLoaded.postValue(false)
                        bannerAdLoading = false
                        Log.d(TAG, "onAdImpression: ")
                        adView = null
//                        loadBanner(context, frameLayout, adID, adOnOff)
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        adView = null

                        bannerAdLoaded.postValue(false)
                        bannerAdLoading = false
                        Log.d(TAG, "onAdImpression: ${p0.toString()}")

                    }

                }

                try {
                    adView!!.loadAd(adRequest)
                } catch (e: Exception) {

                }

            }
        }
    }

    fun showIntroBanner(
        adOnOff: Int,
        adId: String,
        context: Context,
        frameLayout: FrameLayout?,
        bannerAfterNativeFailure: Boolean,
        lifecycleOwner: LifecycleOwner,
    ) {
        if (frameLayout == null) {
            return
        }

        if (adOnOff == 0 || isPurchase) {

            frameLayout?.visibility = View.GONE

            return
        }

        frameLayout?.visibility = View.VISIBLE

        try {
            when (adOnOff) {
                2 -> {

                    if (!topCollapsibleLoading && topCollapsibleLoaded.value == false) {

                        showIntro(
                            adId,
                            context,
                            frameLayout,
                            adOnOff,
                            bannerAfterNativeFailure,
                            lifecycleOwner
                        )

                        topCollapsibleLoaded.observe(lifecycleOwner, Observer<Boolean> { adLoaded ->
                            if (adLoaded) {
                                showIntroBanner(
                                    adOnOff,
                                    adId,
                                    context,
                                    frameLayout,
                                    bannerAfterNativeFailure,
                                    lifecycleOwner
                                )
                                topCollapsibleLoaded.removeObservers(lifecycleOwner)
                            }
                        })

                        return
                    }

                    if (topCollapsibleLoaded.value == false) {

                        topCollapsibleLoaded.observe(lifecycleOwner, Observer<Boolean> { adLoaded ->
                            if (adLoaded) {
                                showIntroBanner(
                                    adOnOff,
                                    adId,
                                    context,
                                    frameLayout,
                                    bannerAfterNativeFailure,
                                    lifecycleOwner
                                )
                                topCollapsibleLoaded.removeObservers(lifecycleOwner)
                            }
                        })

                        return
                    }

                    if (adViewTopCollapsible?.parent != null) {
                        (adViewTopCollapsible?.parent as? ViewGroup)?.removeView(
                            adViewTopCollapsible
                        )
                    }

                    frameLayout?.removeAllViews()
                    frameLayout?.addView(adViewTopCollapsible)

                }

                3 -> {

                    if (!bottomCollapsibleLoading && bottomCollapsibleLoaded.value == false) {

                        showIntro(
                            adId,
                            context,
                            frameLayout,
                            adOnOff,
                            bannerAfterNativeFailure,
                            lifecycleOwner
                        )

                        bottomCollapsibleLoaded.observe(
                            lifecycleOwner,
                            Observer<Boolean> { adLoaded ->
                                if (adLoaded) {

                                    showIntroBanner(
                                        adOnOff,
                                        adId,
                                        context,
                                        frameLayout,
                                        bannerAfterNativeFailure,
                                        lifecycleOwner
                                    )
                                    bottomCollapsibleLoaded.removeObservers(lifecycleOwner)
                                }
                            })

                        return
                    }

                    if (bottomCollapsibleLoaded.value == false) {

                        bottomCollapsibleLoaded.observe(
                            lifecycleOwner,
                            Observer<Boolean> { adLoaded ->
                                if (adLoaded) {
                                    showIntroBanner(
                                        adOnOff,
                                        adId,
                                        context,
                                        frameLayout,
                                        bannerAfterNativeFailure,
                                        lifecycleOwner
                                    )
                                    bottomCollapsibleLoaded.removeObservers(lifecycleOwner)
                                }
                            })

                        return
                    }

                    if (adViewBottomCollapsible?.parent != null) {
                        (adViewBottomCollapsible?.parent as? ViewGroup)?.removeView(
                            adViewBottomCollapsible
                        )
                    }

                    frameLayout?.removeAllViews()
                    frameLayout?.addView(adViewBottomCollapsible)
                }

                5 -> {

                    if (!rectangleLoading && rectangleLoaded.value == false) {

                        showIntro(
                            adId,
                            context,
                            frameLayout,
                            adOnOff,
                            bannerAfterNativeFailure,
                            lifecycleOwner
                        )

                        rectangleLoaded.observe(lifecycleOwner, Observer<Boolean> { adLoaded ->
                            if (adLoaded) {
                                showIntroBanner(
                                    adOnOff,
                                    adId,
                                    context,
                                    frameLayout,
                                    bannerAfterNativeFailure,
                                    lifecycleOwner
                                )
                                rectangleLoaded.removeObservers(lifecycleOwner)
                            }
                        })

                        return
                    }

                    if (rectangleLoaded.value == false) {

                        rectangleLoaded.observe(lifecycleOwner, Observer<Boolean> { adLoaded ->
                            if (adLoaded) {
                                showIntroBanner(
                                    adOnOff,
                                    adId,
                                    context,
                                    frameLayout,
                                    bannerAfterNativeFailure,
                                    lifecycleOwner
                                )
                                rectangleLoaded.removeObservers(lifecycleOwner)
                            }
                        })

                        return
                    }

                    if (adViewRectangle?.parent != null) {
                        (adViewRectangle?.parent as? ViewGroup)?.removeView(adViewRectangle)
                    }

                    frameLayout?.removeAllViews()
                    frameLayout?.addView(adViewRectangle)
                }

                else -> {
                    if (!bannerAdLoading && bannerAdLoaded.value == false) {

                        showIntro(
                            adId,
                            context,
                            frameLayout,
                            adOnOff,
                            bannerAfterNativeFailure,
                            lifecycleOwner
                        )

                        bannerAdLoaded.observe(lifecycleOwner, Observer<Boolean> { adLoaded ->
                            if (adLoaded) {
                                showIntroBanner(
                                    adOnOff,
                                    adId,
                                    context,
                                    frameLayout,
                                    bannerAfterNativeFailure,
                                    lifecycleOwner
                                )
                                bannerAdLoaded.removeObservers(lifecycleOwner)
                            }
                        })

                        return
                    }

                    if (bannerAdLoaded.value == false) {

                        bannerAdLoaded.observe(lifecycleOwner, Observer<Boolean> { adLoaded ->
                            if (adLoaded) {
                                showIntroBanner(
                                    adOnOff,
                                    adId,
                                    context,
                                    frameLayout,
                                    bannerAfterNativeFailure,
                                    lifecycleOwner
                                )
                                bannerAdLoaded.removeObservers(lifecycleOwner)
                            }
                        })

                        return
                    }

                    if (adView?.parent != null) {
                        (adView?.parent as? ViewGroup)?.removeView(adView)
                    }

                    frameLayout?.removeAllViews()
                    frameLayout?.addView(adView)
                }
            }
        } catch (e: Exception) {
//            Log.wtf("BANNER EXCEPTION", e.message + e.stackTraceToString())
        }

    }

    private fun getAdSize(context: Context): AdSize {

        val displayMetrics = context.resources.displayMetrics
        val adWidth = (displayMetrics.widthPixels / displayMetrics.density).toInt()

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
    }

    private fun getInlineAdaptiveBannerSize(context: Context): AdSize {
        val outMetrics = context.resources.displayMetrics
        val adWidthPixels = outMetrics.widthPixels
        val adWidth = (adWidthPixels / outMetrics.density).toInt()
        return AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(context, adWidth)
    }

    private fun getInlineAdaptiveBannerWithHeightad(context: Context, heightDp: Int): AdSize {
        val displayMetrics = context.resources.displayMetrics
        val adWidth = (displayMetrics.widthPixels / displayMetrics.density).toInt()

        val clampedHeight = heightDp.coerceIn(50, 120) // AdMob guideline ke mutabiq
        return AdSize.getInlineAdaptiveBannerAdSize(adWidth, clampedHeight)
    }
}
