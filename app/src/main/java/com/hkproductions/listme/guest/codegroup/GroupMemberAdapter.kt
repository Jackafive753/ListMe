package com.hkproductions.listme.guest.codegroup

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkproductions.listme.databinding.GuestItemGroupMemberBinding
import com.hkproductions.listme.guest.database.GuestData

class GroupMemberAdapter : RecyclerView.Adapter<GroupMemberAdapter.TextViewHolder>() {

    var data = listOf<GuestData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupMemberAdapter.TextViewHolder {
        return TextViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount() = data.size

    class TextViewHolder(val binding: GuestItemGroupMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(contact: GuestData) {
            binding.textViewItemGroupMemberName.text = "${contact.firstName} ${contact.lastName}"
        }

        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GuestItemGroupMemberBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return TextViewHolder(binding)
            }
        }
    }
}