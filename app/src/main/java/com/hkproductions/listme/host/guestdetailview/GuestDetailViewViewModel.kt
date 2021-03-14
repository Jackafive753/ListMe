package com.hkproductions.listme.host.guestdetailview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.host.database.Area
import com.hkproductions.listme.host.database.HostData
import com.hkproductions.listme.host.database.HostDataDao
import kotlinx.coroutines.launch

class GuestDetailViewViewModel(private val database: HostDataDao, private val dataId: Long) :
ViewModel() {
    private val _liveData: MutableLiveData<HostData> = MutableLiveData()
    val liveData: LiveData<HostData>
        get() = _liveData

    private val _area: MutableLiveData<Area?> = MutableLiveData()
    val area: LiveData<Area?>
        get() = _area

    init {
        viewModelScope.launch {
            _liveData.value = database.getHostDataById(dataId)
            _area.value = database.getAreaById(liveData.value!!.areaName)
        }
    }
}