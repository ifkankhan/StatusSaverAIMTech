package com.example.statussaveraim.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.statussaveraim.databinding.ItemIntroBinding
import com.example.statussaveraim.models.IntroModel

class IntroAdapter(private val introList: List<IntroModel>) :
    RecyclerView.Adapter<IntroAdapter.IntroViewHolder>() {

    inner class IntroViewHolder(val binding: ItemIntroBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroViewHolder {
        val binding =
            ItemIntroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IntroViewHolder(binding)
    }

    override fun getItemCount(): Int = introList.size

    override fun onBindViewHolder(holder: IntroViewHolder, position: Int) {
        val intro = introList[position]
        with(holder.binding) {
            ivIntroImage.setImageResource(intro.imageRes)
            tvIntroTitle.text = intro.title
            tvIntroDesc.text = intro.description
        }
    }
}