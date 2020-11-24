package com.hkproductions.listme.guest.startview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hkproductions.listme.databinding.AddButtonHouseMemberBinding
import com.hkproductions.listme.databinding.HouseMemberGuestStartviewItemBinding
import com.hkproductions.listme.guest.database.GuestData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_ADD = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class HouseMemberAdapter(
    private val memberDetailListener: HouseMemberListener,
    private val memberAddListener: AddMemberListener
) : ListAdapter<DataItem, RecyclerView.ViewHolder>(HouseMemberDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HouseMemberViewHolder -> {
                val item = getItem(position) as DataItem.HouseMemberItem
                holder.bind(item.member, memberDetailListener)
            }
            is ButtonViewHolder -> {
                holder.bind(memberAddListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_ADD -> ButtonViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> HouseMemberViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.HouseMemberItem -> ITEM_VIEW_TYPE_ITEM
            is DataItem.AddHouseMember -> ITEM_VIEW_TYPE_ADD
        }
    }

    /**
     * fill adapter with given house_member data and add the addButton at the end of the list
     * @param list list with house_member
     */
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

    /**
     * Holder for the add Button on the end of the house_member list
     * in the startview of the GuestActivity
     */
    class ButtonViewHolder private constructor(private val binding: AddButtonHouseMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: AddMemberListener) {
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ButtonViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AddButtonHouseMemberBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return ButtonViewHolder(binding)
            }
        }
    }

    /**
     * Holder for a house_member on startview of GuestActivity
     * shows name and phone_number
     */
    class HouseMemberViewHolder private constructor(private val binding: HouseMemberGuestStartviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: GuestData, clickListener: HouseMemberListener) {
            binding.houseMember = item
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    HouseMemberGuestStartviewItemBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
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

class AddMemberListener(val clickListener: (Any?) -> Unit) {
    fun onClick() = clickListener(null)
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