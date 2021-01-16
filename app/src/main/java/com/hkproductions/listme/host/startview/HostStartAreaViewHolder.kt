package com.hkproductions.listme.host.startview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkproductions.listme.databinding.HostItemStartAreaBinding
import com.hkproductions.listme.host.database.Area
import com.hkproductions.listme.host.database.HostData

class HostStartAreaViewHolder(private val binding: HostItemStartAreaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(area: Area, list: List<HostData>, clickListener: CheckoutListener) {
        //Set Label with name of the area
        binding.textViewStartAreaItemAreaName.text = "${area.designation} ${area.name}"

        //Set List with guest in this area
        val adapter = HostStartNameAdapter(clickListener)
        adapter.data = list
        binding.recyclerViewStartAreaItem.adapter = adapter

        //Set ClickListener to Check all guest in this area out
        binding.buttonStartAreaItemCheckAllOut.setOnClickListener { clickListener.onClick(list) }

        //TODO clickListener to expand or collapse
    }

    companion object {
        fun from(parent: ViewGroup): HostStartAreaViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HostItemStartAreaBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            return HostStartAreaViewHolder(binding)
        }
    }
}