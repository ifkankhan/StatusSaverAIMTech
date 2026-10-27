package com.example.statussaveraim.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.decode.VideoFrameDecoder
import coil.load
import com.example.statussaveraim.databinding.ItemSavedStatusBinding
import com.example.statussaveraim.models.WhatsAppModelClass

class SavedStatusAdapter(
    private var statusList: List<WhatsAppModelClass>,
    private val onShareClicked: (WhatsAppModelClass) -> Unit,
    private val onDeleteClicked: (WhatsAppModelClass) -> Unit
) : RecyclerView.Adapter<SavedStatusAdapter.SavedStatusViewHolder>() {

    fun updateList(newList: List<WhatsAppModelClass>) {
        statusList = newList
        notifyDataSetChanged()
    }

    inner class SavedStatusViewHolder(val binding: ItemSavedStatusBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedStatusViewHolder {
        val binding = ItemSavedStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedStatusViewHolder(binding)
    }

    override fun getItemCount(): Int = statusList.size

    override fun onBindViewHolder(holder: SavedStatusViewHolder, position: Int) {
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

            btnShare.setOnClickListener {
                onShareClicked(status)
            }

            btnDelete.setOnClickListener {
                onDeleteClicked(status)
            }
        }
    }
}