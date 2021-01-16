package com.hkproductions.listme.host.guestlist;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.hkproductions.listme.R;
import com.hkproductions.listme.host.database.HostDataDao;
import com.hkproductions.listme.host.database.HostDatabase;

public class GuestListFragment extends Fragment {

    private ViewDataBinding binding;
    private GuestListViewModel viewModel;
    private HostDataDao datasource;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.host_fragment_guest_list,container,false);

        //initialize datasource
        datasource = HostDatabase.Companion.getInstance(getContext()).getHostDataDao();


        return super.onCreateView(inflater, container, savedInstanceState);
    }


}
