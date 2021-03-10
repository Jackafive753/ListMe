package com.hkproductions.listme.host.detaillist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.host.database.HostData
import com.hkproductions.listme.host.database.HostDataDao
import kotlinx.coroutines.launch

class DetailListViewModel(val database: HostDataDao, val hostDataId: Long) : ViewModel() {
    val IData = MutableLiveData<List<HostData>?>()
    val IIData = MutableLiveData<List<HostData>?>()


    init {
        viewModelScope.launch {
            val user = database.getHostDataById(hostDataId)
            IData.value =
                database.getContactPersonOne(user.startTimeMilli, user.endTimeMilli, user.areaName)
            IIData.value =
                database.getContactPersonTwo(user.startTimeMilli, user.endTimeMilli, user.areaName)

        }
    }
}