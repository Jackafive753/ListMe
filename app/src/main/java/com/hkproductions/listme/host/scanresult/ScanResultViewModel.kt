package com.hkproductions.listme.host.scanresult

import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.R
import com.hkproductions.listme.host.checkinout
import com.hkproductions.listme.host.database.HostData
import com.hkproductions.listme.host.database.HostDataDao
import kotlinx.coroutines.launch

class ScanResultViewModel(val database: HostDataDao, val hostDataIds: LongArray) : ViewModel() {
    val lData = MutableLiveData<List<HostData>>()
    val areaLi = database.getAllAreas()

    init {
        viewModelScope.launch {
            makingList(hostDataIds)
        }
    }

    fun saveArea(pos: Int, context: Context, resources: Resources) {
        viewModelScope.launch {
            val area = if (pos == -1) {
                null
            } else {
                areaLi.value?.get(pos)!!
            }
            lData.value?.map {
                it.areaName = area?.areaId ?: -1
                database.updateHostData(it)
            }
            Toast.makeText(
                context,
                resources.getString(
                    R.string.scanResultToastMes,
                    area ?: resources.getString(R.string.scannedEntrys_noArea)
                ),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun scannedCode(bao: String, context: Context, areaPosition: Int, resources: Resources) {
        viewModelScope.launch {
            try {
                val erg = checkinout(bao, database)
                makingList(erg)
                saveArea(areaPosition, context, resources)
            } catch (e: NoSuchElementException) {
                Toast.makeText(context, R.string.scan_failure_error_text, Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun makingList(boa: LongArray) {
        val nData = mutableListOf<HostData>()
        for (max in boa) {
            nData.add(database.getHostDataById(max))
        }
        lData.value = nData.toList()
    }
}