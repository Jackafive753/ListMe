package com.hkproductions.listme.host.detaillist

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.HostItemDetaillistHeaderBinding
import com.hkproductions.listme.databinding.HostItemDetaillistPersonsBinding
import com.hkproductions.listme.host.database.HostData
import java.text.SimpleDateFormat

const val HEADER_ITEM: Int = 0
const val PERSON_ITEM: Int = 1

class DetailListAdapter(val resource: Resources) :
    ListAdapter<DetailItem, RecyclerView.ViewHolder>(DetailDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_ITEM -> ViewHolderHeader.from(parent)
            PERSON_ITEM -> ViewHolderPerson.from(parent)
            else -> throw ClassCastException("unknown Viewtype: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderPerson -> {
                val item = getItem(position) as DetailItem.PersonItem
                holder.bind(item.person)
            }
            is ViewHolderHeader -> {
                val item = getItem(position) as DetailItem.HeaderItem
                holder.bind(item.string)
            }

        }
    }

    fun createList(personOne: List<HostData>?, personTwo: List<HostData>?) {
        val nList = mutableListOf<DetailItem>()
        if (!personOne.isNullOrEmpty()) {
            nList.add(
                DetailItem.HeaderItem(
                    -1,
                    resource.getString(R.string.contact_person_one_text)
                )
            )
            personOne.map {
                nList.add(DetailItem.PersonItem(it))
            }
        }
        if (!personTwo.isNullOrEmpty()) {
            nList.add(
                DetailItem.HeaderItem(
                    -2,
                    resource.getString(R.string.contact_person_two_text)
                )
            )
            personTwo.map {
                nList.add(DetailItem.PersonItem(it))
            }
        }
        submitList(nList)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DetailItem.HeaderItem -> HEADER_ITEM
            is DetailItem.PersonItem -> PERSON_ITEM
        }
    }

    class ViewHolderPerson(val binding: HostItemDetaillistPersonsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolderPerson {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    HostItemDetaillistPersonsBinding.inflate(layoutInflater, parent, false)
                return ViewHolderPerson(binding)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: HostData) {
            binding.textViewHostDetailListName.text = item.firstName + " " + item.lastName
            binding.textViewHostDetailListCityPostalCode.text = "${item.postalCode}, ${item.city}"
            binding.textViewHostDetailListStreetHousenumber.text =
                item.street + " " + item.houseNumber
            binding.textViewHostDetailListPhoneNumber.text = item.phoneNumber
            val dfStart = SimpleDateFormat("dd.MM.yyyy HH:mm")
            val dfEnd = SimpleDateFormat("HH:mm")
            val checkInString = dfStart.format(item.startTimeMilli)
            var checkOutString = if (item.endTimeMilli == -1L) {
                ""
            } else {
                dfEnd.format(item.endTimeMilli)
            }
            binding.textViewHostDetailListDate.text = "$checkInString - $checkOutString"
        }
    }
}

class ViewHolderHeader(val binding: HostItemDetaillistHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ViewHolderHeader {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HostItemDetaillistHeaderBinding.inflate(layoutInflater, parent, false)
            return ViewHolderHeader(binding)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: String) {
        binding.textviewHostDetaillistLabelHeader.text = item
    }
}

sealed class DetailItem {
    abstract val id: Long

    data class PersonItem(val person: HostData) : DetailItem() {
        override val id: Long = person.hostDataId
    }

    data class HeaderItem(val idItem: Long, val string: String) : DetailItem() {
        override val id: Long = idItem
    }

}

class DetailDiffCallback : DiffUtil.ItemCallback<DetailItem>() {
    override fun areItemsTheSame(oldItem: DetailItem, newItem: DetailItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DetailItem, newItem: DetailItem): Boolean {
        return oldItem.id == newItem.id
    }

}