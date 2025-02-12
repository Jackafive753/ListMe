package com.hkproductions.listme.guest.codegroup

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.contactListToText
import com.hkproductions.listme.createBitmap
import com.hkproductions.listme.guest.database.GuestData
import com.hkproductions.listme.guest.database.GuestDataDao
import kotlinx.coroutines.launch

/**
 * ViewModel for all important datas of the fragment.
 * @param database the connection to the database on which is worked
 * @param dataIds ids of all contacts who are show in the code
 */
class CodeGroupViewModel(private val database: GuestDataDao, private val dataIds: LongArray) :
    ViewModel() {

    /**
     * contacts who are show in the code
     */
    private val _groupMembers = MutableLiveData<List<GuestData>>()
    val groupMembers: LiveData<List<GuestData>>
        get() = _groupMembers

    /**
     * LiveData of the shown code
     */
    private val _code = MutableLiveData<Bitmap>()
    val code: LiveData<Bitmap>
        get() = _code

    /**
     * prepare all LiveData
     */
    init {
        viewModelScope.launch {
            val list = mutableListOf<GuestData>()
            for (id in dataIds) {
                list.add(database.getDataById(id)!!)
            }
            _groupMembers.value = list

            val contactString = contactListToText(list)

            _code.value = createBitmap(contactString)
        }
    }

}