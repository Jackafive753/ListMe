package com.hkproductions.listme.guest.editview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.guest.database.GuestData
import com.hkproductions.listme.guest.database.GuestDataDao
import com.hkproductions.listme.textToContact
import kotlinx.coroutines.launch

class EditViewViewModel(private val database: GuestDataDao, dataId: Long) : ViewModel() {

    var liveData: MutableLiveData<GuestData> = MutableLiveData()

    init {
        viewModelScope.launch {
            if (dataId == -1L) {
                //create Data
                val contact = GuestData()

                //preload adress of phoneOwner
                val phoneOwner = database.getPhoneOwner()
                phoneOwner?.apply {
                    contact.city = city
                    contact.street = street
                    contact.postalCode = postalCode
                    contact.houseNumber = houseNumber
                }

                //set livedata
                liveData.value = contact
            } else {
                //load data from the database
                liveData.value = database.getDataById(dataId)
            }
        }
    }

    /**
     * insert Data in the database
     * if there is no phoneOwner in the database, the data change to phoneOwner data
     */
    fun insertData() {
        viewModelScope.launch {
            if (database.getPhoneOwner() == null) {
                liveData.value?.phoneOwner = true
            }
            database.insert(liveData.value!!)
        }
    }

    /**
     * get a string and convert it to an contact
     *
     * @param text result of the scan is the string which is converted
     */
    fun scannedCode(text: String) {
        viewModelScope.launch {
            liveData.value = textToContact(text)
        }
    }
}