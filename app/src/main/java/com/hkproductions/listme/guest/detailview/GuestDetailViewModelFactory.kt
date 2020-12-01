package com.hkproductions.listme.guest.detailview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hkproductions.listme.guest.database.GuestDataDao

class GuestDetailViewModelFactory(private val datasource: GuestDataDao, private val dataId: Long) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GuestDetailViewModel::class.java)) {
            return GuestDetailViewModel(datasource, dataId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}