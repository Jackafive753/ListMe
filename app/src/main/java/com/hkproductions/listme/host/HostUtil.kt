package com.hkproductions.listme.host

import com.hkproductions.listme.host.database.HostData

fun checkout(data: HostData) {
    data.endTimeMilli = System.currentTimeMillis()
}

fun checkout(list: List<HostData>) {
    for (data in list) {
        checkout(data)
    }
}