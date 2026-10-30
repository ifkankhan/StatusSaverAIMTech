package com.example.statussaveraim.ads;


import static com.example.statussaveraim.ads.LoadAds.admobInterstitialAd;
import static com.example.statussaveraim.ads.LoadAds.fixInterstitialAd;
import static com.example.statussaveraim.ads.LoadAds.preReLoadAdsHoneyBee;
import static com.example.statussaveraim.ads.LoadAds.setHandlerForAd;
import static com.example.statussaveraim.ads.LoadAds.shouldGoForAds;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.statussaveraim.R;
import com.example.statussaveraim.ads.LoadAds;
import com.example.statussaveraim.utils.Constants;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;


public class ShowAds {

    public static void backPressInterstitial(final Activity context, final InterstitialAd mInterstitialAd) {
        BillingHelper billing = new BillingHelper(context);

        if (billing.shouldShowAds()) {

            if (Constants.INSTANCE.getIsAppOnTimer()) {
                backPressForTimer(context, mInterstitialAd);
            } else {
                backPressForClick(context, mInterstitialAd);
            }
        } else {
            context.finish();
        }
       /* } else {
            if (Constants.INSTANCE.getIsAppOnTimer()) {
               
            } else {
                backPressYandexForClick(context, yandexinterstitialAd);
            }

        }*/
    }

    public static void backPressForClick(final Activity context,
                                         final InterstitialAd mInterstitialAd) {
        if (adShowCounter()) {
            if (mInterstitialAd != null) {
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.loading_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                dialog.show();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        mInterstitialAd.show(context);
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                LoadAds.canShowAppOpen = true;
                                admobInterstitialAd = null;
                                LoadAds.adClickCounter = 0;
                                preReLoadAdsHoneyBee(context);
                                context.finish();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                LoadAds.canShowAppOpen = false;
                            }

                        });
                    }
                }, 1000);
            } else {
                preReLoadAdsHoneyBee(context);

            }
        } else {
            preReLoadAdsHoneyBee(context);
            context.finish();
        }
    }

    public static void backPressForTimer(final Activity context,
                                         final InterstitialAd mInterstitialAd) {
        if (shouldGoForAds) {

            if (mInterstitialAd != null) {
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.loading_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                dialog.show();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        mInterstitialAd.show(context);
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                LoadAds.canShowAppOpen = true;
                                admobInterstitialAd = null;
                                preReLoadAdsHoneyBee(context);
                                setHandlerForAd();
                                context.finish();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                LoadAds.canShowAppOpen = false;
                            }

                        });
                    }
                }, 1000);
            } else {
                preReLoadAdsHoneyBee(context);
                context.finish();
            }
        } else {
            preReLoadAdsHoneyBee(context);
            context.finish();
        }
    }


    public static void directAdsInterstitial(final Context context,
                                             final InterstitialAd mInterstitialAd, final Intent intent) {

        BillingHelper billing = new BillingHelper(context);

        if (billing.shouldShowAds()) {
            if (Constants.INSTANCE.getIsAppOnTimer()) {
                forTimerAds(context, mInterstitialAd, intent);
            } else {
                forClickAds(context, mInterstitialAd, intent);
            }
        } else {
            context.startActivity(intent);
        }

               /* } else {
                    if (Constants.INSTANCE.getIsAppOnTimer()) {
                       
                    } else {
                        
                    }
                }*/

    }


    public static void forClickAds(final Context context,
                                   final InterstitialAd mInterstitialAd, final Intent intent) {
        if (adShowCounter()) {
            if (mInterstitialAd != null) {
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.loading_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                dialog.show();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        mInterstitialAd.show((Activity) context);
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                LoadAds.canShowAppOpen = true;
                                admobInterstitialAd = null;
                                LoadAds.adClickCounter = 0;
                                preReLoadAdsHoneyBee(context);
                                //  HoneyBeeMapNavigationLoadAds.setHandlerForAd();
                                Log.d("ConstantAdsLoadAds", "onAdDismissedFullScreenContent: " + LoadAds.next_ads_time);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                LoadAds.canShowAppOpen = false;
                            }
                        });
                    }
                }, 1000);
            } else {
                preReLoadAdsHoneyBee(context);
                context.startActivity(intent);
            }
        } else {
            preReLoadAdsHoneyBee(context);
            context.startActivity(intent);
        }
    }


    public static void forTimerAds(final Context context,
                                   final InterstitialAd mInterstitialAd, final Intent intent) {
        if (shouldGoForAds) {
            if (mInterstitialAd != null) {
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.loading_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                dialog.show();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        mInterstitialAd.show((Activity) context);
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                LoadAds.canShowAppOpen = true;
                                admobInterstitialAd = null;

                                preReLoadAdsHoneyBee(context);
                                setHandlerForAd();
                                Log.d("ConstantAdsLoadAds", "onAdDismissedFullScreenContent: " + LoadAds.next_ads_time);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                LoadAds.canShowAppOpen = false;
                            }
                        });
                    }
                }, 1000);
            } else {
                preReLoadAdsHoneyBee(context);
                context.startActivity(intent);
            }
        } else {
            preReLoadAdsHoneyBee(context);
            context.startActivity(intent);
        }
    }


    private static void performFragmentTransaction(FragmentManager fm, int containerViewId, Fragment fragment, boolean addToBackStack) {
        if (fm == null || fragment == null) return;
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(containerViewId, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commitAllowingStateLoss();
    }

    static String TAG = "AdLogger:";

    /* public static void logAnalyticsForClicks(String value, Context context) {
         FirebaseAnalytics firebaseAnalytics;
         firebaseAnalytics = FirebaseAnalytics.getInstance(context);
         Bundle bundle = new Bundle();
         try {
             firebaseAnalytics.logEvent(value, bundle);
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 */
    public static Boolean adShowCounter() {
        LoadAds.adClickCounter++;
        Log.d("adShowCounter", " counter  " + LoadAds.adClickCounter + " limit. " + LoadAds.adShowAfter);
        if (LoadAds.adClickCounter >= LoadAds.adShowAfter) {
            Log.d("adShowCounter", "two:   >  show ");
            return true;
        } else {
            Log.d("adShowCounter", " three  else  No");
            return false;
        }
    }

    public static void showingDirectAd(final Activity context, OnAdShowed callback) {

        admobDirectAd(context, callback);

    }

    public static void admobDirectAd(final Activity context, OnAdShowed callback) {
        Log.d("ConstantAdsLoadAds", " showingDirectAd");
        if (fixInterstitialAd != null) {
            Dialog dialog = new Dialog(context);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.loading_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            dialog.show();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    fixInterstitialAd.show(context);
                    fixInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            LoadAds.canShowAppOpen = true;
                            fixInterstitialAd = null;
                            callback.onDismiss();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d("ConstantAdsLoadAds", " showingDirectAd onAdShowed");
                            super.onAdShowedFullScreenContent();
                            LoadAds.canShowAppOpen = false;
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);

                            Log.d("ConstantAdsLoadAds", " showingDirectAd onAdFailed");
                            callback.onDismiss();
                        }
                    });
                }
            }, 1000);
        } else {
            callback.onDismiss();
        }
    }


    public interface OnAdShowed {
        void onDismiss();
    }


}
