package com.hkproductions.listme.host.scanresult;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hkproductions.listme.R;
import com.hkproductions.listme.databinding.HostFragmentScanresultBinding;
import com.hkproductions.listme.host.database.HostDataDao;
import com.hkproductions.listme.host.database.HostDatabase;
import com.hkproductions.listme.host.startview.HostStartViewFragmentDirections;

public class ScanResultFragment extends Fragment {

    private HostFragmentScanresultBinding binding;
    private ScanResultViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,
                R.layout.host_fragment_scanresult,
                container,
                false
        );

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get Arguments
        long[] hostDataIds= ScanResultFragmentArgs.fromBundle(requireArguments()).getHostDataIds();
        // View Model intialize
        HostDataDao dataSource= HostDatabase.Companion.getInstance(requireActivity().getApplication()).getHostDataDao();
        ScanResultViewModelFactory viewModelFactory = new ScanResultViewModelFactory(dataSource, hostDataIds);
        viewModel= new ViewModelProvider(this,viewModelFactory).get(ScanResultViewModel.class);

        if(hostDataIds.length==1){
            binding.textviewHostScanresultLabel.setText(getResources().getString(R.string.scannedEntrys_single_text));
        }else {
            binding.textviewHostScanresultLabel.setText(getResources().getString(R.string.scannedEntrys_multi_text));
        }
        ScanResultAdapter adap= new ScanResultAdapter();
        viewModel.getLData().observe(getViewLifecycleOwner(),list -> adap.setData(list));
        binding.recyclerviewHost.setAdapter(adap);


    }
}
