package com.hkproductions.listme.guest.startview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.HouseMemberGuestStartviewItemBinding
import com.hkproductions.listme.guest.database.GuestData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_ADD = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class HouseMemberAdapter(val clickListener: HouseMemberListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(HouseMemberDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HouseMemberViewHolder -> {
                val item = getItem(position) as DataItem.HouseMemberItem
                holder.bind(item.member, clickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_ADD -> ButtonViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> HouseMemberViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.HouseMemberItem -> ITEM_VIEW_TYPE_ITEM
            is DataItem.AddHouseMember -> ITEM_VIEW_TYPE_ADD
        }
    }

    fun submitListAndAddButton(list: List<GuestData>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.AddHouseMember)
                else -> list.map { DataItem.HouseMemberItem(it) } + listOf(DataItem.AddHouseMember)
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    class ButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): ButtonViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.add_button_house_member, parent, false)
                return ButtonViewHolder(view)
            }
        }
    }

    class HouseMemberViewHolder private constructor(val binding: HouseMemberGuestStartviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: GuestData, clickListener: HouseMemberListener) {
            binding.houseMember = item
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    HouseMemberGuestStartviewItemBinding.inflate(layoutInflater, parent, false)
                return HouseMemberViewHolder(binding)
            }
        }
    }

}

class HouseMemberDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

class HouseMemberListener(val clickListener: (guestId: Long) -> Unit) {
    fun onClick(member: GuestData) = clickListener(member.guestDataId)
}

/**
 * Data can be a house member or the add button
 */
sealed class DataItem {

    abstract val id: Long

    /**
     * Hold house member
     * @param member the house member of this Item
     */
    data class HouseMemberItem(val member: GuestData) : DataItem() {
        override val id = member.guestDataId
    }

    /**
     * add Button to add a house member
     * there is only one so it can be an object
     */
    object AddHouseMember : DataItem() {
        override val id = Long.MIN_VALUE
    }
}