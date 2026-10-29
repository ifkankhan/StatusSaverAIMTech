package com.example.statussaveraim.ads;

import android.content.Context;
import android.content.SharedPreferences;

public class BillingHelper {
    private SharedPreferences billingPreferences;

    public BillingHelper(Context content) {
        billingPreferences = content.getSharedPreferences("PurchasePrefs", Context.MODE_PRIVATE);
    }

    public boolean shouldShowAds() {
        return !((billingPreferences.getBoolean("monthly_subscription", false)) ||
                (billingPreferences.getBoolean("weekly_subscription", false)) ||
                (billingPreferences.getBoolean("yearly_subscription", false)));

    }
}
