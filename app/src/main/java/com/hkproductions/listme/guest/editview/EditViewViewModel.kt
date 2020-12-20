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
     *
     * @param data data to insert in database
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
     * get data to update from database by id and copy data from edited
     *
     * @param id id of the data which must be updated
     * @param edited edited data
     */
    fun updateData() {
        viewModelScope.launch {
            database.update(liveData.value!!)
        }
    }

    fun scannedCode(text: String) {
        viewModelScope.launch {
            liveData.value = textToContact(text)
        }
    }
}