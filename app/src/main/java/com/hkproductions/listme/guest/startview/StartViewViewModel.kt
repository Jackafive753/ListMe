package com.hkproductions.listme.guest.startview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.guest.database.GuestData
import com.hkproductions.listme.guest.database.GuestDataDao
import kotlinx.coroutines.launch

class StartViewViewModel(private val datasource: GuestDataDao) : ViewModel() {

    val database = datasource

    private var _phoneOwner = MutableLiveData<GuestData>()
    val phoneOwner: LiveData<GuestData>
        get() = _phoneOwner

    val houseMembers = database.getHouseMembers()

    private val _navigateToDataDetail = MutableLiveData<Long>()
    val navigateToDataDetail: LiveData<Long>
        get() = _navigateToDataDetail

    init {
        viewModelScope.launch {
            _phoneOwner.value = datasource.getPhoneOwner()
        }
    }

    fun onDataClicked(id: Long) {
        _navigateToDataDetail.value = id
    }
}