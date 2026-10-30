package com.example.statussaveraim.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.statussaveraim.R
import com.example.statussaveraim.ads.AdMob_Native
import com.example.statussaveraim.ads.AdsConstant.exitbottombanner
import com.example.statussaveraim.ads.AdsConstant.exitbottomnative
import com.example.statussaveraim.ads.AdsConstant.homebottombanner
import com.example.statussaveraim.ads.AdsConstant.homebottomnative
import com.example.statussaveraim.ads.BannerAdWork
import com.example.statussaveraim.databinding.ActivityExitBinding

class ExitActivity : AppCompatActivity() {

    private val binding: ActivityExitBinding by lazy {
        ActivityExitBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        showingBottomAds()
        setupListeners()
    }
    fun showingBottomAds() {
        if (exitbottombanner != 0) {
            BannerAdWork.showAdmobBanner(
                exitbottombanner,
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
                exitbottomnative
            )
            AdMob_Native.showNativeAdmob(
                exitbottomnative,
                binding.NativeADBottom.topNativeAdFrameLayout,
                this,
                false,
                this
            )
        }
    }
    private fun setupListeners() {
        // Exit App
        binding.btnLeaveApp.setOnClickListener {
            finishAffinity() // Closes all activities in the task and exits
        }

        // Tap the top rating card -> open store link (optional interactive element based on standard designs)
        binding.exitCard.root.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }
        binding.btnHow.setOnClickListener {
            showHowToUseDialog()
        }
        binding.btnWAB.setOnClickListener {
            finish()
        }
        binding.btnWA.setOnClickListener {
            finish()
        }
    }


    private fun showHowToUseDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_how_to_use, null)
        val dialog = AlertDialog.Builder(this, R.style.TransparentDialogTheme)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Set the formatted HTML text
        dialogView.findViewById<TextView>(R.id.tvStep1).text =
            Html.fromHtml(getString(R.string.how_to_use_step_1), Html.FROM_HTML_MODE_LEGACY)
        dialogView.findViewById<TextView>(R.id.tvStep2).text =
            Html.fromHtml(getString(R.string.how_to_use_step_2), Html.FROM_HTML_MODE_LEGACY)
        dialogView.findViewById<TextView>(R.id.tvStep3).text =
            Html.fromHtml(getString(R.string.how_to_use_step_3), Html.FROM_HTML_MODE_LEGACY)

        dialogView.findViewById<ImageView>(R.id.btnClose).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}