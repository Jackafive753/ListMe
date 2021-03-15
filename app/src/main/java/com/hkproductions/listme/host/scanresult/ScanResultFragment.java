package com.hkproductions.listme.host.scanresult;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

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
import com.hkproductions.listme.host.database.Area;
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
        String hostDatasAsString = ScanResultFragmentArgs.fromBundle(requireArguments()).getHostDatasAsString();
        long areaId = ScanResultFragmentArgs.fromBundle(requireArguments()).getAreaId();

        // View Model intialize
        HostDataDao dataSource =
                HostDatabase.Companion.getInstance(requireActivity().getApplication()).getHostDataDao();
        ScanResultViewModelFactory viewModelFactory =
                new ScanResultViewModelFactory(dataSource, requireContext(), hostDatasAsString, areaId);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ScanResultViewModel.class);

        //set Header
        viewModel.getLData().observe(getViewLifecycleOwner(), list -> {
            if (list.isEmpty()) {
                binding.textviewHostScanresultLabel.setVisibility(View.GONE);
            } else if (list.size() == 1) {
                binding.textviewHostScanresultLabel.setText(getResources().getString(R.string.scannedEntrys_single_text));
            } else {
                binding.textviewHostScanresultLabel.setText(getResources().getString(R.string.scannedEntrys_multi_text));
            }
        });

        //fill Recyclerview
        ScanResultAdapter adap = new ScanResultAdapter();
        viewModel.getLData().observe(getViewLifecycleOwner(), adap::setData);
        binding.recyclerviewHost.setAdapter(adap);

        //fill Spinner
        viewModel.getGivenArea().observe(getViewLifecycleOwner(), area -> {
            if (area == null) {
                ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(requireContext(),
                        R.layout.host_item_spinner);
                binding.spinner.setAdapter(areaAdapter);

                viewModel.getAreaLi().observe(getViewLifecycleOwner(), list -> {
                    areaAdapter.clear();
                    areaAdapter.add(getResources().getString(R.string.scannedEntrys_noArea));
                    for (Area areas : list) {
                        String nArea = areas.getDesignation() + " " + areas.getName();
                        areaAdapter.add(nArea);
                    }
                });

                binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (viewModel.getAreaLi().getValue().size() != 0 && position > 0) {
                            viewModel.getSelectedArea().setValue(viewModel.getAreaLi().getValue().get(position - 1));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                binding.textViewArea.setVisibility(View.GONE);
            } else {
                binding.spinner.setVisibility(View.INVISIBLE);
                binding.textViewArea.setText(area.toString());
                viewModel.getSelectedArea().setValue(area);
            }
        });

        // Buttons ClickListener
        binding.buttonHostScanResultContinue.setOnClickListener(
                event -> {
                    viewModel.save();
                    Navigation.findNavController(requireView())
                            .navigate(ScanResultFragmentDirections.actionScanResultToHostStartView());
                });

        binding.buttonHostScanResultNewScan.setOnClickListener(event -> {
            viewModel.save();
            //open Camer for next scan
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
                viewModel.scannedCode(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
