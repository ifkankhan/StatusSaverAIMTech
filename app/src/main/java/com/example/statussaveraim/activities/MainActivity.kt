package com.example.statussaveraim.activities

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.statussaveraim.R
import com.example.statussaveraim.adapters.SavedStatusAdapter
import com.example.statussaveraim.adapters.StatusAdapter
import com.example.statussaveraim.ads.AdMob_Native
import com.example.statussaveraim.ads.AdsConstant.homebottombanner
import com.example.statussaveraim.ads.AdsConstant.homebottomnative
import com.example.statussaveraim.ads.BannerAdWork
import com.example.statussaveraim.ads.LoadAds
import com.example.statussaveraim.ads.ShowAds
import com.example.statussaveraim.databinding.ActivityMainBinding
import com.example.statussaveraim.models.WhatsAppModelClass
import com.example.statussaveraim.utils.SharedPrefsHelper
import java.io.File

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var sharedPrefs: SharedPrefsHelper
    private var currentTabIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sharedPrefs = SharedPrefsHelper(this)
        LoadAds.preLoadAdsHoneyBee(this)
        showingBottomAds()
        setupTabs()
        setupWhatsappSwitch()
        
        // Load initial state based on permissions
        loadContentState()

        // Setting Top Right Icons Listeners
        binding.btnSettings.setOnClickListener {
            val intent=(Intent(this, SettingsActivity::class.java))
            ShowAds.directAdsInterstitial(this, LoadAds.admobInterstitialAd,intent)
        }

        binding.btnHelp.setOnClickListener {
            showHowToUseDialog()
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent=(Intent(this@MainActivity, ExitActivity::class.java))

                ShowAds.directAdsInterstitial(this@MainActivity, LoadAds.admobInterstitialAd,intent)
            }
        })
    }
    fun showingBottomAds() {
        if (homebottombanner != 0) {
            BannerAdWork.showAdmobBanner(
                homebottombanner,
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
                homebottomnative
            )
            AdMob_Native.showNativeAdmob(
                homebottomnative,
                binding.NativeADBottom.topNativeAdFrameLayout,
                this,
                false,
                this
            )
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

    private fun setupWhatsappSwitch() {
        updateSwitchUI()

        binding.llWhatsappSwitch.setOnClickListener {
            // Toggle the state
            sharedPrefs.isWhatsappBusinessSelected = !sharedPrefs.isWhatsappBusinessSelected
            updateSwitchUI()
            
            // Reload the content based on the new selection
            loadContentState()
        }
    }

    private fun updateSwitchUI() {
        val isBusiness = sharedPrefs.isWhatsappBusinessSelected
        val ivWa = binding.ivWhatsapp
        val ivWaBus = binding.ivWhatsappBusiness

        if (isBusiness) {
            ivWa.setBackgroundResource(0)
            ivWa.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))

            ivWaBus.setBackgroundResource(R.drawable.bg_top_bar_icon)
            ivWaBus.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            ivWaBus.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green_primary))
        } else {
            ivWa.setBackgroundResource(R.drawable.bg_top_bar_icon)
             ivWa.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            ivWa.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green_primary))

            ivWaBus.setBackgroundResource(0)
            ivWaBus.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        }
    }

    private fun setupTabs() {
        val tabImageContainer = binding.tabImage
        val tabVideoContainer = binding.tabVideo
        val tabSavedContainer = binding.tabSaved

        tabImageContainer.setOnClickListener {
            updateTabSelection(0, tabImageContainer, tabVideoContainer, tabSavedContainer)
        }

        tabVideoContainer.setOnClickListener {
            updateTabSelection(1, tabImageContainer, tabVideoContainer, tabSavedContainer)
        }

        tabSavedContainer.setOnClickListener {
            updateTabSelection(2, tabImageContainer, tabVideoContainer, tabSavedContainer)
        }
    }

    private fun updateTabSelection(
        selectedIndex: Int,
        tabImage: LinearLayout,
        tabVideo: LinearLayout,
        tabSaved: LinearLayout
    ) {
        currentTabIndex = selectedIndex
        
        // Reset all tabs
        resetTab(tabImage, R.drawable.images_main_top_bar)
        resetTab(tabVideo, R.drawable.video_top_bar)
        resetTab(tabSaved, R.drawable.saved_top_bar)

        // Select specific tab
        when (selectedIndex) {
            0 -> selectTab(tabImage, R.drawable.images_main_top_bar)
            1 -> selectTab(tabVideo, R.drawable.video_top_bar)
            2 -> selectTab(tabSaved, R.drawable.saved_top_bar)
        }

        loadContentState()
    }

    private fun selectTab(tabLayout: LinearLayout, iconRes: Int) {
        val icon = tabLayout.getChildAt(0) as LinearLayout
        val imageView = icon.getChildAt(0) as ImageView
        val textView = icon.getChildAt(1) as TextView
        val indicator = tabLayout.getChildAt(1)

        imageView.setColorFilter(ContextCompat.getColor(this, R.color.green_primary))
        textView.setTextColor(ContextCompat.getColor(this, R.color.green_primary))
        indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.green_primary))
    }

    private fun resetTab(tabLayout: LinearLayout, iconRes: Int) {
        val icon = tabLayout.getChildAt(0) as LinearLayout
        val imageView = icon.getChildAt(0) as ImageView
        val textView = icon.getChildAt(1) as TextView
        val indicator = tabLayout.getChildAt(1)

        imageView.setColorFilter(ContextCompat.getColor(this, R.color.tab_unselected))
        textView.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected))
        indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_line))
    }

    private fun hasPermissionForSelectedApp(): Boolean {
        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val persistedPermissions = contentResolver.persistedUriPermissions
        
        return if (sharedPrefs.isWhatsappBusinessSelected) {
            val busPath = prefs.getString("business_path", null)
            busPath != null && persistedPermissions.any { it.uri == Uri.parse(busPath) && it.isReadPermission }
        } else {
            val waPath = prefs.getString("whatsapp_path", null)
            waPath != null && persistedPermissions.any { it.uri == Uri.parse(waPath) && it.isReadPermission }
        }
    }

    private fun loadContentState() {
        binding.fragmentContainer.removeAllViews()

        if (!hasPermissionForSelectedApp()) {
            // Show Permission Required Layout
            val view = layoutInflater.inflate(R.layout.fragment_permission_required, binding.fragmentContainer, false)
            binding.fragmentContainer.addView(view)
            
            // Setup Allow Button
            view.findViewById<Button>(R.id.btnAllow).setOnClickListener {
                val intent = Intent(this, PermissionActivity::class.java)
                startActivity(intent)
            }
        } else {
            // Permission Granted, show actual content based on tab
            val targetPath = if (sharedPrefs.isWhatsappBusinessSelected) {
                getSharedPreferences("app_settings", MODE_PRIVATE).getString("business_path", null)
            } else {
                getSharedPreferences("app_settings", MODE_PRIVATE).getString("whatsapp_path", null)
            }

            if (currentTabIndex == 2) {
                // SAVED TAB
                val savedFiles = getSavedFiles()
                if (savedFiles.isEmpty()) {
                    val view = layoutInflater.inflate(R.layout.fragment_nothing_saved_yet, binding.fragmentContainer, false)
                    binding.fragmentContainer.addView(view)
                    view.findViewById<Button>(R.id.btnBrowse)?.setOnClickListener {
                        binding.tabImage.performClick()
                    }
                } else {
                    setupSavedRecyclerView(savedFiles)
                }
            } else {
                // IMAGE (0) OR VIDEO (1) TAB
                if (targetPath != null) {
                    val files = fetchStatusesFromUri(Uri.parse(targetPath), isVideoTab = currentTabIndex == 1)
                    if (files.isEmpty()) {
                        val view = layoutInflater.inflate(R.layout.fragment_no_status, binding.fragmentContainer, false)
                        binding.fragmentContainer.addView(view)
                        view.findViewById<Button>(R.id.btnGoToWhatsapp)?.setOnClickListener {
                            openWhatsapp()
                        }
                    } else {
                        setupRecyclerView(files)
                    }
                } else {
                    // Fallback to error/empty state if path is inexplicably missing
                    val view = layoutInflater.inflate(R.layout.fragment_no_status, binding.fragmentContainer, false)
                    binding.fragmentContainer.addView(view)
                    view.findViewById<Button>(R.id.btnGoToWhatsapp)?.setOnClickListener {
                        openWhatsapp()
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(list: List<WhatsAppModelClass>) {
        val view = layoutInflater.inflate(R.layout.fragment_status_grid, binding.fragmentContainer, false)
        binding.fragmentContainer.addView(view)

        val rv = view.findViewById<RecyclerView>(R.id.rvStatuses)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmptyState)

        if (list.isEmpty()) {
            rv.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
        } else {
            rv.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE
            rv.layoutManager = GridLayoutManager(this, 3)
            val adapter = StatusAdapter(
                statusList = list,
                onItemClicked = { model -> openFullMediaDialog(model) },
                onDownloadClicked = { model, position ->
                    val destUri = saveStatusItem(model)
                    if (destUri != null) {
                        Toast.makeText(this, "Saved to Gallery!", Toast.LENGTH_SHORT).show()
                        (rv.adapter as StatusAdapter).markAsSaved(model.uri)
                    } else {
                        Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show()
                    }
                }
            )
            rv.adapter = adapter
        }
    }

    private fun setupSavedRecyclerView(list: List<WhatsAppModelClass>) {
        val view = layoutInflater.inflate(R.layout.fragment_status_grid, binding.fragmentContainer, false)
        binding.fragmentContainer.addView(view)

        val rv = view.findViewById<RecyclerView>(R.id.rvStatuses)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmptyState)

        if (list.isEmpty()) {
            rv.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
        } else {
            rv.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE
            rv.layoutManager = GridLayoutManager(this, 3)
            
            val adapter = SavedStatusAdapter(
                statusList = list,
                onItemClicked = { model -> openFullMediaDialog(model) },
                onShareClicked = { model -> shareStatus(model) },
                onDeleteClicked = { model -> deleteStatus(model) }
            )
            rv.adapter = adapter
        }
    }

    private fun openFullMediaDialog(model: WhatsAppModelClass) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_full_media_preview, null)
        val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(dialogView)

        val ivFullImage = dialogView.findViewById<ImageView>(R.id.ivFullImage)
        val videoView = dialogView.findViewById<VideoView>(R.id.videoView)
        val btnClose = dialogView.findViewById<ImageView>(R.id.btnClose)
        val btnShare = dialogView.findViewById<ImageView>(R.id.btnShare)

        btnClose.setOnClickListener { dialog.dismiss() }

        btnShare?.setOnClickListener {
            shareStatus(model)
        }

        val uri = Uri.parse(model.uri)

        if (model.isVideo) {
            ivFullImage.visibility = View.GONE
            videoView.visibility = View.VISIBLE

            val mediaController = MediaController(this)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(uri)
            videoView.setOnPreparedListener { mp ->
                mp.isLooping = true
                videoView.start()
            }
            videoView.setOnErrorListener { _, _, _ ->
                openInExternalApp(model)
                true
            }
        } else {
            videoView.visibility = View.GONE
            ivFullImage.visibility = View.VISIBLE
            ivFullImage.load(uri) {
                crossfade(true)
            }
        }

        dialog.setOnDismissListener {
            if (model.isVideo) {
                videoView.stopPlayback()
            }
        }

        dialog.show()
    }

    private fun openInExternalApp(model: WhatsAppModelClass) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                val type = if (model.isVideo) "video/*" else "image/*"
                setDataAndType(Uri.parse(model.uri), type)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Cannot play/view media", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareStatus(model: WhatsAppModelClass) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = if (model.isVideo) "video/mp4" else "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, Uri.parse(model.uri))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Share via"))
    }

    private fun deleteStatus(model: WhatsAppModelClass) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_confirm, null)
        val dialog = AlertDialog.Builder(this, R.style.TransparentDialogTheme) // Need to define this theme or just use default and make background transparent
            .setView(dialogView)
            .create()

        // Make window background transparent so our custom rounded corners show
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
            try {
                val uri = Uri.parse(model.uri)
                contentResolver.delete(uri, null, null)
                Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show()
                loadContentState() // Reload the grid to remove the deleted item
            } catch (e: Exception) {
                Log.e("DeleteStatus", "Failed to delete", e)
                Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        dialog.show()
    }
    
    private fun fetchStatusesFromUri(treeUri: Uri, isVideoTab: Boolean): List<WhatsAppModelClass> {
        val list = mutableListOf<WhatsAppModelClass>()
        try {
            val documentFile = DocumentFile.fromTreeUri(this, treeUri)
            documentFile?.listFiles()?.forEach { file ->
                if (!file.isDirectory && file.name?.startsWith(".") == false) {
                    val isVideo = file.name?.endsWith(".mp4", true) == true
                    val isImage = file.name?.endsWith(".jpg", true) == true || file.name?.endsWith(".png", true) == true
                    
                    if ((isVideoTab && isVideo) || (!isVideoTab && isImage)) {
                        list.add(
                            WhatsAppModelClass(
                                name = file.name ?: "Unknown",
                                uri = file.uri.toString(),
                                date = file.lastModified().toString(),
                                isVideo = isVideo
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("FetchStatus", "Error fetching from treeUri: $treeUri", e)
        }
        return list.sortedByDescending { it.date }
    }

    private fun getSavedFiles(): List<WhatsAppModelClass> {
        val list = mutableListOf<WhatsAppModelClass>()
        
        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATE_MODIFIED,
            MediaStore.MediaColumns.DATA
        )

        // Android 10+ uses RELATIVE_PATH, older uses DATA matching
        val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            "${MediaStore.MediaColumns.RELATIVE_PATH} LIKE ?"
        } else {
            "${MediaStore.MediaColumns.DATA} LIKE ?"
        }
        val selectionArgs = arrayOf("%StatusSaver%")

        try {
            // Query Saved Images
            val imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            contentResolver.query(imageUri, projection, selection, selectionArgs, "${MediaStore.MediaColumns.DATE_MODIFIED} DESC")?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn) ?: "Unknown"
                    val date = cursor.getLong(dateColumn).toString()
                    val uri = Uri.withAppendedPath(imageUri, id.toString()).toString()
                    list.add(WhatsAppModelClass(name, uri, date, false))
                }
            }

            // Query Saved Videos
            val videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            contentResolver.query(videoUri, projection, selection, selectionArgs, "${MediaStore.MediaColumns.DATE_MODIFIED} DESC")?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn) ?: "Unknown"
                    val date = cursor.getLong(dateColumn).toString()
                    val uri = Uri.withAppendedPath(videoUri, id.toString()).toString()
                    list.add(WhatsAppModelClass(name, uri, date, true))
                }
            }
        } catch (e: Exception) {
            Log.e("SavedStatus", "Error fetching saved items", e)
        }

        return list.sortedByDescending { it.date }
    }

    private fun saveStatusItem(model: WhatsAppModelClass): Uri? {
        try {
            val sourceUri = Uri.parse(model.uri)
            val inputStream = contentResolver.openInputStream(sourceUri) ?: return null

            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, model.name)
                put(MediaStore.MediaColumns.MIME_TYPE, if (model.isVideo) "video/mp4" else "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/StatusSaver")
                } else {
                    val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "StatusSaver")
                    if (!directory.exists()) directory.mkdirs()
                    val file = File(directory, model.name)
                    put(MediaStore.MediaColumns.DATA, file.absolutePath)
                }
            }

            val collection = if (model.isVideo) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                } else {
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                } else {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
            }

            val destUri = contentResolver.insert(collection, values)
            if (destUri != null) {
                contentResolver.openOutputStream(destUri)?.use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                return destUri
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    
    private fun openWhatsapp() {
        val packageName = if (sharedPrefs.isWhatsappBusinessSelected) {
            "com.whatsapp.w4b"
        } else {
            "com.whatsapp"
        }
        
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "App is not installed.", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Refresh content in case permissions were granted in PermissionActivity
        if (::sharedPrefs.isInitialized) {
            loadContentState()
        }
    }
}