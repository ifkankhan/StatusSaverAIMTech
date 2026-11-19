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
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private var loadedSourceKey: String? = null
    private var loadToken = 0

    private var permissionView: View? = null
    private var imagePage: View? = null
    private var videoPage: View? = null
    private var savedPage: View? = null
    private var imageAdapter: StatusAdapter? = null
    private var videoAdapter: StatusAdapter? = null
    private var savedAdapter: SavedStatusAdapter? = null

    private val savedStatusUris = mutableSetOf<String>()
    private val savingUris = mutableSetOf<String>()

    private val loadingView: ProgressBar by lazy {
        ProgressBar(this).apply {
            isIndeterminate = true
            val size = (48 * resources.displayMetrics.density).toInt()
            layoutParams = FrameLayout.LayoutParams(size, size, Gravity.CENTER)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sharedPrefs = SharedPrefsHelper(this)
        LoadAds.preLoadAdsHoneyBee(this)
        showingBottomAds()
        setupTabs()
        setupWhatsappSwitch()

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
            refreshContent()
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

        resetTab(tabImage, R.drawable.images_main_top_bar)
        resetTab(tabVideo, R.drawable.video_top_bar)
        resetTab(tabSaved, R.drawable.saved_top_bar)

        when (selectedIndex) {
            0 -> selectTab(tabImage, R.drawable.images_main_top_bar)
            1 -> selectTab(tabVideo, R.drawable.video_top_bar)
            2 -> selectTab(tabSaved, R.drawable.saved_top_bar)
        }

        showCurrentTab()
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

    private fun currentSourceKey(): String? {
        val path = currentStatusPath() ?: return null
        return "${sharedPrefs.isWhatsappBusinessSelected}|$path"
    }

    private fun currentStatusPath(): String? {
        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        return if (sharedPrefs.isWhatsappBusinessSelected) {
            prefs.getString("business_path", null)
        } else {
            prefs.getString("whatsapp_path", null)
        }
    }

    private fun refreshContent() {
        if (!hasPermissionForSelectedApp()) {
            showPermissionRequired()
            return
        }

        val sourceKey = currentSourceKey()
        if (sourceKey == null) {
            loadToken++
            loadedSourceKey = null
            hidePermission()
            dropStatusPages()
            bindImagePage(emptyList())
            bindVideoPage(emptyList())
            showCurrentTab()
            refreshSavedOnly()
            return
        }

        if (sourceKey != loadedSourceKey) {
            loadedSourceKey = sourceKey
            dropStatusPages()
        }
        hidePermission()

        val token = ++loadToken
        showLoadingIfEmpty()
        val path = sourceKey.substringAfter("|")

        lifecycleScope.launch {
            val split = withContext(Dispatchers.IO) {
                fetchStatusesSplit(Uri.parse(path))
            }
            val saved = withContext(Dispatchers.IO) { getSavedFiles() }
            if (token != loadToken || isFinishing || isDestroyed) return@launch
            if (sourceKey != loadedSourceKey) return@launch

            hideLoading()
            bindImagePage(split.first)
            bindVideoPage(split.second)
            bindSavedPage(saved)
            showCurrentTab()
        }
    }

    private fun refreshSavedOnly() {
        lifecycleScope.launch {
            val saved = withContext(Dispatchers.IO) { getSavedFiles() }
            if (isFinishing || isDestroyed || !hasPermissionForSelectedApp()) return@launch
            bindSavedPage(saved)
            showCurrentTab()
        }
    }

    private fun tabVisibility(tabIndex: Int): Int {
        return if (currentTabIndex == tabIndex) View.VISIBLE else View.GONE
    }

    private fun showPermissionRequired() {
        loadToken++
        loadedSourceKey = null
        if (permissionView?.parent != null && imagePage == null && videoPage == null && savedPage == null) {
            return
        }
        dropStatusPages()
        hideLoading()
        binding.fragmentContainer.removeAllViews()
        val view = layoutInflater.inflate(R.layout.fragment_permission_required, binding.fragmentContainer, false)
        view.findViewById<Button>(R.id.btnAllow).setOnClickListener {
            startActivity(Intent(this, PermissionActivity::class.java))
        }
        permissionView = view
        binding.fragmentContainer.addView(view)
    }

    private fun hidePermission() {
        permissionView?.let {
            if (it.parent != null) binding.fragmentContainer.removeView(it)
        }
        permissionView = null
    }

    private fun dropStatusPages() {
        imagePage?.let { binding.fragmentContainer.removeView(it) }
        videoPage?.let { binding.fragmentContainer.removeView(it) }
        savedPage?.let { binding.fragmentContainer.removeView(it) }
        imagePage = null
        videoPage = null
        savedPage = null
        imageAdapter = null
        videoAdapter = null
        savedAdapter = null
    }

    private fun showLoadingIfEmpty() {
        if (imagePage != null || videoPage != null || savedPage != null) return
        if (loadingView.parent == null) {
            binding.fragmentContainer.addView(loadingView)
        }
    }

    private fun hideLoading() {
        if (loadingView.parent != null) {
            binding.fragmentContainer.removeView(loadingView)
        }
    }

    private fun showCurrentTab() {
        imagePage?.visibility = tabVisibility(0)
        videoPage?.visibility = tabVisibility(1)
        savedPage?.visibility = tabVisibility(2)
    }

    private fun bindImagePage(files: List<WhatsAppModelClass>) {
        imagePage = bindStatusPage(
            existing = imagePage,
            existingAdapter = imageAdapter,
            files = files,
            tabIndex = 0,
            createAdapter = { list ->
                StatusAdapter(
                    statusList = list,
                    savedStatusUris = savedStatusUris,
                    onItemClicked = { model -> openFullMediaDialog(model) },
                    onDownloadClicked = { model, _ -> downloadStatus(model) }
                ).also { imageAdapter = it }
            }
        )
    }

    private fun bindVideoPage(files: List<WhatsAppModelClass>) {
        videoPage = bindStatusPage(
            existing = videoPage,
            existingAdapter = videoAdapter,
            files = files,
            tabIndex = 1,
            createAdapter = { list ->
                StatusAdapter(
                    statusList = list,
                    savedStatusUris = savedStatusUris,
                    onItemClicked = { model -> openFullMediaDialog(model) },
                    onDownloadClicked = { model, _ -> downloadStatus(model) }
                ).also { videoAdapter = it }
            }
        )
    }

    private fun bindStatusPage(
        existing: View?,
        existingAdapter: StatusAdapter?,
        files: List<WhatsAppModelClass>,
        tabIndex: Int,
        createAdapter: (List<WhatsAppModelClass>) -> StatusAdapter
    ): View {
        if (files.isEmpty()) {
            if (existing != null && existing.findViewById<Button>(R.id.btnGoToWhatsapp) != null) {
                existing.visibility = tabVisibility(tabIndex)
                return existing
            }
            existing?.let { binding.fragmentContainer.removeView(it) }
            if (tabIndex == 0) imageAdapter = null else videoAdapter = null
            val view = layoutInflater.inflate(R.layout.fragment_no_status, binding.fragmentContainer, false)
            view.findViewById<Button>(R.id.btnGoToWhatsapp)?.setOnClickListener { openWhatsapp() }
            view.visibility = tabVisibility(tabIndex)
            binding.fragmentContainer.addView(view)
            return view
        }

        if (existing != null && existingAdapter != null &&
            existing.findViewById<RecyclerView>(R.id.rvStatuses) != null
        ) {
            existingAdapter.updateList(files)
            existing.visibility = tabVisibility(tabIndex)
            return existing
        }

        existing?.let { binding.fragmentContainer.removeView(it) }
        val view = layoutInflater.inflate(R.layout.fragment_status_grid, binding.fragmentContainer, false)
        val rv = view.findViewById<RecyclerView>(R.id.rvStatuses)
        configureStatusRecycler(rv)
        rv.adapter = createAdapter(files)
        view.findViewById<TextView>(R.id.tvEmptyState)?.visibility = View.GONE
        view.visibility = tabVisibility(tabIndex)
        binding.fragmentContainer.addView(view)
        return view
    }

    private fun bindSavedPage(files: List<WhatsAppModelClass>) {
        if (files.isEmpty()) {
            if (savedPage?.findViewById<Button>(R.id.btnBrowse) != null) {
                savedPage?.visibility = tabVisibility(2)
                return
            }
            savedPage?.let { binding.fragmentContainer.removeView(it) }
            savedAdapter = null
            val view = layoutInflater.inflate(R.layout.fragment_nothing_saved_yet, binding.fragmentContainer, false)
            view.findViewById<Button>(R.id.btnBrowse)?.setOnClickListener {
                binding.tabImage.performClick()
            }
            view.visibility = tabVisibility(2)
            binding.fragmentContainer.addView(view)
            savedPage = view
            return
        }

        val recycler = savedPage?.findViewById<RecyclerView>(R.id.rvStatuses)
        if (recycler != null && savedAdapter != null) {
            savedAdapter?.updateList(files)
            savedPage?.visibility = tabVisibility(2)
            return
        }

        savedPage?.let { binding.fragmentContainer.removeView(it) }
        val view = layoutInflater.inflate(R.layout.fragment_status_grid, binding.fragmentContainer, false)
        val rv = view.findViewById<RecyclerView>(R.id.rvStatuses)
        configureStatusRecycler(rv)
        val adapter = SavedStatusAdapter(
            statusList = files,
            onItemClicked = { model -> openFullMediaDialog(model) },
            onShareClicked = { model -> shareStatus(model) },
            onDeleteClicked = { model -> deleteStatus(model) }
        )
        rv.adapter = adapter
        view.findViewById<TextView>(R.id.tvEmptyState)?.visibility = View.GONE
        view.visibility = tabVisibility(2)
        binding.fragmentContainer.addView(view)
        savedPage = view
        savedAdapter = adapter
    }

    private fun configureStatusRecycler(rv: RecyclerView) {
        rv.setHasFixedSize(true)
        rv.setItemViewCacheSize(12)
        rv.itemAnimator = null
        rv.layoutManager = GridLayoutManager(this, 3)
    }

    private fun downloadStatus(model: WhatsAppModelClass, onDone: ((Boolean) -> Unit)? = null) {
        if (model.uri in savedStatusUris || !savingUris.add(model.uri)) {
            onDone?.invoke(model.uri in savedStatusUris)
            return
        }
        lifecycleScope.launch {
            val destUri = withContext(Dispatchers.IO) { saveStatusItem(model) }
            savingUris.remove(model.uri)
            val success = destUri != null
            if (success) {
                savedStatusUris.add(model.uri)
                imageAdapter?.notifySaved(model.uri)
                videoAdapter?.notifySaved(model.uri)
                Toast.makeText(this@MainActivity, "Saved to Gallery!", Toast.LENGTH_SHORT).show()
                refreshSavedOnly()
            } else if (!isFinishing) {
                Toast.makeText(this@MainActivity, "Failed to save", Toast.LENGTH_SHORT).show()
            }
            onDone?.invoke(success)
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

        val btnDownload = dialogView.findViewById<ImageView>(R.id.btnDownload)
        if (currentTabIndex == 2) {
            btnDownload?.visibility = View.GONE
        } else {
            fun renderDownload() {
                val saved = model.uri in savedStatusUris
                btnDownload?.setImageResource(
                    if (saved) R.drawable.ic_check_green_circle else R.drawable.ic_download_orange
                )
            }
            renderDownload()
            btnDownload?.setOnClickListener {
                if (model.uri in savedStatusUris) return@setOnClickListener
                downloadStatus(model) { success ->
                    if (success && dialog.isShowing) renderDownload()
                }
            }
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
            dialog.dismiss()
            lifecycleScope.launch {
                val deleted = withContext(Dispatchers.IO) {
                    try {
                        contentResolver.delete(Uri.parse(model.uri), null, null) > 0
                    } catch (e: Exception) {
                        Log.e("DeleteStatus", "Failed to delete", e)
                        false
                    }
                }
                if (isFinishing || isDestroyed) return@launch
                if (deleted) {
                    Toast.makeText(this@MainActivity, "Deleted successfully", Toast.LENGTH_SHORT).show()
                    refreshSavedOnly()
                } else {
                    Toast.makeText(this@MainActivity, "Failed to delete", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()
    }
    
    private fun fetchStatusesSplit(treeUri: Uri): Pair<List<WhatsAppModelClass>, List<WhatsAppModelClass>> {
        val images = mutableListOf<WhatsAppModelClass>()
        val videos = mutableListOf<WhatsAppModelClass>()
        try {
            if (DocumentsContract.isTreeUri(treeUri)) {
                val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
                    treeUri,
                    DocumentsContract.getTreeDocumentId(treeUri)
                )
                val projection = arrayOf(
                    DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                    DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                    DocumentsContract.Document.COLUMN_MIME_TYPE,
                    DocumentsContract.Document.COLUMN_LAST_MODIFIED
                )
                contentResolver.query(childrenUri, projection, null, null, null)?.use { cursor ->
                    val idCol = cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DOCUMENT_ID)
                    val nameCol = cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DISPLAY_NAME)
                    val mimeCol = cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_MIME_TYPE)
                    val dateCol = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED)
                    while (cursor.moveToNext()) {
                        val name = cursor.getString(nameCol) ?: continue
                        if (name.startsWith(".")) continue
                        val mime = cursor.getString(mimeCol) ?: ""
                        if (mime == DocumentsContract.Document.MIME_TYPE_DIR) continue
                        val isVideo = mime.startsWith("video/") || name.endsWith(".mp4", true)
                        val isImage = mime.startsWith("image/") ||
                            name.endsWith(".jpg", true) ||
                            name.endsWith(".jpeg", true) ||
                            name.endsWith(".png", true)
                        if (!isVideo && !isImage) continue
                        val docUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, cursor.getString(idCol))
                        val date = if (dateCol >= 0 && !cursor.isNull(dateCol)) {
                            cursor.getLong(dateCol).toString()
                        } else {
                            "0"
                        }
                        val model = WhatsAppModelClass(name, docUri.toString(), date, isVideo)
                        if (isVideo) videos.add(model) else images.add(model)
                    }
                } ?: return fetchStatusesWithDocumentFile(treeUri)
            } else {
                return fetchStatusesWithDocumentFile(treeUri)
            }
        } catch (e: Exception) {
            Log.e("FetchStatus", "Error fetching from treeUri: $treeUri", e)
            return fetchStatusesWithDocumentFile(treeUri)
        }
        images.sortByDescending { it.date.toLongOrNull() ?: 0L }
        videos.sortByDescending { it.date.toLongOrNull() ?: 0L }
        return images to videos
    }

    private fun fetchStatusesWithDocumentFile(treeUri: Uri): Pair<List<WhatsAppModelClass>, List<WhatsAppModelClass>> {
        val images = mutableListOf<WhatsAppModelClass>()
        val videos = mutableListOf<WhatsAppModelClass>()
        try {
            DocumentFile.fromTreeUri(this, treeUri)?.listFiles()?.forEach { file ->
                if (file.isDirectory || file.name?.startsWith(".") != false) return@forEach
                val name = file.name ?: return@forEach
                val isVideo = name.endsWith(".mp4", true)
                val isImage = name.endsWith(".jpg", true) || name.endsWith(".jpeg", true) || name.endsWith(".png", true)
                if (!isVideo && !isImage) return@forEach
                val model = WhatsAppModelClass(name, file.uri.toString(), file.lastModified().toString(), isVideo)
                if (isVideo) videos.add(model) else images.add(model)
            }
        } catch (e: Exception) {
            Log.e("FetchStatus", "DocumentFile fallback failed: $treeUri", e)
        }
        images.sortByDescending { it.date.toLongOrNull() ?: 0L }
        videos.sortByDescending { it.date.toLongOrNull() ?: 0L }
        return images to videos
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
        return try {
            val sourceUri = Uri.parse(model.uri)
            contentResolver.openInputStream(sourceUri)?.use { inputStream ->
                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, model.name)
                    put(MediaStore.MediaColumns.MIME_TYPE, if (model.isVideo) "video/mp4" else "image/jpeg")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/StatusSaver")
                    } else {
                        val directory = File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                            "StatusSaver"
                        )
                        if (!directory.exists()) directory.mkdirs()
                        put(MediaStore.MediaColumns.DATA, File(directory, model.name).absolutePath)
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

                val destUri = contentResolver.insert(collection, values) ?: return@use null
                val copied = contentResolver.openOutputStream(destUri)?.use { outputStream ->
                    inputStream.copyTo(outputStream)
                    true
                } ?: false
                if (!copied) {
                    contentResolver.delete(destUri, null, null)
                    null
                } else {
                    destUri
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
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
        if (::sharedPrefs.isInitialized) {
            refreshContent()
        }
    }
}