package com.hkproductions.listme.host.scanresult

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.host.database.HostData
import com.hkproductions.listme.host.database.HostDataDao
import kotlinx.coroutines.launch

class ScanResultViewModel(val database:HostDataDao, val hostDataIds:LongArray) : ViewModel(){
    val lData = MutableLiveData<List<HostData>>()
    init {
        viewModelScope.launch {
            val nData = mutableListOf<HostData>()
            for (max in hostDataIds){
                nData.add(database.getHostDataById(max))
            }
            lData.value=nData.toList()
        }
    }
}