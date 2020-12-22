package com.hkproductions.listme.guest.startview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.GuestItemStartviewContactBinding
import com.hkproductions.listme.guest.database.GuestData
import com.hkproductions.listme.guest.startview.ContactAdapter.ContactViewHolder

class ContactAdapter(
    private val memberDetailListener: ContactListener
) : RecyclerView.Adapter<ContactViewHolder>() {

    var data = listOf<GuestData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, memberDetailListener)
    }

    override fun getItemCount() = data.size

    /**
     * Holder for a house_member on startview of GuestActivity
     * shows name and phone_number
     */
    class ContactViewHolder private constructor(private val binding: GuestItemStartviewContactBinding) :
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
            fun from(parent: ViewGroup): ContactViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    GuestItemStartviewContactBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                return ContactViewHolder(binding)
            }
        }
    }

}

class ContactListener(val clickListener: (guestId: Long) -> Unit) {
    fun onClick(member: GuestData) = clickListener(member.guestDataId)
}