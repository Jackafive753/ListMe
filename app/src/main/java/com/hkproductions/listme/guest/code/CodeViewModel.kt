package com.hkproductions.listme.guest.code

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.contactToText
import com.hkproductions.listme.createBitmap
import com.hkproductions.listme.guest.database.GuestData
import com.hkproductions.listme.guest.database.GuestDataDao
import kotlinx.coroutines.launch

class CodeViewModel(private val database: GuestDataDao, private val initDataId: Long) :
    ViewModel() {

    private val _code = MutableLiveData<Bitmap>()
    val code: LiveData<Bitmap>
        get() = _code

    private val _selected = MutableLiveData<GuestData>()
    val selected: LiveData<GuestData>
        get() = _selected

    private val _allGuestData = MutableLiveData<List<GuestData>>()
    val allGuestData: LiveData<List<GuestData>>
        get() = _allGuestData

    init {

        viewModelScope.launch {
            _selected.value = database.getDataById(initDataId)
            _code.value = createBitmap(contactToText(selected.value!!))
            _allGuestData.value = database.getAllGuestData()
        }
    }

    fun onShowContactCodeClicked(dataId: Long) {
        viewModelScope.launch {
            _selected.value = database.getDataById(dataId)
            _code.value = createBitmap(contactToText(selected.value!!))
        }
    }
}