package com.hkproductions.listme.host.startview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hkproductions.listme.host.database.HostDataDao

class HostStartViewModelFactory(val database: HostDataDao) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HostStartViewModel::class.java)) {
            return HostStartViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}