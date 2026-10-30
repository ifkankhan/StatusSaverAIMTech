package com.example.statussaveraim.ads

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.example.statussaveraim.utils.Constants
import com.google.firebase.analytics.FirebaseAnalytics
import org.json.JSONObject

class PurchaseHelper(private val activityContext: Context) :
    PurchasesUpdatedListener {


    var priceUpdateListener: (() -> Unit)? = null
    private lateinit var googleBillingHoneyBeeMapNavigationClient: BillingClient
    private val TAG = "BillingLoggerAI:"
    private val listAvailHoneyBeeMapNavigationPurchases = ArrayList<ProductDetails>()
    private lateinit var billingHoneyBeeMapNavigationPreferences: SharedPreferences
    private val listAvailEarthLiveSubsMapPurchases = ArrayList<ProductDetails>()

    init {
        initMyBillingClientHoneyBeeMapNavigation()
    }

    private fun initMyBillingClientHoneyBeeMapNavigation() {
        billingHoneyBeeMapNavigationPreferences =
            activityContext.getSharedPreferences("PurchasePrefs", Context.MODE_PRIVATE)
        googleBillingHoneyBeeMapNavigationClient = BillingClient
            .newBuilder(activityContext)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build()
            )
            .setListener(this)
            .build()
        googleBillingHoneyBeeMapNavigationClient.startConnection(object :
            BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Google Billing is Connected")

                    fetchGlobeMapAllSubFromConsole()
                    fetchGlobeMapPurchasedSubFromConsole()
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d(TAG, "Google Billing is  Disconnected")
            }
        })
    }


    fun extractPriceAmountMicros(jsonString: String): String? {
        try {
            val jsonObject = JSONObject(jsonString)
            return jsonObject.getString("price")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    fun purchaseWeekly() {
        Log.d(TAG, "Going to purchase ads_purchase")
        val product =
            listAvailEarthLiveSubsMapPurchases.firstOrNull { it.productId == "weekly_subscription" }
        if (product != null) {
            try {
                val offerToken = product.subscriptionOfferDetails?.firstOrNull()?.offerToken ?: ""
                val productDetailsParamsList = listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(product)
                        .setOfferToken(offerToken)
                        .build()
                )
                val flowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build()
                val responseCode =
                    googleBillingHoneyBeeMapNavigationClient.launchBillingFlow(
                        activityContext as Activity,
                        flowParams
                    ).responseCode
                Log.d(TAG, "Google Billing Response : $responseCode")
            } catch (e: Exception) {
                Log.e(TAG, "Launch billing flow failed", e)
            }
        } else {
            Log.d(TAG, "Nothing to purchase for google billing")
        }
    }

    fun purchaseMonthly() {
        Log.d(TAG, "Going to purchase ads_purchase")
        val product =
            listAvailEarthLiveSubsMapPurchases.firstOrNull { it.productId == "monthly_subscription" }
        if (product != null) {
            try {
                val offerToken = product.subscriptionOfferDetails?.firstOrNull()?.offerToken ?: ""
                val productDetailsParamsList = listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(product)
                        .setOfferToken(offerToken)
                        .build()
                )
                val flowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build()
                val responseCode =
                    googleBillingHoneyBeeMapNavigationClient.launchBillingFlow(
                        activityContext as Activity,
                        flowParams
                    ).responseCode
                Log.d(TAG, "Google Billing Response : $responseCode")
            } catch (e: Exception) {
                Log.e(TAG, "Launch billing flow failed", e)
            }
        } else {
            Log.d(TAG, "Nothing to purchase for google billing")
        }
    }

    fun purchaseYearly() {
        Log.d(TAG, "Going to purchase ads_purchase")
        val product =
            listAvailEarthLiveSubsMapPurchases.firstOrNull { it.productId == "yearly_subscription" }
        if (product != null) {
            try {
                val offerToken = product.subscriptionOfferDetails?.firstOrNull()?.offerToken ?: ""
                val productDetailsParamsList = listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(product)
                        .setOfferToken(offerToken)
                        .build()
                )
                val flowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build()
                val responseCode =
                    googleBillingHoneyBeeMapNavigationClient.launchBillingFlow(
                        activityContext as Activity,
                        flowParams
                    ).responseCode
                Log.d(TAG, "Google Billing Response : $responseCode")
            } catch (e: Exception) {
                Log.e(TAG, "Launch billing flow failed", e)
            }
        } else {
            Log.d(TAG, "Nothing to purchase for google billing")
        }
    }


    private fun fetchGlobeMapAllSubFromConsole() {

        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("monthly_subscription")
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("weekly_subscription")
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("yearly_subscription")
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        googleBillingHoneyBeeMapNavigationClient.queryProductDetailsAsync(params) { billingResult, queryProductDetailsResult ->
            val productDetailsList = queryProductDetailsResult.productDetailsList
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList != null) {
                for (productDetails in productDetailsList) {
                    listAvailEarthLiveSubsMapPurchases.add(productDetails)
                    val price =
                        productDetails.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList?.lastOrNull()?.formattedPrice
                    if (price != null) {
                        when (productDetails.productId) {
                            "monthly_subscription" -> {
                                Log.i("TAG_Billing", "monthly_subscription- $price")
                                Constants.billingMonthly.postValue(price)

                            }

                            "weekly_subscription" -> {
                                Log.i("TAG_Billing", "weekly_subscription- $price")
                                Constants.billingWeekly.postValue(price)
                            }

                            "yearly_subscription" -> {
                                Log.i("TAG_Billing", "yearly_subscription- $price")
                                Constants.billingYearly.postValue(price)
                            }

                        }
                    }
                }
                (activityContext as? Activity)?.runOnUiThread {
                    priceUpdateListener?.invoke()
                }
            } else {
                Log.i(TAG, "No skus or failed query: ${billingResult.responseCode}")
            }
        }

    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?,
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                Log.d(TAG, "onPurchases Successfully Purchased : " + purchase.skus[0])
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.d(TAG, "Google Billing Cancelled")
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Log.d(TAG, "Google Billing Purchased Already")
            Toast.makeText(
                activityContext,
                "You have already purchased this item",
                Toast.LENGTH_LONG
            )
                .show()
        } else {
            Log.d(TAG, "Google billing other error " + billingResult.responseCode)
        }
    }

    private val purchaseAcknowledgedListener: AcknowledgePurchaseResponseListener = object
        : AcknowledgePurchaseResponseListener {
        override fun onAcknowledgePurchaseResponse(p0: BillingResult) {
            Log.d(TAG, "Success Acknowledged : ${p0.responseCode}  :${p0.debugMessage}")
            // fetchHoneyBeeMapNavigationPurchasedInAppsFromConsole()
        }

    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                Log.d(TAG, "Process acknowledging: ${purchase.skus[0]}")
                val acknowledgeParamaters = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                googleBillingHoneyBeeMapNavigationClient.acknowledgePurchase(
                    acknowledgeParamaters,
                    purchaseAcknowledgedListener
                )

                val productId = purchase.products.firstOrNull() ?: return
                listAvailEarthLiveSubsMapPurchases.find { it.productId == productId }
                    ?.let { productDetails ->

                        val offer = productDetails.subscriptionOfferDetails?.firstOrNull()
                        val pricingPhase = offer?.pricingPhases?.pricingPhaseList?.firstOrNull()

                        val price = (pricingPhase?.priceAmountMicros ?: 0L) / 1_000_000.0
                        val currency = pricingPhase?.priceCurrencyCode ?: ""

                        logPurchaseToFirebase(
                            activityContext,
                            productId,
                            price,
                            currency
                        )
                        Log.d(TAG, "handlePurchase: $productId")
                        billingHoneyBeeMapNavigationPreferences.edit()
                            .putBoolean(productId, true).apply()

                        restartApp(activityContext)
                    }
            }
        }
    }

    fun fetchGlobeMapPurchasedSubFromConsole() {
        googleBillingHoneyBeeMapNavigationClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
            object : PurchasesResponseListener {
                override fun onQueryPurchasesResponse(
                    billingResult: BillingResult,
                    listPurchased: MutableList<Purchase>
                ) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        val activeSubIds = listPurchased
                            .filter { it.purchaseState == Purchase.PurchaseState.PURCHASED }
                            .flatMap { it.products }
                            .toSet()
                        Log.d(TAG, " Purchased ID: $activeSubIds")
                        val knownSubIds = listOf(
                            "monthly_subscription",
                            "weekly_subscription",
                            "yearly_subscription"
                        )

                        val editor = billingHoneyBeeMapNavigationPreferences.edit()
                        for (productId in knownSubIds) {
                            if (activeSubIds.contains(productId)) {
                                Log.d(TAG, "Product Purchased: $productId")
                                editor.putBoolean(productId, true)
                            } else {
                                Log.d(TAG, "Product Not Purchased or Refunded: $productId")
                                editor.putBoolean(productId, false)
                            }
                        }
                        editor.apply()
                    }
                }
            })
    }


    fun restartApp(context: Context) {
        val intent = context.packageManager
            .getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        context.startActivity(intent)
        Runtime.getRuntime().exit(0)
    }

    fun logPurchaseToFirebase(context: Context, data: String, price: Double, currency: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.CURRENCY, currency)
            putDouble(FirebaseAnalytics.Param.VALUE, price)
        }

        FirebaseAnalytics.getInstance(context)
            .logEvent("Subscription_${data}", bundle)
    }
}
