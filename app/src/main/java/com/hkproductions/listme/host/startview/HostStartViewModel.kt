package com.hkproductions.listme.host.startview

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.R
import com.hkproductions.listme.host.checkin
import com.hkproductions.listme.host.database.Area
import com.hkproductions.listme.host.database.HostData
import com.hkproductions.listme.host.database.HostDataDao
import kotlinx.coroutines.launch

class HostStartViewModel(val database: HostDataDao) : ViewModel() {

    val openEntries: LiveData<List<HostData>> = database.getOpenEntries()

    private val _scanResult = MutableLiveData<LongArray>()
    val scanResult: LiveData<LongArray>
        get() = _scanResult

    private val _checkedInAreas = MutableLiveData<Map<Area, List<HostData>>>()
    val checkedInAreas: LiveData<Map<Area, List<HostData>>>
        get() = _checkedInAreas

    private val _navigateToScanResult = MutableLiveData<LongArray>()
    val navigateToScanResult: LiveData<LongArray>
        get() = _navigateToScanResult

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

    fun scannedCode(result: String, context: Context) {
        viewModelScope.launch {
            try {
                _navigateToScanResult.value = checkin(result, database)
            } catch (e: NoSuchElementException) {
                Toast.makeText(context, R.string.scan_failure_error_text, Toast.LENGTH_LONG).show()
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

    fun refreshDatabase() {
        viewModelScope.launch {
            com.hkproductions.listme.host.refreshDatabase(database)
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