package com.example.statussaveraim.ads


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Keep
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.statussaveraim.ads.AdsConstant.isPurchase
import com.example.statussaveraim.R
import com.example.statussaveraim.ads.AdsConstant.isPurchase
import com.example.statussaveraim.ads.AdsConstant.nativeAdId
import com.example.statussaveraim.ads.AdsConstant.nativecardradius
import com.example.statussaveraim.databinding.NativeShimmerStyleBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

@Keep
object AdMob_Native {

    var nativeAd: NativeAd? = null
    private var nativeLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    private var nativeisLoading = false

    val TAG = "NativeAD"
    val nativeToBannerMap = hashMapOf(
        1 to 5,
        2 to 5,
        3 to 1,
        4 to 4,
    )

    fun showNativeAdmob(
        visible: Int,
        adcontainer: FrameLayout,
        requireActivity: Context,
        bannerafternativefailure: Boolean,
        lifecycleOwner: LifecycleOwner,
        adId: String = ""
    ) {


        if (visible == 0 || isPurchase) {

            adcontainer.visibility = View.GONE
            return

        } else {

            adcontainer.visibility = View.VISIBLE




            if (!nativeisLoading && nativeLoaded.value == false) {

                loadNative(
                    visible,
                    adcontainer,
                    requireActivity,
                    bannerafternativefailure,
                    lifecycleOwner,
                    adId
                )
                nativeLoaded.observe(
                    lifecycleOwner,
                    Observer<Boolean> { nativeAdLoaded ->
                        if (nativeAdLoaded) {


                            showNativeAdmob(
                                visible,
                                adcontainer,
                                requireActivity, bannerafternativefailure, lifecycleOwner, adId

                            )
                            nativeLoaded.removeObservers(lifecycleOwner)
                        }
                    })

                return
            }



            if (nativeLoaded.value == false) {

                loadNative(
                    visible,
                    adcontainer,
                    requireActivity,
                    bannerafternativefailure,
                    lifecycleOwner,
                    adId
                )
                nativeLoaded.observe(
                    lifecycleOwner,
                    Observer<Boolean> { nativeAdLoaded ->
                        if (nativeAdLoaded) {

                            showNativeAdmob(
                                visible,
                                adcontainer,
                                requireActivity, bannerafternativefailure, lifecycleOwner, adId

                            )
                            nativeLoaded.removeObservers(lifecycleOwner)
                        }
                    })

                return
            }


            try {


                if (nativeAd != null) {

                    var adView: NativeAdView? = null
                    if (visible == 1) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.layout_native_ad_design,
                            null
                        ) as NativeAdView
                    } else if (visible == 2) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.layout_native_ad_design_top_button,
                            null
                        ) as NativeAdView


                    } else if (visible == 3) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.layout_native_nomedia,
                            null
                        ) as NativeAdView
                    } else if (visible == 4) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.layout_native_ad_design_med,
                            null
                        ) as NativeAdView
                    } else if (visible == 5) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.nomedia_videoaddlayout,
                            null
                        ) as NativeAdView
                    } else if (visible == 6) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.bignativelayoutnew,
                            null
                        ) as NativeAdView
                    } else if (visible == 7) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.native_7,
                            null
                        ) as NativeAdView
                    } else if (visible == 8) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.native8,
                            null
                        ) as NativeAdView
                    } else if (visible == 9) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.nativelayout_9,
                            null
                        ) as NativeAdView
                    }

                    if (adView != null) {
                        nativeAd?.let { populateNativeAd(it, adView, requireActivity, adcontainer) }
                    }

                    adcontainer.removeAllViews()
                    adcontainer.addView(adView)


                    Log.d(TAG, "displayNativeAd: if ")
                } else {
                    loadNative(
                        visible,
                        adcontainer,
                        requireActivity,
                        bannerafternativefailure,
                        lifecycleOwner
                    )
                }


            } catch (e: Exception) {

            }


        }


    }


    fun loadNative(
        visible: Int,
        adcontainer: FrameLayout?,
        requireActivity: Context,
        bannerafternativefailure: Boolean,
        lifecycleOwner: LifecycleOwner,
        adId: String = ""
    ) {


        if (isPurchase) {


            return

        }


        if (nativeisLoading) {
            return
        }
        nativeisLoading = true




        val finalAdId = if (adId.isNotEmpty()) adId else if (nativeAdId.isEmpty()) requireActivity.getString(R.string.AM_NATIVE_AD_ID) else nativeAdId

        Log.d(TAG, "loadAdNative : id " + finalAdId)

        try {

            var builder: AdLoader.Builder? = null
            builder = AdLoader.Builder(requireActivity, finalAdId)

            builder.forNativeAd(NativeAd.OnNativeAdLoadedListener { nativeAdd: NativeAd ->


                if (nativeAd != null) {
                    nativeAd!!.destroy()
                }
                nativeAd = nativeAdd


            })
            val videoOptions = VideoOptions.Builder()
                .setStartMuted(true)
                .build()
            val adOptions = NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build()
            builder.withNativeAdOptions(adOptions)
            val adLoader = builder.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.d(TAG, "onAdFailedToLoad: ${loadAdError.message}")
                    nativeAd = null
                    nativeLoaded.postValue(false)
                    nativeisLoading = false
                    Log.d(TAG, bannerafternativefailure.toString())

                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    nativeLoaded.postValue(true)
                    nativeisLoading = false
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    nativeAd = null
                    nativeLoaded.postValue(false)
                    nativeisLoading = false

                    loadNative(
                        visible,
                        null,
                        requireActivity,
                        bannerafternativefailure,
                        lifecycleOwner,
                        adId
                    )
                }
            }).build()
            adLoader.loadAd(AdRequest.Builder().build())
        } catch (e: Exception) {

        }


    }

    fun showIntroNativeAdmob(
        visible: Int,
        adcontainer: FrameLayout,
        requireActivity: Context,
        bannerafternativefailure: Boolean,
        lifecycleOwner: LifecycleOwner,
        adId: String = ""
    ) {


        if (visible == 0 || isPurchase) {

            adcontainer.visibility = View.GONE
            return

        } else {

            adcontainer.visibility = View.VISIBLE




            if (!nativeisLoading && nativeLoaded.value == false) {

                introNative(
                    visible,
                    adcontainer,
                    requireActivity,
                    bannerafternativefailure,
                    lifecycleOwner,
                    adId
                )
                nativeLoaded.observe(
                    lifecycleOwner,
                    Observer<Boolean> { nativeAdLoaded ->
                        if (nativeAdLoaded) {


                            showIntroNativeAdmob(
                                visible,
                                adcontainer,
                                requireActivity, bannerafternativefailure, lifecycleOwner, adId

                            )
                            nativeLoaded.removeObservers(lifecycleOwner)
                        }
                    })

                return
            }



            if (nativeLoaded.value == false) {

                introNative(
                    visible,
                    adcontainer,
                    requireActivity,
                    bannerafternativefailure,
                    lifecycleOwner,
                    adId
                )
                nativeLoaded.observe(
                    lifecycleOwner,
                    Observer<Boolean> { nativeAdLoaded ->
                        if (nativeAdLoaded) {

                            showIntroNativeAdmob(
                                visible,
                                adcontainer,
                                requireActivity, bannerafternativefailure, lifecycleOwner, adId

                            )
                            nativeLoaded.removeObservers(lifecycleOwner)
                        }
                    })

                return
            }


            try {


                if (nativeAd != null) {

                    var adView: NativeAdView? = null
                    if (visible == 1) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.layout_native_ad_design,
                            null
                        ) as NativeAdView
                    } else if (visible == 2) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.layout_native_ad_design_top_button,
                            null
                        ) as NativeAdView


                    } else if (visible == 3) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.layout_native_nomedia,
                            null
                        ) as NativeAdView
                    } else if (visible == 4) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.layout_native_ad_design_med,
                            null
                        ) as NativeAdView
                    } else if (visible == 5) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.nomedia_videoaddlayout,
                            null
                        ) as NativeAdView
                    } else if (visible == 6) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.bignativelayoutnew,
                            null
                        ) as NativeAdView
                    } else if (visible == 7) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.native_7,
                            null
                        ) as NativeAdView
                    } else if (visible == 8) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.native8,
                            null
                        ) as NativeAdView
                    } else if (visible == 9) {
                        adView = LayoutInflater.from(requireActivity).inflate(
                            R.layout.nativelayout_9,
                            null
                        ) as NativeAdView
                    }

                    if (adView != null) {
                        nativeAd?.let { populateNativeAd(it, adView, requireActivity, adcontainer) }
                    }

                    adcontainer.removeAllViews()
                    adcontainer.addView(adView)


                    Log.d(TAG, "Intro displayNativeAd: if ")
                } else {
                    introNative(
                        visible,
                        adcontainer,
                        requireActivity,
                        bannerafternativefailure,
                        lifecycleOwner
                    )
                }


            } catch (e: Exception) {

            }
        }
    }

    fun introNative(
        visible: Int,
        adcontainer: FrameLayout?,
        requireActivity: Context,
        bannerafternativefailure: Boolean,
        lifecycleOwner: LifecycleOwner,
        adId: String = ""
    ) {
        if (isPurchase) {
            return
        }
        if (nativeisLoading) {
            return
        }
        nativeisLoading = true
        val finalAdId = if (adId.isNotEmpty()) adId else requireActivity.getString(R.string.AM_NATIVE_INTRO)

        Log.d(TAG, " Intro loadAdNative : id " + finalAdId)
        try {
            var builder: AdLoader.Builder? = null
            builder = AdLoader.Builder(requireActivity, finalAdId)
            builder.forNativeAd(NativeAd.OnNativeAdLoadedListener { nativeAdd: NativeAd ->


                if (nativeAd != null) {
                    nativeAd!!.destroy()
                }
                nativeAd = nativeAdd


            })
            val videoOptions = VideoOptions.Builder()
                .setStartMuted(true)
                .build()
            val adOptions = NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build()
            builder.withNativeAdOptions(adOptions)
            val adLoader = builder.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.d(TAG, " Intro onAdFailedToLoad: ${loadAdError.message}")
                    nativeAd = null
                    nativeLoaded.postValue(false)
                    nativeisLoading = false
                    Log.d(TAG, bannerafternativefailure.toString())

                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Log.d(TAG, " Intro onAdLoaded: ")
                    nativeLoaded.postValue(true)
                    nativeisLoading = false
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    nativeAd = null
                    nativeLoaded.postValue(false)
                    nativeisLoading = false
                }
            }).build()
            adLoader.loadAd(AdRequest.Builder().build())
        } catch (e: Exception) {

        }


    }

    fun loadFreshNative(
        visible: Int,
        adcontainer: FrameLayout,
        requireActivity: Context,
        adId: String = ""
    ) {
        if (visible == 0 || isPurchase) {
            adcontainer.visibility = View.GONE
            return
        }

        adcontainer.visibility = View.VISIBLE

        val finalAdId = if (adId.isNotEmpty()) adId else requireActivity.getString(R.string.AM_NATIVE_INTRO)

        Log.d(TAG, " Fresh loadAdNative : id $finalAdId")
        try {
            val builder = AdLoader.Builder(requireActivity, finalAdId)
            builder.forNativeAd { nativeAdd: NativeAd ->
                var adView: NativeAdView? = null
                when (visible) {
                    1 -> adView = LayoutInflater.from(requireActivity).inflate(R.layout.layout_native_ad_design, null) as NativeAdView
                    2 -> adView = LayoutInflater.from(requireActivity).inflate(R.layout.layout_native_ad_design_top_button, null) as NativeAdView
                    3 -> adView = LayoutInflater.from(requireActivity).inflate(R.layout.layout_native_nomedia, null) as NativeAdView
                    4 -> adView = LayoutInflater.from(requireActivity).inflate(R.layout.layout_native_ad_design_med, null) as NativeAdView
                    5 -> adView = LayoutInflater.from(requireActivity).inflate(R.layout.nomedia_videoaddlayout, null) as NativeAdView
                    6 -> adView = LayoutInflater.from(requireActivity).inflate(R.layout.bignativelayoutnew, null) as NativeAdView
                    7 -> adView = LayoutInflater.from(requireActivity).inflate(R.layout.native_7, null) as NativeAdView
                    8 -> adView = LayoutInflater.from(requireActivity).inflate(R.layout.native8, null) as NativeAdView
                    9 -> adView = LayoutInflater.from(requireActivity).inflate(R.layout.nativelayout_9, null) as NativeAdView
                    else -> adView = LayoutInflater.from(requireActivity).inflate(R.layout.layout_native_ad_design, null) as NativeAdView
                }

                if (adView != null) {
                    populateNativeAd(nativeAdd, adView, requireActivity, adcontainer)
                    adcontainer.removeAllViews()
                    adcontainer.addView(adView)
                }
            }

            val videoOptions = VideoOptions.Builder().setStartMuted(true).build()
            val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()
            builder.withNativeAdOptions(adOptions)

            val adLoader = builder.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.d(TAG, " Fresh onAdFailedToLoad: ${loadAdError.message}")
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Log.d(TAG, " Fresh onAdLoaded: ")
                }
            }).build()
            
            adLoader.loadAd(AdRequest.Builder().build())
        } catch (e: Exception) {
            Log.d(TAG, "Fresh Native exception: $e")
        }
    }

    private fun populateNativeAd(
        nativeAd: NativeAd,
        adView: NativeAdView,
        requireActivity: Context,
        adcontainer: FrameLayout,
    ) {

        try {

///setting colors from remote
            val nativecardview = adView.findViewById<CardView>(R.id.nativecardview)

            // Inside your populateNativeAd function, replace the border setting code with:
            try {
                val borderColor = Color.parseColor(AdsConstant.nativebordercolor)
                val borderWidth = AdsConstant.nativeborderwidth

                // Create a GradientDrawable with the border
                val shape = GradientDrawable()
                shape.shape = GradientDrawable.RECTANGLE
                shape.cornerRadius = nativecardradius.toFloat()
                shape.setColor(Color.parseColor(AdsConstant.nativecardbgcolor))
                shape.setStroke(borderWidth, borderColor)

                // Apply the drawable as background
                nativecardview.background = shape
            } catch (e: Exception) {
                Log.d("bordercolor", "populateNativeAd: $e")
            }

            val mediaView: MediaView = adView.findViewById(R.id.ad_media)
            adView.mediaView = mediaView

            adView.headlineView = adView.findViewById(R.id.ad_headline)

            try {
                val headlinecolor = Color.parseColor(AdsConstant.nativeheadlinecolor)
                adView.findViewById<TextView>(R.id.ad_headline).setTextColor(headlinecolor)
            } catch (e: Exception) {

            }


            val adtv = adView.findViewById<TextView>(R.id.adtexttv)
            try {
                val adtextcolor = Color.parseColor(AdsConstant.nativeAdtextcolor)
                val adtextbgcolor = Color.parseColor(AdsConstant.nativeAdtextbgclr)
                adtv.setTextColor(adtextcolor)
                adtv.setBackgroundColor(adtextbgcolor)
                //       adtv.background.setTint(adtextbgcolor)

            } catch (e: Exception) {

            }



            adView.bodyView = adView.findViewById(R.id.ad_body)

            try {
                val bodycolor = Color.parseColor(AdsConstant.nativelowertxtcolor)

                adView.findViewById<TextView>(R.id.ad_body).setTextColor(bodycolor)
            } catch (e: Exception) {
                Log.d("lowertxt", "populateNativeAd: $e")
            }


            adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
            try {
                val btntextcolor = Color.parseColor(AdsConstant.nativebuttontextcolor)
                adView.findViewById<AppCompatButton>(R.id.ad_call_to_action)
                    .setTextColor(btntextcolor)
            } catch (e: Exception) {

            }
            try {
//                          val drawable3 = AppCompatResources.getDrawable(requireActivity, R.drawable.ad_btn)
//           drawable3?.setTint(Color.parseColor(AdsConstant.nativebuttoncolor))
                adView.findViewById<AppCompatButton>(R.id.ad_call_to_action).setBackgroundColor(
                    Color.parseColor(
                        AdsConstant.nativebuttoncolor
                    )
                )
            } catch (e: Exception) {
                Log.d("actionbuttonissue", "populateNativeAd: $e ")
            }

            adView.iconView = adView.findViewById(R.id.ad_app_icon)

            (adView.headlineView as TextView?)?.text = nativeAd.headline


            if (nativeAd.body == null) {
                adView.bodyView?.visibility = View.GONE
            } else {
                adView.bodyView?.visibility = View.VISIBLE
                (adView.bodyView as TextView?)?.text = nativeAd.body
            }
            if (nativeAd.callToAction == null) {
                adView.callToActionView?.visibility = View.VISIBLE
            } else {
                adView.callToActionView?.visibility = View.VISIBLE
                (adView.callToActionView as Button?)?.text = nativeAd.callToAction
            }
            if (nativeAd.icon == null) {
                adView.iconView?.visibility = View.GONE
            } else {
                (adView.iconView as ImageView?)?.setImageDrawable(nativeAd.icon?.drawable)
                adView.iconView?.visibility = View.VISIBLE
            }
            adView.setNativeAd(nativeAd)
        } catch (e: Exception) {
            Log.d("myTag", "id  exception $e")
        }
    }

    fun View.visible() {
        visibility = View.VISIBLE
    }

    fun View.gone() {
        visibility = View.GONE
    }

    fun View.invisible() {
        visibility = View.INVISIBLE
    }

    fun showNativeShimmer(shimmerLayout: NativeShimmerStyleBinding, mainScreenBottomNative: Int) {

        when (mainScreenBottomNative) {
            0 -> return
            1 -> shimmerLayout.shimmer1.root.visible()
            2 -> shimmerLayout.shimmerSmall.root.visible()
            3 -> shimmerLayout.shimmer3.root.visible()
            4 -> shimmerLayout.shimmer4.root.visible()
            5 -> shimmerLayout.shimmer5.root.visible()
            6 -> shimmerLayout.shimmer6.root.visible()
            7 -> shimmerLayout.shimmer7.root.visible()
            8 -> shimmerLayout.shimmer8.root.visible()
            9 -> shimmerLayout.shimmer9.root.visible()


            else -> shimmerLayout.shimmerNormal.root.visible()
        }
    }

    fun showNativeShimmerView(shimmerView: View, mainScreenBottomNative: Int) {
        // This function is for Java compatibility where we pass a View instead of binding
        // For now, just make the shimmer view visible
        // You can add specific shimmer logic here if needed
        shimmerView.visibility = View.VISIBLE
    }

}