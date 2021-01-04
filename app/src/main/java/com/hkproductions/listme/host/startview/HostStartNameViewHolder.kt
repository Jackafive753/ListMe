package com.hkproductions.listme.host.startview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkproductions.listme.databinding.HostItemStartNameBinding
import com.hkproductions.listme.host.checkout
import com.hkproductions.listme.host.database.HostData

class HostStartNameViewHolder(private val binding: HostItemStartNameBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    public fun bind(item: HostData) {
        binding.textViewStartNameItemName.text = "$item.firstName $item.lastname"
        binding.buttonStartNameItemCheckout.setOnClickListener {
            checkout(item)
        }
    }

    companion object {
        fun from(parent: ViewGroup): HostStartNameViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HostItemStartNameBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            return HostStartNameViewHolder(binding)
        }
    }

}