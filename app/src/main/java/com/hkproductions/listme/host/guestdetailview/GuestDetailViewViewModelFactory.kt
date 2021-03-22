package com.hkproductions.listme.host.guestdetailview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hkproductions.listme.host.database.HostDataDao
import java.lang.IllegalArgumentException

class GuestDetailViewViewModelFactory(private val datasource : HostDataDao, private val dataId: Long) :
    ViewModelProvider.Factory {
    @Suppress( "UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GuestDetailViewViewModel::class.java)){
            return GuestDetailViewViewModel(datasource,dataId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}