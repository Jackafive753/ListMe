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

/**
 * ViewModel to hold all important data for the fragment
 * @param database the connection to the database on which is worked
 * @param initDataId the id of the contact whose code is displayed first
 */
class CodeViewModel(private val database: GuestDataDao, private val initDataId: Long) :
    ViewModel() {

    /**
     * LiveData of the shown code
     */
    private val _code = MutableLiveData<Bitmap>()
    val code: LiveData<Bitmap>
        get() = _code

    /**
     * LiveData of the shown GuestData
     */
    private val _selected = MutableLiveData<GuestData>()
    val selected: LiveData<GuestData>
        get() = _selected

    /**
     * All GuestData who are contact and can be displayed
     */
    private val _allGuestData = MutableLiveData<List<GuestData>>()
    val allGuestData: LiveData<List<GuestData>>
        get() = _allGuestData

    /**
     * called when this viewModel is created
     * set liveData values
     */
    init {
        viewModelScope.launch {
            _selected.value = database.getDataById(initDataId)
            _code.value = createBitmap(contactToText(selected.value!!))
            _allGuestData.value = database.getAllGuestData()
        }
    }

    /**
     * trigger if a contact button is clicked
     * change values to show the matching code
     *
     * @param dataId Id of the clicked contact
     */
    fun onShowContactCodeClicked(dataId: Long) {
        viewModelScope.launch {
            _selected.value = database.getDataById(dataId)
            _code.value = createBitmap(contactToText(selected.value!!))
        }
    }
}