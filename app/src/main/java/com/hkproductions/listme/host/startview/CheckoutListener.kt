package com.hkproductions.listme.host.startview

import com.hkproductions.listme.host.database.HostData

class CheckoutListener(val clickListener: (hostDataIds: LongArray) -> Unit) {
    fun onClick(datas: List<HostData>) {
        val ids = LongArray(datas.size)
        for ((index, data) in datas.withIndex()) {
            ids[index] = data.hostDataId
        }
        clickListener(ids)
    }
}