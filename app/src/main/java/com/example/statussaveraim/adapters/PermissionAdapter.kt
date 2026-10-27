package com.example.statussaveraim.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.statussaveraim.databinding.ItemPermissionBinding
import com.example.statussaveraim.models.PermissionModel

class PermissionAdapter(private val permissionList: List<PermissionModel>) :
    RecyclerView.Adapter<PermissionAdapter.PermissionViewHolder>() {

    inner class PermissionViewHolder(val binding: ItemPermissionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionViewHolder {
        val binding =
            ItemPermissionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PermissionViewHolder(binding)
    }

    override fun getItemCount(): Int = permissionList.size

    override fun onBindViewHolder(holder: PermissionViewHolder, position: Int) {
        val permission = permissionList[position]
        with(holder.binding) {
            tvStepText.text = permission.stepText
            tvPermissionTitle.text = permission.title
            tvPermissionDesc.text = permission.description
            ivPermissionImage.setImageResource(permission.imageRes)
        }
    }
}