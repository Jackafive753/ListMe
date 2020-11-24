package com.hkproductions.listme.guest.editview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.guest.database.GuestData
import com.hkproductions.listme.guest.database.GuestDataDao
import kotlinx.coroutines.launch

class EditViewViewModel(private val database: GuestDataDao, dataId: Long) : ViewModel() {

    private var _liveData: MutableLiveData<GuestData> = MutableLiveData()
    val liveData: LiveData<GuestData>
        get() = _liveData

    init {
        viewModelScope.launch {
            var data = database.getDataById(dataId)
            if (data == null) {
                data = GuestData()
            }
            _liveData.value = data
        }
    }

    /**
     * insert Data in the database
     * if there is no phoneOwner in the database, the data change to phoneOwner data
     *
     * @param data data to insert in database
     */
    fun insertData(data: GuestData) {
        viewModelScope.launch {
            if (database.getPhoneOwner() == null) {
                data.phoneOwner = true
            }
            database.insert(data)
        }
    }

    /**
     * get data to update from database by id and copy data from edited
     *
     * @param id id of the data which must be updated
     * @param edited edited data
     */
    fun updateData(id: Long, edited: GuestData) {
        Log.i("info", "update")
        viewModelScope.launch {
            val old = database.getDataById(id)
            old.copyData(edited)
            database.update(old)
        }
    }
}