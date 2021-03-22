package com.hkproductions.listme.host.startview

import com.hkproductions.listme.host.database.HostData

class CheckoutListener(val clickListener: (hostDataIds: LongArray, isAreaDialog: Boolean) -> Unit) {
    fun onClick(datas: List<HostData>, isAreaDialog: Boolean) {
        val ids = LongArray(datas.size)
        for ((index, data) in datas.withIndex()) {
            ids[index] = data.hostDataId
        }
        clickListener(ids, isAreaDialog)
    }
}