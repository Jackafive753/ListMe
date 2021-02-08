package com.hkproductions.listme.host.scanresult

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkproductions.listme.R
import com.hkproductions.listme.host.checkinout
import com.hkproductions.listme.host.database.HostData
import com.hkproductions.listme.host.database.HostDataDao
import kotlinx.coroutines.launch

class ScanResultViewModel(val database:HostDataDao, val hostDataIds:LongArray) : ViewModel() {
    val lData = MutableLiveData<List<HostData>>()

    init {
        viewModelScope.launch {
            makingList(hostDataIds)
        }
    }

    fun scannedCode(bao: String, context: Context) {
        viewModelScope.launch {
            try {
                var erg = checkinout(bao, database)
                makingList(erg)
            } catch (e: NoSuchElementException) {
                Toast.makeText(context, R.string.scan_failure_error_text, Toast.LENGTH_LONG).show()
            }
        }
    }

    suspend fun makingList(boa: LongArray) {
        val nData = mutableListOf<HostData>()
        for (max in boa) {
            nData.add(database.getHostDataById(max))
        }
        lData.value = nData.toList()
    }
}