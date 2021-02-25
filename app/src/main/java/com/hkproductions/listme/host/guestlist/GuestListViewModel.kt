package com.hkproductions.listme.host.guestlist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.host.database.HostData
import com.hkproductions.listme.host.database.HostDataDao
import kotlinx.coroutines.launch
import java.util.*

class GuestListViewModel(private val database: HostDataDao) : ViewModel() {
    @JvmField
    var data =
        MutableLiveData<List<HostData>?>()

    @JvmField
    var liveName = MutableLiveData<String>()

    @JvmField
    var liveDate = MutableLiveData<Long>()

    @JvmField
    var liveStartTime = MutableLiveData<Long>()

    @JvmField
    var liveEndTime = MutableLiveData<Long>()

    fun alterList() {
        Log.i("GuestListAltering", liveDate.value.toString())

        viewModelScope.launch {
            data.setValue(
                when (liveName.value.isNullOrBlank()) {
                    true -> database.getEntriesByTime(
                        liveStartTime.value!! + liveDate.value!!,
                        liveEndTime.value!! + liveDate.value!!
                    )
                    false -> database.getEntriesByNameAndTime(
                        liveName.value!!,
                        liveStartTime.value!!.plus(liveDate.value!!),
                        liveEndTime.value!!.plus(liveDate.value!!)
                    )
                }
            )

        }
        Log.i("DebugGuestListNoElement", "name: " + liveName.value)
        Log.i(
            "DebugGuestListNoElement",
            "startTime: " + (liveStartTime.value?.plus(liveDate.value!!))
        )
        Log.i("DebugGuestListNoElement", "endTime: " + (liveEndTime.value?.plus(liveDate.value!!)))
        Log.i(
            "DebugGuestListNoElement",
            "data: " + if (data.value != null) data.value.toString() else ""
        )
    }

    init {
        liveDate.value = Calendar.getInstance().timeInMillis
        liveStartTime.value = 0L
        liveEndTime.value = 0L
        alterList()
    }
}