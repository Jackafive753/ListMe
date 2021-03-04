package com.hkproductions.listme.host.tablemanagement

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.host.database.Area
import com.hkproductions.listme.host.database.HostDataDao
import kotlinx.coroutines.launch

class AreaManagementViewModel(val database: HostDataDao) : ViewModel() {

    var areas = database.getAllAreas()

    var designation = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            //Not sensless null check, if database found no item it returns null
            @Suppress("SENSELESS_NULL_IN_WHEN")
            designation.value = when (val string: String = database.getAreaDesignation()) {
                null -> ""
                else -> string
            }
        }
    }

    fun onAreaDeleteClicked(areaId: Long) {
        viewModelScope.launch {
            database.deleteAreaById(areaId)
        }
    }

    fun addArea() {
        viewModelScope.launch {
            database.insertArea(
                Area(
                    name = (areas.value?.size?.plus(1)).toString(),
                    designation = designation.value!!
                )
            )
        }
    }


    fun setNewDesignation() {
        viewModelScope.launch {
            val list = database.getAllAreasAsList()
            for (area in list) {
                area.designation = designation.value!!
                Log.i("Listme", area.toString())
                Log.i("Listme", "designation: ${designation.value!!}")
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

