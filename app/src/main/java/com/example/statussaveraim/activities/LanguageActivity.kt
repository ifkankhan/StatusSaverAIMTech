package com.example.statussaveraim.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.statussaveraim.adapters.LanguageAdapter
import com.example.statussaveraim.ads.AdMob_Native
import com.example.statussaveraim.ads.AdsConstant.languagebottombanner
import com.example.statussaveraim.ads.AdsConstant.languagebottomnative
import com.example.statussaveraim.ads.BannerAdWork
import com.example.statussaveraim.databinding.ActivityLanguageBinding
import com.example.statussaveraim.models.LanguageModel

class LanguageActivity : AppCompatActivity() {

    private val binding: ActivityLanguageBinding by lazy {
        ActivityLanguageBinding.inflate(layoutInflater)
    }

    private var selectedLanguage: LanguageModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        showingBottomAds()
        setupRecyclerView()

        binding.btnSelect.setOnClickListener {
            if (selectedLanguage != null) {
                Toast.makeText(this, "Selected: ${selectedLanguage?.name}", Toast.LENGTH_SHORT)
                    .show()
                // Navigate to Intro Activity
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please select a language", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showingBottomAds() {
        if (languagebottombanner != 0) {
            BannerAdWork.showAdmobBanner(
                languagebottombanner,
                this,
                binding.bannerADBottom.bannerContainer,
                false,
                this
            )
        } else {
            binding.bannerADBottom.bannerAdShimmerView.visibility = View.GONE
            binding.bannerADBottom.bannerContainer.visibility = View.GONE
            AdMob_Native.showNativeShimmer(
                binding.NativeADBottom.shimmerLayout,
                languagebottomnative
            )
            AdMob_Native.showNativeAdmob(
                languagebottomnative,
                binding.NativeADBottom.topNativeAdFrameLayout,
                this,
                false,
                this
            )
        }
    }

    private fun setupRecyclerView() {
        val languages = listOf(
            LanguageModel("Spanish", "es"),
            LanguageModel("French", "fr"),
            LanguageModel("Arabic", "sa"),
            LanguageModel("Germany", "de"),
            LanguageModel("English", "us", true),
            LanguageModel("Turkish", "tr"),
            LanguageModel("Russian", "ru"),
            LanguageModel("Chinese", "cn"),
            LanguageModel("Urdu", "pk"),
            LanguageModel("Indonesia", "id"),
            LanguageModel("Korean", "kr"),
            LanguageModel("Japanese", "jp"),
            LanguageModel("Italian", "it"),
            LanguageModel("Hindi", "in"),
            LanguageModel("Persian", "ir"),
            LanguageModel("Greek", "gr")
        )

        selectedLanguage = languages.find { it.isSelected }

        val adapter = LanguageAdapter(languages) { language ->
            selectedLanguage = language
        }

        binding.rvLanguages.adapter = adapter
    }
}