package com.hkproductions.listme.host.detaillist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hkproductions.listme.host.database.HostDataDao

@Suppress("UNCHECKED_CAST")
class DetailListViewModelFactory(val database: HostDataDao, val hostDataId: Long) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailListViewModel::class.java)) {
            return DetailListViewModel(database, hostDataId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}