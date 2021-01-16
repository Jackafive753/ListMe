package com.hkproductions.listme.host

import com.hkproductions.listme.host.database.HostDataDao
import com.hkproductions.listme.textToGuestList

/**
 * get a string from scan result
 * convert string to guestList
 * and check in guests
 *
 * @param string string from scan result
 * @param database database where the check in happens
 */
suspend fun checkin(string: String, database: HostDataDao): LongArray {

    val guestList = textToGuestList(string)

    val hostDataIds = LongArray(guestList.size)

    for ((index, guest) in guestList.withIndex()) {
        //insertHostData returns given id of the guest
        hostDataIds[index] = database.insertHostData(guest)
    }

    return hostDataIds
}