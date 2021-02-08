package com.hkproductions.listme.host.scanresult;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hkproductions.listme.R;
import com.hkproductions.listme.databinding.HostFragmentScanresultBinding;
import com.hkproductions.listme.host.database.HostDataDao;
import com.hkproductions.listme.host.database.HostDatabase;

public class ScanResultFragment extends Fragment {

    private HostFragmentScanresultBinding binding;
    private ScanResultViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
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
        long[] hostDataIds = ScanResultFragmentArgs.fromBundle(requireArguments()).getHostDataIds();
        // View Model intialize
        HostDataDao dataSource = HostDatabase.Companion.getInstance(requireActivity().getApplication()).getHostDataDao();
        ScanResultViewModelFactory viewModelFactory = new ScanResultViewModelFactory(dataSource, hostDataIds);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ScanResultViewModel.class);

        if (hostDataIds.length == 1) {
            binding.textviewHostScanresultLabel.setText(getResources().getString(R.string.scannedEntrys_single_text));
        } else {
            binding.textviewHostScanresultLabel.setText(getResources().getString(R.string.scannedEntrys_multi_text));
        }
        ScanResultAdapter adap = new ScanResultAdapter();
        viewModel.getLData().observe(getViewLifecycleOwner(), adap::setData);
        binding.recyclerviewHost.setAdapter(adap);

        binding.buttonHostScanResultContinue.setOnClickListener(event -> Navigation.findNavController(requireView()).navigate(ScanResultFragmentDirections.actionScanResultToHostStartView()));

        binding.buttonHostScanresultNewScan.setOnClickListener(event -> {
            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.DATA_MATRIX, IntentIntegrator.QR_CODE);
            integrator.setPrompt(getResources().getString(R.string.host_scan_header));
            integrator.setOrientationLocked(true);
            integrator.initiateScan();
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                viewModel.scannedCode(result.getContents(), requireContext());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
