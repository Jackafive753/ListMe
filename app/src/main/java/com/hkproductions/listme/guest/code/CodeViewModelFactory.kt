package com.hkproductions.listme.guest.code

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hkproductions.listme.guest.database.GuestDataDao

class CodeViewModelFactory(private val datasource: GuestDataDao, private val initDataId: Long) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CodeViewModel::class.java))
            return CodeViewModel(datasource, initDataId) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}