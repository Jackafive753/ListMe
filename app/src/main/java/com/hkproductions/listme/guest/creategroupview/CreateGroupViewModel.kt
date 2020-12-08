package com.hkproductions.listme.guest.creategroupview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.guest.database.GuestData
import com.hkproductions.listme.guest.database.GuestDataDao
import kotlinx.coroutines.launch

class CreateGroupViewModel(val database: GuestDataDao) : ViewModel() {

    /**
     * LiveData<List<GuestData>> of all Contacts
     */
    val contacts = database.getContacts()

    /**
     * LiveData<GuestData> with phoneOwner
     */
    private var _phoneOwner = MutableLiveData<GuestData>()
    val phoneOwner: LiveData<GuestData>
        get() = _phoneOwner

    init {
        viewModelScope.launch {
            _phoneOwner.value = database.getPhoneOwner()
        }
    }
}