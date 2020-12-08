package com.hkproductions.listme.guest.creategroupview

import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkproductions.listme.databinding.GuestContactCheckboxItemBinding
import com.hkproductions.listme.guest.creategroupview.CheckContactAdapter.CheckBoxViewHolder
import com.hkproductions.listme.guest.database.GuestData

class CheckContactAdapter :
    RecyclerView.Adapter<CheckBoxViewHolder>() {

    /**
     * data hold all contact in a list
     */
    var data = listOf<GuestData>()
        set(value) {
            Log.i("Listme", value.size.toString())
            field = value
            notifyDataSetChanged()
        }

    /**
     * hold <position, checked> (<Integer, Boolean>)
     * access to checked state of each item
     */
    val checkedState = SparseBooleanArray(data.size)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckBoxViewHolder {
        return CheckBoxViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CheckBoxViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, checkedState, position)
    }

    override fun getItemCount() = data.size

    /**
     * Holder for a name of a contact in a checkbox.
     * if checked the contact will added to a code group
     */
    class CheckBoxViewHolder
    private constructor(private val binding: GuestContactCheckboxItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GuestData, checkedState: SparseBooleanArray, position: Int) {
            binding.contact = item
            binding.checkboxItemContact.setOnClickListener {
                checkedState.put(position, binding.checkboxItemContact.isChecked)
            }
        }

        companion object {
            fun from(parent: ViewGroup): CheckBoxViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GuestContactCheckboxItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return CheckBoxViewHolder(binding)
            }
        }
    }

}