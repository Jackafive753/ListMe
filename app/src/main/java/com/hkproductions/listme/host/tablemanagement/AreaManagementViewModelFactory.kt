package com.hkproductions.listme.host.tablemanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hkproductions.listme.host.database.HostDataDao

class AreaManagementViewModelFactory(val database: HostDataDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AreaManagementViewModel::class.java)) {
            return AreaManagementViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}