plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.statussaveraim"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "statussaver.statusdownloader.downloadstatus.videoimagesaver.wasaver"
        minSdk = 24
        targetSdk = 37
        versionCode = 2
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
            resValue("string", "ADMOBE_ID", "ca-app-pub-4016151566711692~1327740831")
            resValue("string", "AM_BANNER_AD_ID", "ca-app-pub-3940256099942544/2014213617")
            resValue("string", "AM_INTER_AD_ID", "ca-app-pub-3940256099942544/1033173712")
            resValue("string", "AM_SPLASH_APPOPEN_AD_ID", "ca-app-pub-3940256099942544/9257395921")
            resValue("string", "AM_SPLASH_INTER_AD_ID", "ca-app-pub-3940256099942544/1033173712")
            resValue("string", "AM_NATIVE_AD_ID", "ca-app-pub-3940256099942544/2247696110")
            resValue("string", "AM_NATIVE_INTRO", "ca-app-pub-3940256099942544/2247696110")
            resValue("string", "AM_APPOPEN_AD_ID", "ca-app-pub-3940256099942544/9257395921")
        }
        debug {
            resValue("string", "ADMOBE_ID", "ca-app-pub-4016151566711692~1327740831")
            resValue("string", "AM_BANNER_AD_ID", "ca-app-pub-3940256099942544/2014213617")
            resValue("string", "AM_INTER_AD_ID", "ca-app-pub-3940256099942544/1033173712")
            resValue("string", "AM_SPLASH_APPOPEN_AD_ID", "ca-app-pub-3940256099942544/9257395921")
            resValue("string", "AM_SPLASH_INTER_AD_ID", "ca-app-pub-3940256099942544/1033173712")
            resValue("string", "AM_NATIVE_AD_ID", "ca-app-pub-3940256099942544/2247696110")
            resValue("string", "AM_NATIVE_INTRO", "ca-app-pub-3940256099942544/2247696110")
            resValue("string", "AM_APPOPEN_AD_ID", "ca-app-pub-3940256099942544/9257395921")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
        resValues = true
    }
    if (file("google-services.json").exists()) {
        apply(plugin = "com.google.gms.google-services")
        apply(plugin = "com.google.firebase.crashlytics")
    }}
if (file("google-services.json").exists()) {
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.androidx.lifecycle.process)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.analytics.ktx)

    implementation("io.coil-kt:coil:2.6.0")
    implementation("io.coil-kt:coil-video:2.6.0")
    implementation("androidx.documentfile:documentfile:1.0.1")
    //google billing
    implementation("com.android.billingclient:billing:9.1.0")
    //Ads
    implementation("com.google.android.gms:play-services-ads:24.4.0")
    //shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    //SDP
    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation("com.intuit.ssp:ssp-android:1.0.6")
}