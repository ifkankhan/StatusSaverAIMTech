package com.example.statussaveraim.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.statussaveraim.R
import com.example.statussaveraim.adapters.PermissionAdapter
import com.example.statussaveraim.ads.AdMob_Native
import com.example.statussaveraim.ads.AdsConstant.homebottombanner
import com.example.statussaveraim.ads.AdsConstant.homebottomnative
import com.example.statussaveraim.ads.AdsConstant.permbottombanner
import com.example.statussaveraim.ads.AdsConstant.permbottomnative
import com.example.statussaveraim.ads.BannerAdWork
import com.example.statussaveraim.databinding.ActivityPermissionBinding
import com.example.statussaveraim.models.PermissionModel
import com.example.statussaveraim.utils.SharedPrefsHelper

class PermissionActivity : AppCompatActivity() {

    private val binding: ActivityPermissionBinding by lazy {
        ActivityPermissionBinding.inflate(layoutInflater)
    }

    private lateinit var permissionAdapter: PermissionAdapter

    private val whatsappLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    try {
                        val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        contentResolver.takePersistableUriPermission(uri, takeFlags)

                        getSharedPreferences("app_settings", MODE_PRIVATE).edit().apply {
                            putString("whatsapp_path", uri.toString())
                            apply()
                        }
                        updateWhatsAppGrantedUI()
                        checkCanContinue()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

    fun showingBottomAds() {
        if (permbottombanner != 0) {
            BannerAdWork.showAdmobBanner(
                permbottombanner,
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
                permbottomnative
            )
            AdMob_Native.showNativeAdmob(
                permbottomnative,
                binding.NativeADBottom.topNativeAdFrameLayout,
                this,
                false,
                this
            )
        }
    }

    private val businessLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    try {
                        val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        contentResolver.takePersistableUriPermission(uri, takeFlags)

                        getSharedPreferences("app_settings", MODE_PRIVATE).edit().apply {
                            putString("business_path", uri.toString())
                            apply()
                        }

                        updateBusinessGrantedUI()
                        checkCanContinue()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        showingBottomAds()
        setupViewPager()
        setupListeners()
    }

    private fun setupViewPager() {
        val permissionList = listOf(
            PermissionModel(
                getString(R.string.step_01),
                getString(R.string.permission1_title),
                getString(R.string.permission1_desc),
                R.drawable.access_1_img
            ),
            PermissionModel(
                getString(R.string.step_02),
                getString(R.string.permission2_title),
                getString(R.string.permission2_desc),
                R.drawable.acess_2_img
            )
        )

        permissionAdapter = PermissionAdapter(permissionList)
        binding.viewPager.adapter = permissionAdapter
        binding.viewPager.isUserInputEnabled =
            false // Optional: Disable swiping to force button clicks

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateStepIndicator(position)
                if (position == permissionList.size - 1) {
                    binding.btnNext.text = getString(R.string.done)
                } else {
                    binding.btnNext.text = getString(R.string.next)
                }
            }
        })
    }

    private fun updateStepIndicator(position: Int) {
        if (position == 0) {
            binding.tvStep1.setBackgroundResource(R.drawable.bg_step_circle_active)
            binding.tvStep2.setBackgroundResource(R.drawable.bg_step_circle_inactive)
            binding.lineStep.setBackgroundColor(Color.parseColor("#E0E0E0"))
        } else {
            binding.tvStep1.setBackgroundResource(R.drawable.bg_step_circle_active)
            binding.tvStep2.setBackgroundResource(R.drawable.bg_step_circle_active)
            binding.lineStep.setBackgroundColor(ContextCompat.getColor(this, R.color.green_primary))
        }
    }

    private fun updateWhatsAppGrantedUI() {
        // Update UI if needed after WA permission granted
    }

    private fun updateBusinessGrantedUI() {
        // Update UI if needed after WA Business permission granted
    }

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun checkCanContinue() {
        val hasWhatsApp = isAppInstalled("com.whatsapp")
        val hasBusiness = isAppInstalled("com.whatsapp.w4b")

        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val waPath = prefs.getString("whatsapp_path", null)
        val busPath = prefs.getString("business_path", null)
        val persistedPermissions = contentResolver.persistedUriPermissions

        val waGranted =
            waPath != null && persistedPermissions.any { it.uri == Uri.parse(waPath) && it.isReadPermission }
        val busGranted =
            busPath != null && persistedPermissions.any { it.uri == Uri.parse(busPath) && it.isReadPermission }

        val canContinue = if (hasWhatsApp && hasBusiness) {
            waGranted || busGranted
        } else if (hasWhatsApp) {
            waGranted
        } else if (hasBusiness) {
            busGranted
        } else {
            true
        }

        if (canContinue) {
            binding.btnNext.setBackgroundResource(R.drawable.bg_continue_enabled)
            binding.btnNext.isEnabled = true
            navigateToMain()
        } else {
            binding.btnNext.setBackgroundResource(R.drawable.bg_continue_disabled)
            binding.btnNext.isEnabled = true
        }
    }

    private fun setupListeners() {
        binding.btnNext.setOnClickListener {
            if (binding.viewPager.currentItem + 1 < permissionAdapter.itemCount) {
                binding.viewPager.currentItem += 1
            } else {
                val sharedPrefs = SharedPrefsHelper(this)
                if (sharedPrefs.isWhatsappBusinessSelected) {
                    openBusinessFolderPicker()
                } else {
                    openWhatsAppFolderPicker()
                }
            }
        }

        binding.tvSkip.setOnClickListener {
            navigateToMain()
        }
    }

    private fun openWhatsAppFolderPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val waUri =
                    Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fmedia/document/primary%3AAndroid%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses")
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, waUri)
            }
            putExtra("android.content.extra.SHOW_ADVANCED", true)
        }
        try {
            whatsappLauncher.launch(intent)
        } catch (e: Exception) {
            val fallbackIntent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            whatsappLauncher.launch(fallbackIntent)
        }
    }

    private fun openBusinessFolderPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val businessUri =
                    Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fmedia/document/primary%3AAndroid%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp%20Business%2FMedia%2F.Statuses")
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, businessUri)
            }
            putExtra("android.content.extra.SHOW_ADVANCED", true)
        }
        try {
            businessLauncher.launch(intent)
        } catch (e: Exception) {
            val fallbackIntent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            businessLauncher.launch(fallbackIntent)
        }
    }

    private fun navigateToMain() {
        val sharedPrefs = SharedPrefsHelper(this)
        sharedPrefs.hasGivenPermission = true
        sharedPrefs.isFirstTimeLaunch = false

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}