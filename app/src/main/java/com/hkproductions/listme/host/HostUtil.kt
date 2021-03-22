package com.hkproductions.listme.host

import android.util.Log
import com.hkproductions.listme.Constant.hostDataLifeSpan
import com.hkproductions.listme.host.database.HostDataDao

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