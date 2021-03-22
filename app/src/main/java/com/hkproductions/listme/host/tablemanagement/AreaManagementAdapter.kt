package com.hkproductions.listme.host.tablemanagement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hkproductions.listme.databinding.HostItemAreaManagementBinding
import com.hkproductions.listme.host.database.Area
import com.hkproductions.listme.host.database.HostDataDao
import com.hkproductions.listme.host.tablemanagement.AreaManagementAdapter.AreaManagementViewHolder

class AreaManagementAdapter(
    val database: HostDataDao,
    val clickListener: AreaDeleteClickListener,
    val updateListener: AreaUpdateListener
) :
    ListAdapter<Area, AreaManagementViewHolder>(object : ItemCallback<Area>() {
        override fun areItemsTheSame(oldItem: Area, newItem: Area): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Area, newItem: Area): Boolean {
            return oldItem.name == newItem.name &&
                    oldItem.areaId == newItem.areaId
        }

    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaManagementViewHolder {
        return AreaManagementViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AreaManagementViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, updateListener)
    }

    class AreaManagementViewHolder(val binding: HostItemAreaManagementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            area: Area,
            clickListener: AreaDeleteClickListener,
            updateListener: AreaUpdateListener
        ) {
            binding.area = area
            binding.buttonAreaManagementConfirm.setOnClickListener {
                updateListener.onUpdate(area, binding.editTextAreaManagementName.text.toString())
            }
            binding.clickListener = clickListener

        }

        companion object {
            fun from(parent: ViewGroup): AreaManagementViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = HostItemAreaManagementBinding.inflate(inflater)
                return AreaManagementViewHolder(binding)
            }
        }
    }

}

class AreaDeleteClickListener(val clickListener: (area: Area) -> Unit) {
    fun onClick(area: Area) = clickListener(area)
}

class AreaUpdateListener(val updateListener: (area: Area, name: String) -> Unit) {
    fun onUpdate(area: Area, string: String) = updateListener(area, string)
}