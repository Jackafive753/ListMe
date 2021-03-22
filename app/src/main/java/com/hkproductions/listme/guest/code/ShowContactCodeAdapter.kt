package com.hkproductions.listme.guest.code

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkproductions.listme.databinding.GuestItemShowContactCodeBinding
import com.hkproductions.listme.guest.code.ShowContactCodeAdapter.ShowContactCodeViewHolder
import com.hkproductions.listme.guest.database.GuestData

/**
 * Adapter for the RecyclerView in GuestStartView
 * hold all contacts
 * @param contactCodeListener clickListener which is triggered when on a contact is clicked
 */
class ShowContactCodeAdapter(
    private val contactCodeListener: ShowContactCodeListener
) : RecyclerView.Adapter<ShowContactCodeViewHolder>() {

    /**
     * list of all contacts
     */
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

        /**
         * method to bind important data to the viewholder and refresh the layout
         * @param item contact who is shown in this viewHolder
         * @param clickListener clickListener which is triggered when this viewHolder is clicked
         */
        @SuppressLint("SetTextI18n")
        fun bind(item: GuestData, clickListener: ShowContactCodeListener) {
            binding.buttonCodeContact.text = "${item.firstName}\n${item.lastName}"
            binding.contact = item
            binding.clickListener = clickListener
        }

        companion object {
            /**
             * method to create a new ViewHolder and load the layout
             */
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

/**
 * Listener which called when a contact is clicked
 * @param clickListener method that happen if clicked
 */
class ShowContactCodeListener(val clickListener: (guestId: Long) -> Unit) {
    fun onClick(member: GuestData) = clickListener(member.guestDataId)
}
