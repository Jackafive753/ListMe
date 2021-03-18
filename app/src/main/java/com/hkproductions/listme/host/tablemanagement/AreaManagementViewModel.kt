package com.hkproductions.listme.host.tablemanagement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.host.database.Area
import com.hkproductions.listme.host.database.HostDataDao
import kotlinx.coroutines.launch

class AreaManagementViewModel(val database: HostDataDao) : ViewModel() {

    var areas = database.getAllAreas()

    val nArea = MutableLiveData<String>()

    init {
        viewModelScope.launch {
        }
    }

    fun onAreaDeleteClicked(areaId: Long) {
        viewModelScope.launch {
            database.deleteAreaById(areaId)
            database.getHostDataByArea(areaId).map {
                it.areaName = -1L
                database.updateHostData(it)
            }
        }
    }

    fun addArea() {
        viewModelScope.launch {
            database.insertArea(
                Area(
                    name = nArea.value ?: areas.value?.size.toString()
                )
            )
        }
    }


    fun setNewDesignation() {
        viewModelScope.launch {
            val list = database.getAllAreasAsList()
            for (area in list) {
                database.updateArea(area)
            }
//            areas = database.getAllAreas()
        }
    }

    fun updateAreaName(areaId: Long, name: String) {
        viewModelScope.launch {
            var area = database.getAreaById(areaId)
            area.name = name
            database.updateArea(area)
        }
    }
}

