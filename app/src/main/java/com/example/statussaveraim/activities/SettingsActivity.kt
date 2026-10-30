package com.example.statussaveraim.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.statussaveraim.BuildConfig
import com.example.statussaveraim.R
import com.example.statussaveraim.ads.AdMob_Native
import com.example.statussaveraim.ads.AdMob_Native.showNativeShimmer
import com.example.statussaveraim.ads.AdsConstant.settingbottombanner
import com.example.statussaveraim.ads.AdsConstant.settingbottomnative
import com.example.statussaveraim.ads.BannerAdWork
import com.example.statussaveraim.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private val binding: ActivitySettingsBinding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        showingBottomAds()
        setupUI()
        setupListeners()
    }

    fun showingBottomAds() {
        if (settingbottombanner != 0) {
            BannerAdWork.showAdmobBanner(
                settingbottombanner,
                this,
                binding.bannerADBottom.bannerContainer,
                false,
                this
            )
        } else {
            binding.bannerADBottom.bannerAdShimmerView.visibility = View.GONE
            binding.bannerADBottom.bannerContainer.visibility = View.GONE
            showNativeShimmer(
                binding.NativeADBottom.shimmerLayout,
                settingbottomnative
            )
            AdMob_Native.showNativeAdmob(
                settingbottomnative,
                binding.NativeADBottom.topNativeAdFrameLayout,
                this,
                false,
                this
            )
        }
    }

    private fun setupUI() {
        binding.tvVersion.text = BuildConfig.VERSION_NAME
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnHowToUse.setOnClickListener {
            showHowToUseDialog()
        }

        binding.btnRateUs.setOnClickListener {
            showRateUsDialog()
        }

        binding.btnShareApp.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Status Saver")
                val shareMessage =
                    "\nLet me recommend you this application\n\nhttps://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}\n\n"
                putExtra(Intent.EXTRA_TEXT, shareMessage)
            }
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        }

        binding.btnFeedback.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:support@example.com")
                putExtra(Intent.EXTRA_SUBJECT, "Feedback for Status Saver")
            }
            try {
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnPrivacyPolicy.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://statussaveraimtech.blogspot.com/2026/10/aim-tech-status-saver.html")
            )
            startActivity(intent)
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

    private fun showRateUsDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_rate_us, null)
        val dialog = AlertDialog.Builder(this, R.style.TransparentDialogTheme)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val stars = arrayOf(
            dialogView.findViewById<ImageView>(R.id.star1),
            dialogView.findViewById<ImageView>(R.id.star2),
            dialogView.findViewById<ImageView>(R.id.star3),
            dialogView.findViewById<ImageView>(R.id.star4),
            dialogView.findViewById<ImageView>(R.id.star5)
        )

        var currentRating = 0

        for (i in stars.indices) {
            stars[i].setOnClickListener {
                currentRating = i + 1
                for (j in stars.indices) {
                    if (j < currentRating) {
                        stars[j].setImageResource(R.drawable.fill_star)
                    } else {
                        stars[j].setImageResource(R.drawable.unfill_star)
                    }
                }
            }
        }

        dialogView.findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            dialog.dismiss()
            if (currentRating > 0) {
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
            } else {
                Toast.makeText(this, "Please select a rating first", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}