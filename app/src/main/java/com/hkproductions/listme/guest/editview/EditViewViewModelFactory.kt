package com.hkproductions.listme.guest.editview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hkproductions.listme.guest.database.GuestDataDao

class EditViewViewModelFactory(private val datasource: GuestDataDao, private val dataId: Long) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditViewViewModel::class.java)) {
            return EditViewViewModel(datasource, dataId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}