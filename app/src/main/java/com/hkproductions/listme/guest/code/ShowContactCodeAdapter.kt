package com.hkproductions.listme.guest.code

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkproductions.listme.databinding.GuestItemShowContactCodeBinding
import com.hkproductions.listme.guest.code.ShowContactCodeAdapter.ShowContactCodeViewHolder
import com.hkproductions.listme.guest.database.GuestData

class ShowContactCodeAdapter(
    private val contactCodeListener: ShowContactCodeListener
) : RecyclerView.Adapter<ShowContactCodeViewHolder>() {

    var data = listOf<GuestData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowContactCodeViewHolder {
        return ShowContactCodeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ShowContactCodeViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, contactCodeListener)
    }

    override fun getItemCount() = data.size

    class ShowContactCodeViewHolder private constructor(private val binding: GuestItemShowContactCodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: GuestData, clickListener: ShowContactCodeListener) {
            binding.buttonCodeContact.text = "${item.firstName}\n${item.lastName}"
            binding.contact = item
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ShowContactCodeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GuestItemShowContactCodeBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return ShowContactCodeViewHolder(binding)
            }
        }
    }

}

class ShowContactCodeListener(val clickListener: (guestId: Long) -> Unit) {
    fun onClick(member: GuestData) = clickListener(member.guestDataId)
}
