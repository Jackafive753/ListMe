package com.hkproductions.listme.host.guestlist;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hkproductions.listme.guest.database.GuestDataDao;
import com.hkproductions.listme.host.database.HostDataDao;

public class GuestListViewModelFactory implements ViewModelProvider.Factory {
    private HostDataDao database;
    public GuestListViewModelFactory(HostDataDao database){
        this.database = database;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(GuestListViewModel.class)){
            return (T) new GuestListViewModel(database);
        }
        throw new IllegalArgumentException("Unkown ViewModel class");
    }
}
