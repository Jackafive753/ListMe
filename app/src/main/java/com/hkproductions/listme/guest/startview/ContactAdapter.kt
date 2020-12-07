package com.hkproductions.listme.guest.startview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.GuestAddButtonHouseMemberBinding
import com.hkproductions.listme.databinding.GuestStartviewContactItemBinding
import com.hkproductions.listme.guest.database.GuestData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_ADD = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class ContactAdapter(
    private val memberDetailListener: ContactListener,
    private val memberAddListener: AddContactListener
) : ListAdapter<DataItem, RecyclerView.ViewHolder>(ContactDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContactViewHolder -> {
                val item = getItem(position) as DataItem.ContactItem
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
            ITEM_VIEW_TYPE_ITEM -> ContactViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.ContactItem -> ITEM_VIEW_TYPE_ITEM
            is DataItem.AddContact -> ITEM_VIEW_TYPE_ADD
        }
    }

    /**
     * fill adapter with given house_member data and add the addButton at the end of the list
     * @param list list with house_member
     */
    fun submitListAndAddButton(list: List<GuestData>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.AddContact)
                else -> list.map { DataItem.ContactItem(it) } + listOf(DataItem.AddContact)
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
    class ButtonViewHolder private constructor(private val binding: GuestAddButtonHouseMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: AddContactListener) {
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ButtonViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GuestAddButtonHouseMemberBinding.inflate(
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
    class ContactViewHolder private constructor(private val binding: GuestStartviewContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: GuestData, clickListener: ContactListener) {
            binding.contactButton.text =
                binding.root.context.resources.getString(
                    R.string.contact_short_info,
                    item.firstName,
                    item.lastName,
                    item.phoneNumber
                )
            binding.contact = item
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    GuestStartviewContactItemBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                return ContactViewHolder(binding)
            }
        }
    }

}

class ContactDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

class ContactListener(val clickListener: (guestId: Long) -> Unit) {
    fun onClick(member: GuestData) = clickListener(member.guestDataId)
}

class AddContactListener(val clickListener: (Any?) -> Unit) {
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
    data class ContactItem(val member: GuestData) : DataItem() {
        override val id = member.guestDataId
    }

    /**
     * add Button to add a house member
     * there is only one so it can be an object
     */
    object AddContact : DataItem() {
        override val id = Long.MIN_VALUE
    }
}