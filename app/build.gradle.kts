plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.statussaveraim"
    compileSdk = 37

    defaultConfig {
        applicationId = "statussaver.statusdownloader.downloadstatus.videoimagesaver.wasaver"
        minSdk = 24
        targetSdk = 36
        versionCode = 4
        versionName = "1.0.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            optimization {
                enable = true
            }
            resValue("string", "ADMOBE_ID", "ca-app-pub-1831800168999377~3928883472")
            resValue("string", "AM_BANNER_AD_ID", "ca-app-pub-1831800168999377/4306343334")
            resValue("string", "AM_INTER_AD_ID", "ca-app-pub-1831800168999377/6661043930")
            resValue("string", "AM_SPLASH_APPOPEN_AD_ID", "ca-app-pub-1831800168999377/2502307071")
            resValue("string", "AM_SPLASH_INTER_AD_ID", "ca-app-pub-1831800168999377/2687953997")
            resValue("string", "AM_NATIVE_AD_ID", "ca-app-pub-1831800168999377/3184833352")
            resValue("string", "AM_NATIVE_INTRO",  "ca-app-pub-1831800168999377/7996120996")
            resValue("string", "AM_NATIVE_LANGUAGE", "ca-app-pub-1831800168999377/1189225400")
            resValue("string", "AM_RECTANGLE_INTRO_ID", "ca-app-pub-1831800168999377/7748708982")
            resValue("string", "AM_COLLAPSABLE_INTRO_ID", "ca-app-pub-1831800168999377/2559961482")
            resValue("string", "AM_APPOPEN_AD_ID", "ca-app-pub-1831800168999377/5937108874")
        }
        debug {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("string", "ADMOBE_ID", "ca-app-pub-4016151566711692~1327740831")
            resValue("string", "AM_BANNER_AD_ID", "ca-app-pub-3940256099942544/2014213617")
            resValue("string", "AM_RECTANGLE_INTRO_ID", "ca-app-pub-3940256099942544/2014213617")
            resValue("string", "AM_COLLAPSABLE_INTRO_ID", "ca-app-pub-3940256099942544/2014213617")
            resValue("string", "AM_INTER_AD_ID", "ca-app-pub-3940256099942544/1033173712")
            resValue("string", "AM_SPLASH_APPOPEN_AD_ID", "ca-app-pub-3940256099942544/9257395921")
            resValue("string", "AM_SPLASH_INTER_AD_ID", "ca-app-pub-3940256099942544/1033173712")
            resValue("string", "AM_NATIVE_AD_ID", "ca-app-pub-3940256099942544/2247696110")
            resValue("string", "AM_NATIVE_INTRO", "ca-app-pub-3940256099942544/2247696110")
            resValue("string", "AM_NATIVE_LANGUAGE", "ca-app-pub-3940256099942544/2247696110")
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
}

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