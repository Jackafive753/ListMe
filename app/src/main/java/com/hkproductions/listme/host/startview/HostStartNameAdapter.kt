package com.hkproductions.listme.host.startview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkproductions.listme.host.database.HostData

class HostStartNameAdapter : RecyclerView.Adapter<HostStartNameViewHolder>() {

    var data = listOf<HostData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HostStartNameViewHolder {
        return HostStartNameViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HostStartNameViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount() = data.size
}