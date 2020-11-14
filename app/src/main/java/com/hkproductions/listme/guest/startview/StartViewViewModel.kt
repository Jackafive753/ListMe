package com.hkproductions.listme.guest.startview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkproductions.listme.guest.database.GuestData
import com.hkproductions.listme.guest.database.GuestDataDao

class StartViewViewModel(private val datasource: GuestDataDao) : ViewModel() {

    private var _phoneOwner = MutableLiveData<GuestData>()
    val phoneOwner: LiveData<GuestData>
        get() = _phoneOwner

    init {
        _phoneOwner = datasource.getPhoneOwner()
    }
}