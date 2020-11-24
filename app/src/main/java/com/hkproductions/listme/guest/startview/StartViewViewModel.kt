package com.hkproductions.listme.guest.startview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.guest.database.GuestData
import com.hkproductions.listme.guest.database.GuestDataDao
import kotlinx.coroutines.launch

class StartViewViewModel(private val database: GuestDataDao) : ViewModel() {

    //live data of the phone owner
    private var _phoneOwner = MutableLiveData<GuestData>()
    val phoneOwner: LiveData<GuestData>
        get() = _phoneOwner

    //list of all house members
    val houseMembers = database.getContacts()

    //live data holds id of a house_member to be navigated to
    private val _navigateToDataDetail = MutableLiveData<Long>()
    val navigateToDataDetail: LiveData<Long>
        get() = _navigateToDataDetail

    private val _navigateToCreateMember = MutableLiveData<Boolean>()
    val navigateToCreateMember: LiveData<Boolean>
        get() = _navigateToCreateMember

    init {
        viewModelScope.launch {
            _phoneOwner.value = database.getPhoneOwner()
        }
    }

    fun onMemberDetailClicked() {
        _navigateToDataDetail.value = null
    }

    /**
     * Trigger if an house_member or the addButton on the startview is clicked
     * @param id id of the house_member
     */
    fun onMemberClicked(id: Long) {
        _navigateToDataDetail.value = id
    }

    fun onCreateMemberClicked() {
        _navigateToCreateMember.value = true
    }

    fun onMemberCreateNavigated() {
        _navigateToCreateMember.value = false
    }
}