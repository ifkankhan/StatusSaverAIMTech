package com.example.statussaveraim.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.statussaveraim.R
import com.example.statussaveraim.databinding.ItemLanguageBinding
import com.example.statussaveraim.models.LanguageModel

class LanguageAdapter(
    private var languages: List<LanguageModel>,
    private val onLanguageSelected: (LanguageModel) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    inner class LanguageViewHolder(val binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding = ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LanguageViewHolder(binding)
    }

    override fun getItemCount(): Int = languages.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = languages[position]
        with(holder.binding) {
            tvLanguageName.text = language.name
            ivFlag.load(language.flagUrl) {
                crossfade(true)
                transformations(RoundedCornersTransformation(8f))
            }

            if (language.isSelected) {
                root.setBackgroundResource(R.drawable.bg_language_item_selected)
                tvLanguageName.setTextColor(ContextCompat.getColor(root.context, R.color.green_primary))
                ivCheck.visibility = View.VISIBLE
            } else {
                root.setBackgroundResource(R.drawable.bg_language_item_unselected)
                tvLanguageName.setTextColor(ContextCompat.getColor(root.context, R.color.text_secondary))
                ivCheck.visibility = View.GONE
            }

            root.setOnClickListener {
                languages.forEach { it.isSelected = false }
                language.isSelected = true
                notifyDataSetChanged()
                onLanguageSelected(language)
            }
        }
    }
}