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

/**
 * delete all hostData entries that older than dataLifeSpan
 *
 * @param database database to actualize
 */
suspend fun refreshDatabase(database: HostDataDao) {
    val hostDatas = database.getAllEntriesAsList()
    val currentTime = System.currentTimeMillis()

    //2.5 weeks = 1,512,000,000 milliseconds
    //2 weeks = 1,209,600,000 milliseconds
    val dataLifeSpan = 1512000000

    for (data in hostDatas) {
        //if data older then dataLifeSpan then delete data
        if (data.endTimeMilli < currentTime - dataLifeSpan) {
            database.deleteHostData(data)
        }
    }
}