package com.hkproductions.listme.guest.detailview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.guest.database.GuestData
import com.hkproductions.listme.guest.database.GuestDataDao
import kotlinx.coroutines.launch

class GuestDetailViewModel(private val database: GuestDataDao, private val dataId: Long) :
    ViewModel() {

    private val _liveData: MutableLiveData<GuestData> = MutableLiveData()
    val liveData: LiveData<GuestData>
        get() = _liveData

    init {
        viewModelScope.launch {
            _liveData.value = database.getDataById(dataId)
        }
    }

}