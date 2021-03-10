package com.hkproductions.listme.host.guestdetailview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.host.database.HostData
import com.hkproductions.listme.host.database.HostDataDao
import kotlinx.coroutines.launch

class GuestDetailViewViewModel(private val database: HostDataDao, private val dataId: Long) :
ViewModel(){
    private val _liveData: MutableLiveData<HostData> = MutableLiveData()
    val liveData : LiveData<HostData>
    get() = _liveData


    init {
        viewModelScope.launch {
            _liveData.value = database.getHostDataById(dataId)
        }
    }
}