package com.hkproductions.listme.host.guestlist;

import android.text.format.Time;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hkproductions.listme.host.database.HostData;
import com.hkproductions.listme.host.database.HostDataDao;

import java.util.Date;
import java.util.List;

public class GuestListViewModel extends ViewModel {
    private HostDataDao database;
    public LiveData<List<HostData>> data;
    public MutableLiveData<String> liveName = new MutableLiveData<String>();
    public MutableLiveData<Long> liveDate = new MutableLiveData<Long>();
    public MutableLiveData<Long> liveStartTime = new MutableLiveData<Long>();
    public MutableLiveData<Long> liveEndTime= new MutableLiveData<Long>();
    public GuestListViewModel(HostDataDao database){
        this.database = database;
//        data = database.getEntriesByNameAndTime();
    }
    public void alterList(){
        data = database.getEntriesByNameAndTime(liveName.getValue(),
                liveDate.getValue() + liveStartTime.getValue(),
                liveDate.getValue() + liveEndTime.getValue());
        Log.i("GuestListAltering", String.valueOf(liveDate.getValue()));
    }
}
