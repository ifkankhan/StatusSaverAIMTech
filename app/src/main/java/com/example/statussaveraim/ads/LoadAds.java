package com.example.statussaveraim.ads;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.statussaveraim.R;
import com.example.statussaveraim.utils.MyApp;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.preload.PreloadCallbackV2;
import com.google.android.gms.ads.rewarded.RewardedAd;

public class LoadAds {
    public static String interstitial_admob_inApp = MyApp.Companion.getStr(R.string.AM_INTER_AD_ID);
    public static String interstitial_Splash = MyApp.Companion.getStr(R.string.AM_SPLASH_INTER_AD_ID);
    public static String app_open_splash_ad_id_admob = MyApp.Companion.getStr(R.string.AM_SPLASH_APPOPEN_AD_ID);

    public static boolean shouldShowAdmob = true;
    public static boolean isAdsShow = true;
    public static boolean backpressadcontrol = true;
    public static boolean should_show_app_open = true;
    public static PreloadCallbackV2 preloadCallback;

    public static boolean canShowAppOpen = true;

    public static long next_ads_time = 12000;

    public static boolean shouldGoForAds = true;

    public static InterstitialAd admobInterstitialAd;
    public static InterstitialAd fixInterstitialAd;
    public static RewardedAd rewardedAdAdmob;
    public static boolean canReLoadedAdMob = false;
    public static boolean canReLoadedYandex = false;

    public static int adClickCounter = 0;
    public static int adShowAfter = 3;
    public static boolean IsAppOnTimer = false;
    public static boolean isAdLoadFailed = false;
    private static final Handler myHandler = new Handler();

    public static void setHandlerForAd() {
        shouldGoForAds = false;
        Log.d("ConstantAdsLoadAds", "shouldGoForAds onTimeStart: " + shouldGoForAds);
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shouldGoForAds = true;
                Log.d("ConstantAdsLoadAds", "shouldGoForAds onTimeComplete: " + shouldGoForAds);
            }
        }, next_ads_time);
    }

    //1
    public static void preLoadAdsHoneyBee(final Context context) {
        BillingHelper billing=new BillingHelper(context);

        if(billing.shouldShowAds()) {
            //admobeload
            if (admobInterstitialAd == null) {
                canReLoadedAdMob = false;
                InterstitialAd.load(context, LoadAds.interstitial_admob_inApp, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        Log.d("ConstantAdsLoadAds", "Admob loaded");
                        canReLoadedAdMob = true;
                        admobInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.d("ConstantAdsLoadAds", "Admob Faild: " + loadAdError.toString());
                        canReLoadedAdMob = true;
                        admobInterstitialAd = null;
                    }
                });
            } else {
                Log.d("ConstantAdsLoadAds", "admobe AlReady loaded");
            }
        }else{
            Log.d("ConstantAdsLoadAds", "Admobe Controled from Firebase");
        }

    }


    //1
    public static void loadDirectAd(final Context context, String adUnitAdmob, OnVideoLoad videoCallback) {
        directAdmobAd(context, adUnitAdmob, videoCallback);
    }


    public static void directAdmobAd(final Context context, String adUnitAdmob, OnVideoLoad videoCallback) {
        BillingHelper billing=new BillingHelper(context);

        if(billing.shouldShowAds()) {
        if (fixInterstitialAd == null) {
            //canReLoadedAdMob = false;
            InterstitialAd.load(context, adUnitAdmob, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    super.onAdLoaded(interstitialAd);
                    Log.d("ConstantAdsLoadAds", "Direct Admob loaded");
                    fixInterstitialAd = interstitialAd;
                    videoCallback.onLoaded();
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Log.d("ConstantAdsLoadAds", "Direct Admob Faild: " + loadAdError.toString());
                    fixInterstitialAd = null;

                    videoCallback.onFailed();

                }
            });
        } else {
            Log.d("ConstantAdsLoadAds", "admobe AlReady loaded");
            videoCallback.onLoaded();
        }
        }else{
            videoCallback.onFailed();
        }
    }


    public interface OnVideoLoad {
        void onLoaded();

        void onFailed();

        void onPurchased();
    }

    //all
    public static void preReLoadAdsHoneyBee(final Context context) {

        //admobeload
        if (admobInterstitialAd != null) {
            Log.d("ConstantAdsLoadAds", "admobe ReAlReady loaded");
        } else {
            Log.d("ConstantAdsLoadAds", "canReLoadedAdMob " + canReLoadedAdMob);
            if (canReLoadedAdMob) {
                canReLoadedAdMob = false;

                InterstitialAd.load(context, LoadAds.interstitial_admob_inApp, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        Log.d("ConstantAdsLoadAds", "Admob Reloaded");
                        canReLoadedAdMob = true;
                        isAdLoadFailed = false;
                        admobInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.d("ConstantAdsLoadAds", "Admob ReFaild: " + loadAdError.toString());
                        canReLoadedAdMob = true;
                        isAdLoadFailed = true;
                        admobInterstitialAd = null;
                    }
                });

            } else {
                Log.d("ConstantAdsLoadAds", "Admob last ad request is in pending");
            }
        }
    }

    public void loadRewardedAD(Context context, final OnVideoLoad videoCallback) {
       /* RewardedAd.load(
                new AdRequest.Builder("SADD").build(),
                new AdLoadCallback<RewardedAd>() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        // Rewarded ad loaded.
                        super.onAdLoaded(rewardedAd);
                        rewardedAdAdmob = rewardedAd;
                        videoCallback.onLoaded();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        // Rewarded ad failed to load.
                        super.onAdFailedToLoad(adError);
                        rewardedAdAdmob = null;
                        videoCallback.onFailed();
                    }
                }
        );*/
    }

}
