package com.hkproductions.listme.host.detaillist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.host.database.HostData
import com.hkproductions.listme.host.database.HostDataDao
import kotlinx.coroutines.launch

class DetailListViewModel(val database: HostDataDao, val hostDataId: Long) : ViewModel() {
    val dataOne = MutableLiveData<List<HostData>?>()
    val dataTwo = MutableLiveData<List<HostData>?>()


    init {
        viewModelScope.launch {
            val user = database.getHostDataById(hostDataId)
            dataOne.value =
                database.getContactPersonOne(
                    user.startTimeMilli,
                    user.endTimeMilli,
                    user.areaName,
                    user.hostDataId
                )
            dataTwo.value =
                database.getContactPersonTwo(user.startTimeMilli, user.endTimeMilli, user.areaName)

        }
    }
}