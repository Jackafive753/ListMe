package com.hkproductions.listme.host

import android.util.Log
import com.hkproductions.listme.Constant.hostDataLifeSpan
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
suspend fun checkinout(string: String, database: HostDataDao): LongArray {

    val guestList = textToGuestList(string)

    val hostDataIds = LongArray(guestList.size)

    for ((index, guest) in guestList.withIndex()) {
        val helpGuest = database.getHostDataByAttributesAndCheckedIn(
            guest.firstName,
            guest.lastName,
            guest.street,
            guest.houseNumber,
            guest.postalCode,
            guest.city,
            guest.phoneNumber
        )
        if (helpGuest != null) {
            helpGuest.endTimeMilli = System.currentTimeMillis()
            database.updateHostData(helpGuest)
            hostDataIds[index] = helpGuest.hostDataId
        } else {
            //insertHostData returns given id of the guest
            hostDataIds[index] = database.insertHostData(guest)
        }
    }

    return hostDataIds
}

/**
 * delete all hostData entries that older than dataLifeSpan
 *
 * @param database database to actualize
 */
suspend fun refreshDatabase(database: HostDataDao) {
    Log.i("Refresh", "RefreshDatabase")
    val hostDatas = database.getAllEntriesAsList()
    val currentTime = System.currentTimeMillis()

    for (data in hostDatas) {
        //if data older then dataLifeSpan then delete data
        if (data.endTimeMilli < currentTime - hostDataLifeSpan && data.endTimeMilli > -1) {
            database.deleteHostData(data)
        }
    }
}