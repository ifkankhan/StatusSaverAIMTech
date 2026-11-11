package com.example.statussaveraim.ads

import androidx.lifecycle.MutableLiveData


object AdsConstant {
    @JvmField
    var splashmove = MutableLiveData(false)
    var backTimer = 2
    var nativebannerfailonoff = 1
    var SplashPreferenceAd = 1
    var splashappopenonoff = 1
    var timedurationtoshowinterstital = 12000
    var interstitialAdsToShowCount = 5
    var splashinterstiaitalonoff: Int = 1
    var openadresumeonoff: Int = 0
    var numofclicksInterstitial: Int = 5

    //Splash
    var splashbottombannerad = 0
    var splashbottomnative = 0

    //Drawer / Intro
    var drawerbottombanner = 1
    var drawerbottomnative = 0


    var MainAdsControl: String? = null

    var willAllAdsNotShow = false
    var isPurchase = false
    var RewardedIntestritialId = ""
    var RewarededvideoId = ""
    var nativecardradius = 25
    var bannerboardercolor = "#2196F3"
    var bannercustombannerheight = 100
    var nativecardbgcolor = "#EFEEEA"
    var nativeheadlinecolor = "#FF0000"
    var nativeAdtextcolor = "#FFFFFF"
    var nativeAdtextbgclr = "#000000"
    var nativelowertxtcolor = "#000000"
    var nativebuttoncolor = "#FF0000"
    var nativebuttontextcolor = "#FFFFFF"
    var nativebordercolor = "#000000"
    var nativeborderwidth = 2 // In dp

    var bannerborderwidth = 1
    var paywallOn = true
    var bannercardradius = 24
    var nativedescriptioncolor = "#FFFFFF"
    var nativeAdicontextcolor = "#FFFFFF"
    var nativeAdiconbgclr = "#D39800"
    var interstitialAdId = ""
    var nativeAdId = ""
    var bannerAdId = ""
    var appOpenAdId = ""
    var rewardedInterstitialAdId = ""

    //Language
    var languagebottombanner = 3
    var languagebottomnative = 0

    //Intro1
    var intro1bottombanner = 1
    var intro1bottomnative = 0
    //Intro2
    var intro2bottombanner = 1
    var intro2bottomnative = 0
    //Intro3
    var intro3bottombanner = 1
    var intro3bottomnative = 0

    //Home
    var homebottombanner = 1
    var homebottomnative = 0

    //Exit
    var exitbottombanner = 0
    var exitbottomnative = 4

    //Permission
    var permbottombanner = 0
    var permbottomnative = 4
    //Settings
    var settingbottombanner = 0
    var settingbottomnative = 3



}