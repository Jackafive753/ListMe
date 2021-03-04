package com.hkproductions.listme.host.scanresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hkproductions.listme.host.database.HostDataDao

@Suppress("UNCHECKED_CAST")
class ScanResultViewModelFactory(val database:HostDataDao, val hostDataIds:LongArray) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScanResultViewModel::class.java)) {
            return ScanResultViewModel(database, hostDataIds) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}