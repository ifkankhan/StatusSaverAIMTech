package com.example.statussaveraim.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.statussaveraim.R
import com.example.statussaveraim.adapters.IntroAdapter
import com.example.statussaveraim.ads.AdMob_Native
import com.example.statussaveraim.ads.AdsConstant.homebottombanner
import com.example.statussaveraim.ads.AdsConstant.homebottomnative
import com.example.statussaveraim.ads.AdsConstant.intro1bottombanner
import com.example.statussaveraim.ads.AdsConstant.intro1bottomnative
import com.example.statussaveraim.ads.BannerAdWork
import com.example.statussaveraim.databinding.ActivityIntroBinding
import com.example.statussaveraim.models.IntroModel

class IntroActivity : AppCompatActivity() {

    private val binding: ActivityIntroBinding by lazy {
        ActivityIntroBinding.inflate(layoutInflater)
    }

    private lateinit var introAdapter: IntroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        showingBottomAds()
        setupViewPager()
        setupListeners()
    }
    fun showingBottomAds() {
        if (intro1bottombanner != 0) {
            BannerAdWork.showAdmobBanner(
                intro1bottombanner,
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
                intro1bottomnative
            )
            AdMob_Native.showNativeAdmob(
                intro1bottomnative,
                binding.NativeADBottom.topNativeAdFrameLayout,
                this,
                false,
                this
            )
        }
    }
    private fun setupViewPager() {
        val introList = listOf(
            IntroModel(
                R.drawable.intro_1_img,
                getString(R.string.intro1_title),
                getString(R.string.intro1_desc)
            ),
            IntroModel(
                R.drawable.intro_2_img,
                getString(R.string.intro2_title),
                getString(R.string.intro2_desc)
            ),
            IntroModel(
                R.drawable.intro3_img, // Reuse an image or a default one since we only have two
                getString(R.string.intro3_title),
                getString(R.string.intro3_desc)
            )
        )

        introAdapter = IntroAdapter(introList)
        binding.viewPager.adapter = introAdapter

        setupIndicators(introList.size)
        setCurrentIndicator(0)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
                if (position == introList.size - 1) {
                    binding.btnNext.text = getString(R.string.get_started)
                } else {
                    binding.btnNext.text = getString(R.string.next)
                }
            }
        })
    }

    private fun setupIndicators(count: Int) {
        val indicators = arrayOfNulls<ImageView>(count)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.setImageDrawable(
                ContextCompat.getDrawable(applicationContext, R.drawable.dot_unselected)
            )
            indicators[i]?.layoutParams = layoutParams
            binding.layoutDots.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.layoutDots.childCount
        for (i in 0 until childCount) {
            val imageView = binding.layoutDots.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.dot_selected)
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.dot_unselected)
                )
            }
        }
    }

    private fun setupListeners() {
        binding.btnNext.setOnClickListener {
            if (binding.viewPager.currentItem + 1 < introAdapter.itemCount) {
                binding.viewPager.currentItem += 1
            } else {
                navigateToMain()
            }
        }

        binding.tvSkip.setOnClickListener {
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, PermissionActivity::class.java)
        startActivity(intent)
        finish()
    }
}