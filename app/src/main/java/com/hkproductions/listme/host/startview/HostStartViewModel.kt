package com.hkproductions.listme.host.startview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.host.database.Area
import com.hkproductions.listme.host.database.HostData
import com.hkproductions.listme.host.database.HostDataDao
import com.hkproductions.listme.textToGuestList
import kotlinx.coroutines.launch

class HostStartViewModel(val database: HostDataDao) : ViewModel() {

    val openEntries: LiveData<List<HostData>> = database.getOpenEntries()

    private val _scanResult = MutableLiveData<LongArray>()
    val scanResult: LiveData<LongArray>
        get() = _scanResult

    private val _checkedInAreas = MutableLiveData<Map<Area, List<HostData>>>()
    val checkedInAreas: LiveData<Map<Area, List<HostData>>>
        get() = _checkedInAreas

    init {
        _checkedInAreas.value = mapOf()
        actualizeMap()
    }

    fun actualizeMap() {
        viewModelScope.launch {
            val map = mutableMapOf<Area, List<HostData>>()
            for (area in database.getAllAreasAsList()) {
                map[area] = database.getOpenEntriesInAreaAsList(area.name)
            }
            map[Area(name = "NO_AREA")] = database.getOpenEntriesInAreaAsList("")
            _checkedInAreas.value = map
        }
    }

    fun scannedCode(result: String) {
        viewModelScope.launch {
            try {
                val guestList = textToGuestList(result)
            } catch (e: NoSuchElementException) {
                //TODO ERROR HANDLING Scanned Code are Illegal
            }
        }
    }

    fun checkout(hostDataIds: LongArray) {
        viewModelScope.launch {
            for (id in hostDataIds) {
                val hostData = database.getHostDataById(id)
                hostData.endTimeMilli = System.currentTimeMillis()
                database.updateHostData(hostData)
            }
        }
    }

    /**
     * Method only for DEVELOPERMODE
     * clear host_data_table
     */
    fun clearHostData() {
        viewModelScope.launch {
            database.deleteAllHostData()
        }
    }

}