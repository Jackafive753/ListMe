package com.hkproductions.listme.host.startview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.host.database.Area
import com.hkproductions.listme.host.database.HostData
import com.hkproductions.listme.host.database.HostDataDao
import kotlinx.coroutines.launch

class HostStartViewModel(val database: HostDataDao) : ViewModel() {

    val openEntries: LiveData<List<HostData>> = database.getOpenEntries()

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
            for (area in database.getAllAreas()) {
                map.put(area, database.getOpenEntriesInArea(area.name))
            }
            map.put(Area(name = "NO_AREA"), database.getOpenEntriesInArea(""))
            _checkedInAreas.value = map
        }
    }

}