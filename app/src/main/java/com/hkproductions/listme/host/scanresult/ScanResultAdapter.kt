package com.hkproductions.listme.host.scanresult

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkproductions.listme.R
import com.hkproductions.listme.databinding.HostItemScanresultBinding
import com.hkproductions.listme.host.database.HostData

class ScanResultAdapter : RecyclerView.Adapter<ScanResultAdapter.ScanResultViewHolder>() {
    var data = listOf<HostData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanResultViewHolder {
        return ScanResultViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ScanResultViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    class ScanResultViewHolder(
        private val binding: HostItemScanresultBinding,
        private val resource: Resources
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ScanResultViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HostItemScanresultBinding.inflate(layoutInflater, parent, false)
                val resource = parent.resources
                return ScanResultViewHolder(binding, resource)
            }
        }

        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bind(item: HostData) {
            binding.textViewHostScanresultName.text = item.firstName + " " + item.lastName
            binding.textViewHostScanresultCityPostalCode.text = "${item.postalCode} ${item.city}"
            binding.textViewHostScanresultStreetHousenumber.text =
                item.street + " " + item.houseNumber
            binding.textViewPhoneNumber.text = item.phoneNumber
            binding.textViewHostScanresultStatus.text = when (item.endTimeMilli) {
                -1L -> resource.getString(R.string.checked_in)
                else -> resource.getString(R.string.checked_out)
            }
        }
    }


}