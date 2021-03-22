package com.hkproductions.listme.host.scanresult

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.R
import com.hkproductions.listme.host.database.Area
import com.hkproductions.listme.host.database.HostData
import com.hkproductions.listme.host.database.HostDataDao
import com.hkproductions.listme.textToGuestList
import kotlinx.coroutines.launch

class ScanResultViewModel(
    val database: HostDataDao,
    private val context: Context,
    hostDatasAsString: String,
    areaId: Long
) : ViewModel() {
    val lData = MutableLiveData<List<HostData>>(listOf())
    val areaLi = database.getAllAreas()
    val givenArea = database.getLiveAreaById(areaId)
    val selectedArea = MutableLiveData<Area?>(null)

    init {
        scannedCode(hostDatasAsString)
    }

    fun scannedCode(string: String) {
        viewModelScope.launch {
            try {
                val list = textToGuestList(string)
                list.map {
                    if (database.getHostDataByAttributesAndCheckedIn(
                            it.firstName,
                            it.lastName,
                            it.street,
                            it.houseNumber,
                            it.postalCode,
                            it.city,
                            it.phoneNumber
                        ) != null
                    ) {
                        it.endTimeMilli = 0
                    }
                }
                lData.value = list
            } catch (e: NoSuchElementException) {
                Toast.makeText(context, R.string.scan_failure_error_text, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun save() {
        viewModelScope.launch {
            lData.value?.map {
                //check if guest is check in
                val helpGuest = database.getHostDataByAttributesAndCheckedIn(
                    it.firstName,
                    it.lastName,
                    it.street,
                    it.houseNumber,
                    it.postalCode,
                    it.city,
                    it.phoneNumber
                )
                if (helpGuest != null) {
                    //check out
                    helpGuest.endTimeMilli = System.currentTimeMillis()
                    database.updateHostData(helpGuest)
                } else {
                    //check in
                    it.areaName = selectedArea.value?.areaId ?: -1L
                    database.insertHostData(it)
                }
            }
        }
    }
}