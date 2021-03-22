package com.hkproductions.listme.guest.codegroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hkproductions.listme.guest.database.GuestDataDao

class CodeGroupViewModelFactory(val datasource: GuestDataDao, val dataIds: LongArray) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CodeGroupViewModel::class.java))
            return CodeGroupViewModel(datasource, dataIds) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}