package com.example.statussaveraim.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.decode.VideoFrameDecoder
import coil.load
import coil.size.Scale
import com.example.statussaveraim.R
import com.example.statussaveraim.databinding.ItemStatusBinding
import com.example.statussaveraim.models.WhatsAppModelClass

class StatusAdapter(
    private var statusList: List<WhatsAppModelClass>,
    private val savedStatusUris: Set<String>,
    private val onItemClicked: (WhatsAppModelClass) -> Unit,
    private val onDownloadClicked: (WhatsAppModelClass, Int) -> Unit
) : RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    fun updateList(newList: List<WhatsAppModelClass>) {
        if (sameUris(statusList, newList)) return
        statusList = newList
        notifyDataSetChanged()
    }

    fun notifySaved(uri: String) {
        val index = statusList.indexOfFirst { it.uri == uri }
        if (index != -1) notifyItemChanged(index)
    }

    private fun sameUris(current: List<WhatsAppModelClass>, next: List<WhatsAppModelClass>): Boolean {
        if (current.size != next.size) return false
        for (i in current.indices) {
            if (current[i].uri != next[i].uri) return false
        }
        return true
    }

    inner class StatusViewHolder(val binding: ItemStatusBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding = ItemStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(binding)
    }

    override fun getItemCount(): Int = statusList.size

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val status = statusList[position]

        with(holder.binding) {
            ivStatus.load(status.uri) {
                size(360)
                scale(Scale.FILL)
                crossfade(false)
                if (status.isVideo) {
                    decoderFactory { result, options, _ ->
                        VideoFrameDecoder(result.source, options)
                    }
                }
            }

            ivPlayVideo.visibility = if (status.isVideo) View.VISIBLE else View.GONE

            root.setOnClickListener {
                onItemClicked(status)
            }

            val alreadySaved = savedStatusUris.contains(status.uri)
            btnDownload.setImageResource(
                if (alreadySaved) R.drawable.ic_check_green_circle else R.drawable.ic_download_orange
            )
            btnDownload.bringToFront()
            btnDownload.setOnClickListener {
                if (alreadySaved) return@setOnClickListener
                onDownloadClicked(status, holder.bindingAdapterPosition)
            }
        }
    }
}