package com.hkproductions.listme.guest.creategroupview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hkproductions.listme.guest.database.GuestDataDao

class CreateGroupViewModelFactory(private val datasource: GuestDataDao) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateGroupViewModel::class.java))
            return CreateGroupViewModel(datasource) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}