package com.example.statussaveraim.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.decode.VideoFrameDecoder
import coil.load
import com.example.statussaveraim.R
import com.example.statussaveraim.databinding.ItemStatusBinding
import com.example.statussaveraim.models.WhatsAppModelClass

class StatusAdapter(
    private var statusList: List<WhatsAppModelClass>,
    private val onItemClicked: (WhatsAppModelClass) -> Unit,
    private val onDownloadClicked: (WhatsAppModelClass, Int) -> Unit
) : RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    private val savedStatusUris = mutableSetOf<String>()

    fun updateList(newList: List<WhatsAppModelClass>) {
        statusList = newList
        notifyDataSetChanged()
    }

    fun markAsSaved(uri: String) {
        savedStatusUris.add(uri)
        // Find index and update just that item if needed, 
        // or rely on notifyDataSetChanged since we just downloaded one.
        val index = statusList.indexOfFirst { it.uri == uri }
        if (index != -1) {
            notifyItemChanged(index)
        }
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
                crossfade(true)
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

            if (savedStatusUris.contains(status.uri)) {
                btnDownload.setImageResource(R.drawable.ic_check_green_circle)
                btnDownload.setOnClickListener(null) // Disable clicking if already saved
            } else {
                btnDownload.setImageResource(R.drawable.ic_download_orange)
                btnDownload.setOnClickListener {
                    onDownloadClicked(status, position)
                }
            }
        }
    }
}