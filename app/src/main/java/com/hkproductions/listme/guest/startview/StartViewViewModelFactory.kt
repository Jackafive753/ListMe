package com.hkproductions.listme.guest.startview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hkproductions.listme.guest.database.GuestDataDao

class StartViewViewModelFactory(private val datasource: GuestDataDao) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartViewViewModel::class.java)) {
            return StartViewViewModel(datasource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}