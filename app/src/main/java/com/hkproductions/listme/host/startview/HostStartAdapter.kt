package com.hkproductions.listme.host.startview

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hkproductions.listme.host.database.Area
import com.hkproductions.listme.host.database.HostData
import com.hkproductions.listme.host.startview.DataItem.AreaItem
import com.hkproductions.listme.host.startview.DataItem.NameItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val VIEW_TYPE_AREA_ITEM = 0
private const val VIEW_TYPE_NAME_ITEM = 1

class HostStartAdapter(val clickListener: CheckoutListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(DiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_AREA_ITEM -> HostStartAreaViewHolder.from(parent)
            VIEW_TYPE_NAME_ITEM -> HostStartNameViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HostStartNameViewHolder -> {
                val item = getItem(position) as NameItem
                holder.bind(item.data, clickListener)
            }
            is HostStartAreaViewHolder -> {
                val item = getItem(position) as AreaItem
                holder.bind(item.area, item.nameList, clickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AreaItem -> VIEW_TYPE_AREA_ITEM
            is NameItem -> VIEW_TYPE_NAME_ITEM
            else -> -1
        }
    }

    fun submitMap(areaMap: Map<Area, List<HostData>>) {
        adapterScope.launch {
            val list = mutableListOf<DataItem>()
            areaMap.map {
                if (it.key.name == "NO_AREA") {
                    list.addAll(it.value.map { hostData -> NameItem(hostData) })
                } else {
                    list.add(AreaItem(it.key, it.value))
                }
            }
            withContext(Dispatchers.Default) {
                submitList(list)
            }
        }
    }

}

class DiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }
}

sealed class DataItem {
    abstract val id: Long

    data class AreaItem(val area: Area, val nameList: List<HostData>) : DataItem() {
        override val id: Long = -area.areaId
    }

    data class NameItem(val data: HostData) : DataItem() {
        override val id: Long = data.hostDataId
    }
}